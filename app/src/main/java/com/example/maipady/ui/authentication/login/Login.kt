package com.example.maipady.ui.authentication.login

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import co.paystack.android.PaystackSdk
import co.paystack.android.PaystackSdk.applicationContext
import com.example.maipady.R
import com.example.maipady.databinding.LoginFragmentBinding
import com.example.maipady.ui.authentication.AuthSharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.maipady.ui.authentication.Utility

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database


class Login : Fragment() {


    private lateinit var binding: LoginFragmentBinding
    private lateinit var auth: FirebaseAuth
    private val authSharedViewModel: AuthSharedViewModel by activityViewModels()
    private lateinit var database: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = LoginFragmentBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        // Initialize Firebase Auth
        auth = Firebase.auth
        database = Firebase.database.reference
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goButton.isEnabled = false

        //initiate text watchers
        textWatchers()
        //handle go button clicks
        handleClicks()

        binding.payButton.setOnClickListener {
            authSharedViewModel.emailValue(binding.emailText.text.toString())
            authSharedViewModel.regNoValue(binding.regNoText.text.toString())
            findNavController().navigate(R.id.action_login_to_payment)
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            authSharedViewModel.getUser(currentUser.uid)
            findNavController().navigate(R.id.action_login_to_nav_home)
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(applicationContext, "login successful!", Toast.LENGTH_LONG)
                        .show()
                    //navigate to home
                    findNavController().navigate(R.id.action_login_to_nav_home)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(applicationContext, "RegNo does not match email provided", Toast.LENGTH_SHORT)
                        .show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    applicationContext,
                    exception.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun checkEmailExistsOrNot(email: String, regNo: String) {
        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener { task ->
            Log.d(TAG, "" + (task.result?.signInMethods?.size ?: 0))
            if (task.result?.signInMethods?.size == 0) {
                // email not existed
                binding.loadingUser.visibility = GONE
                binding.goButton.visibility = GONE
                binding.registerLayout.visibility = VISIBLE
            } else {
                // email existed
                //login
                binding.loadingUser.visibility = GONE
                login(email, regNo)

            }
        }.addOnFailureListener { e -> e.printStackTrace() }
    }

    private fun handleClicks() {

        //on click of pay button
        binding.goButton.setOnClickListener {

            //here you can check for network availability first, if the network is available, continue
            if (Utility.isNetworkAvailable(applicationContext)) {

                //show loading
                binding.loadingUser.visibility = VISIBLE
                binding.goButton.isEnabled = false

                //save to viewModel
                authSharedViewModel.emailValue(binding.emailText.text.toString())
                authSharedViewModel.regNoValue(binding.regNoText.text.toString())
                //check if already exist
                checkEmailExistsOrNot(
                    binding.emailText.text.toString(),
                    binding.regNoText.text.toString()
                )

            } else {

                Toast.makeText(context, "Please check your internet", Toast.LENGTH_LONG).show()

            }

        }
    }

    private fun textWatchers() {
        val watcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val s1 = binding.emailText.text.toString()
                val s2 = binding.regNoText.text.toString()

                //check if they are empty, make button not clickable
                binding.goButton.isEnabled = !(s1.isEmpty() || s2.isEmpty())

//                if (s2.length == 2) {
//                    binding.regNoText.text?.append("/")
//                }
            }


            override fun afterTextChanged(s: Editable?) {}

        }
        binding.emailText.addTextChangedListener(watcher)
        binding.regNoText.addTextChangedListener(watcher)
    }
}