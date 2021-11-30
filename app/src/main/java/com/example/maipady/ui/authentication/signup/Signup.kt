package com.example.maipady.ui.authentication.signup

import android.content.ContentValues
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
import com.example.maipady.R
import com.example.maipady.databinding.SignupFragmentBinding
import com.example.maipady.ui.authentication.AuthSharedViewModel
import com.example.maipady.ui.authentication.Utility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Signup : Fragment() {

    private lateinit var binding: SignupFragmentBinding
    private lateinit var auth: FirebaseAuth
    private val authSharedViewModel: AuthSharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = SignupFragmentBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        // Initialize Firebase Auth
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textWatchers()
        //handle register button clicks
        handleClicks()
    }

    private fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(ContentValues.TAG, "createWithEmail:success")
                binding.loadingUser.visibility = GONE
                //save user details
                auth.currentUser?.let { authSharedViewModel.writeNewUser(it.uid) }
                Toast.makeText(context, "success!", Toast.LENGTH_SHORT).show()
                //navigate to home
                findNavController().navigate(R.id.action_sign_up_to_nav_home)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                binding.loadingUser.visibility = GONE
                Toast.makeText(
                    PaystackSdk.applicationContext,
                    "Registration failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(
                PaystackSdk.applicationContext,
                exception.localizedMessage,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun handleClicks() {

        //on click of pay button
        binding.registerButton.setOnClickListener {

            //here you can check for network availability first, if the network is available, continue
            if (Utility.isNetworkAvailable(PaystackSdk.applicationContext)) {

                //show loading
                binding.loadingUser.visibility = VISIBLE
                binding.registerButton.isEnabled = false

                //save to viewModel
                authSharedViewModel.privateKeyValue(binding.privateKeyText.text.toString())
                if (binding.pointRadio5.isChecked) {
                    authSharedViewModel.gradeSystemValue("5")
                } else if (binding.pointRadio4.isChecked) {
                    authSharedViewModel.gradeSystemValue("4")
                }

                //register user
                authSharedViewModel.email.observe(viewLifecycleOwner, { email ->
                    authSharedViewModel.regNo.observe(viewLifecycleOwner, { regNo ->
                        register(email, regNo)
                    })
                })

            } else {

                Toast.makeText(context, "Please check your internet", Toast.LENGTH_LONG).show()

            }

        }
    }

    private fun textWatchers() {
        val watcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val s1 = binding.privateKeyText.text.toString()

                //check if they are empty, make button not clickable
                binding.registerButton.isEnabled = s1.isNotEmpty()

            }


            override fun afterTextChanged(s: Editable?) {}

        }
        binding.privateKeyText.addTextChangedListener(watcher)
    }
}