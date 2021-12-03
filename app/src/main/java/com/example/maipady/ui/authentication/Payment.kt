package com.example.maipady.ui.authentication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import co.paystack.android.Paystack
import co.paystack.android.PaystackSdk
import co.paystack.android.PaystackSdk.applicationContext
import co.paystack.android.Transaction
import co.paystack.android.exceptions.ExpiredAccessCodeException
import co.paystack.android.model.Card
import co.paystack.android.model.Charge
import com.example.maipady.R
import com.example.maipady.databinding.PaymentFragmentBinding
import com.example.maipady.payment.CreditCardTextFormatter
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONException
import java.util.*

class Payment : Fragment() {

    private var transaction: Transaction? = null
    private var charge: Charge? = null
    private lateinit var binding: PaymentFragmentBinding
    private lateinit var auth: FirebaseAuth
    private val authSharedViewModel: AuthSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = PaymentFragmentBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        initViews()
        return binding.root
    }


    /**
     * Initialize all views here
     */

    private fun initViews(){

        //add text watcher to edit text fields
        addTextWatcherToEditText()

        //set the amount to pay in the button, this could be dynamic, that is why it wasn't added in the activity layout
        authSharedViewModel.getAmount()
        authSharedViewModel.amount.observe(viewLifecycleOwner,{
            binding.btnPay.text = "pay ₦$it"
        })
        //handle clicks here
        handleClicks()

    }

    /**
     * Add formatting to card number, cvv, and expiry date
     */

    private fun addTextWatcherToEditText(){

        //Make button UnClickable for the first time
        binding.btnPay.isEnabled = false

        //make the button clickable after detecting changes in input field
        val watcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            @SuppressLint("StringFormatInvalid")
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                val s1 = binding.etCardNumber.text.toString()
                val s2 = binding.etExpiry.text.toString()
                val s3 = binding.etCvv.text.toString()

                //check if they are empty, make button not clickable
                if (s1.isEmpty() || s2.isEmpty() || s3.isEmpty()) {
                    binding.btnPay.isEnabled = false
                }


                //check the length of all edit text, if meet the required length, make button clickable
                if (s1.length >= 16 && s2.length == 5 && s3.length == 3) {
                    binding.btnPay.isEnabled = true

                }

                //if edit text doesn't meet the required length, make button not clickable. You could use else statement from the above if
                if (s1.length < 16 || s2.length < 5 || s3.length < 3) {
                    binding.btnPay.isEnabled = false
                }

                //add a slash to expiry date after first two character(month)
                if (s2.length == 2) {
                    if (start == 2 && before == 1 && !s2.contains("/")) {
                        binding.etExpiry.setText(getString(R.string.expiry_space, s2[0]))
                        binding.etExpiry.setSelection(1)
                    } else {
                        binding.etExpiry.append("/")
//                        binding.etExpiry.setText(getString(R.string.expiry_slash, s2))
                        binding.etExpiry.setSelection(3)
                    }
                }


            }

            override fun afterTextChanged(s: Editable) {
                val s2 = binding.etExpiry.text.toString()
                if (s2.length == 5){
                    binding.etCvv.requestFocus()
                }
            }
        }

        //add text watcher
        binding.etCardNumber.addTextChangedListener(CreditCardTextFormatter()) //helper class for card number formatting
        binding.etExpiry.addTextChangedListener(watcher)
        binding.etCvv.addTextChangedListener(watcher)


    }

    private fun handleClicks(){

        //on click of pay button
        binding.btnPay.setOnClickListener {

            //here you can check for network availability first, if the network is available, continue
            if (Utility.isNetworkAvailable(applicationContext)) {

                //show loading
                binding.loadingPayOrder.visibility = View.VISIBLE
                binding.btnPay.isEnabled = false

                //perform payment
                doPayment()

            } else {

                Toast.makeText(context, "Please check your internet", Toast.LENGTH_LONG).show()

            }

        }
    }

    private fun doPayment(){

        val publicTestKey = "pk_test_5926278aec22dac4025a312b75dca51e37c6c0ad"

        //set public key
        PaystackSdk.setPublicKey(publicTestKey)

        // initialize the charge
        charge = Charge()
        charge!!.card = loadCardFromForm()
        authSharedViewModel.amount.observe(viewLifecycleOwner,{
            val amount = it.toInt().times(100)
            charge!!.amount = amount
        })
        authSharedViewModel.email.observe(viewLifecycleOwner,{
            charge!!.email = it
        })
        charge!!.reference = "payment" + Calendar.getInstance().timeInMillis

        try {
            charge!!.putCustomField("Charged From", "Android SDK")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        doChargeCard()

    }


    private fun loadCardFromForm(): Card {

        //validate fields

        val cardNumber = binding.etCardNumber.text.toString().trim()
        val expiryDate = binding.etExpiry.text.toString().trim()
        val cvc = binding.etCvv.text.toString().trim()

        //formatted values
        val cardNumberWithoutSpace = cardNumber.replace(" ", "")
        val monthValue = expiryDate.substring(0, expiryDate.length.coerceAtMost(2))
        val yearValue = expiryDate.takeLast(2)

        //build card object with ONLY the number, update the other fields later
        val card: Card = Card.Builder(cardNumberWithoutSpace, 0, 0, "").build()

        //update the cvc field of the card
        card.cvc = cvc

        //validate expiry month;
        val sMonth: String = monthValue
        var month = 0
        try {
            month = sMonth.toInt()
        } catch (ignored: Exception) {
        }

        card.expiryMonth = month

        //validate expiry year
        val sYear: String = yearValue
        var year = 0
        try {
            year = sYear.toInt()
        } catch (ignored: Exception) {
        }
        card.expiryYear = year

        return card

    }


    /**
     * DO charge and receive call backs - successful and failed payment
     */

    private fun doChargeCard(){

        transaction = null

        PaystackSdk.chargeCard(requireActivity(), charge, object : Paystack.TransactionCallback {

            // This is called only after transaction is successful
            override fun onSuccess(transaction: Transaction) {

                //hide loading
                binding.loadingPayOrder.visibility = View.GONE
                binding.btnPay.isEnabled = true

                //successful, show a toast or navigate to another activity or fragment
                Toast.makeText(context, "Yeah!!, Payment was successful", Toast.LENGTH_LONG).show()

                this@Payment.transaction = transaction

                //now you can store the transaction reference, and perform a verification on your backend server
                findNavController().navigate(R.id.action_payment_to_sign_up)

            }

            // This is called only before requesting OTP
            // Save reference so you may send to server if
            // error occurs with OTP
            // No need to dismiss dialog

            override fun beforeValidate(transaction: Transaction) {

                this@Payment.transaction = transaction

            }

            override fun onError(error: Throwable, transaction: Transaction) {

                //stop loading
                binding.loadingPayOrder.visibility = View.GONE
                binding.btnPay.isEnabled = true

                // If an access code has expired, simply ask your server for a new one
                // and restart the charge instead of displaying error

                this@Payment.transaction = transaction
                if (error is ExpiredAccessCodeException) {
                    this@Payment.doChargeCard()
                    return
                }


                if (transaction.reference != null) {

                    Toast.makeText(context, error.message?:"", Toast.LENGTH_LONG).show()

                    //also you can do a verification on your backend server here

                } else {

                    Toast.makeText(context, error.message?:"", Toast.LENGTH_LONG).show()

                }
            }

        })

    }
    private fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(ContentValues.TAG, "createWithEmail:success")
                Toast.makeText(context,"success!",Toast.LENGTH_SHORT).show()
            }else {
                // If sign in fails, display a message to the user.
                Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                Toast.makeText(applicationContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }


    override fun onPause() {
        super.onPause()
        binding.loadingPayOrder.visibility = View.GONE
    }

}