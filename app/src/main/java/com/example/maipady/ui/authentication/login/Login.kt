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
import co.paystack.android.PaystackSdk.applicationContext
import com.example.maipady.R
import com.example.maipady.databinding.LoginFragmentBinding
import com.example.maipady.ui.authentication.AuthSharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnFailureListener

import com.google.firebase.auth.SignInMethodQueryResult

import com.google.android.gms.tasks.OnCompleteListener




class Login : Fragment() {


    private lateinit var binding: LoginFragmentBinding
    private lateinit var auth: FirebaseAuth
    private val authSharedViewModel: AuthSharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = LoginFragmentBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        // Initialize Firebase Auth
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goButton.isEnabled = false
        val watcher: TextWatcher = object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val s1 = binding.emailText.text.toString()
                val s2 = binding.regNoText.text.toString()


                //check if they are empty, make button not clickable
                binding.goButton.isEnabled = !(s1.isEmpty() || s2.isEmpty())
            }
            override fun afterTextChanged(s: Editable?) {}

        }

        binding.emailText.addTextChangedListener(watcher)
        binding.regNoText.addTextChangedListener(watcher)

        binding.goButton.setOnClickListener {
            authSharedViewModel.emailValue(binding.emailText.text.toString())
            authSharedViewModel.regNoValue(binding.regNoText.text.toString())
            checkEmailExistsOrNot(binding.emailText.text.toString(), binding.regNoText.text.toString())
            }

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
            //reload()
        }
    }

    private fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "createWithEmail:success")
                Toast.makeText(context,"success!",Toast.LENGTH_SHORT).show()
            }else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithEmail:failure", task.exception)
                Toast.makeText(applicationContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(applicationContext, "login successful!", Toast.LENGTH_LONG).show()
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(applicationContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkEmailExistsOrNot(email: String, regNo: String) {
        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener { task ->
            Log.d(TAG, "" + (task.result?.signInMethods?.size ?: 0))
            if (task.result?.signInMethods?.size == 0) {
                // email not existed
                binding.goButton.visibility = GONE
                binding.registerLayout.visibility = VISIBLE
            } else {
                // email existed
                //login
                login(email, regNo)

            }
        }.addOnFailureListener(OnFailureListener { e -> e.printStackTrace() })
    }
}