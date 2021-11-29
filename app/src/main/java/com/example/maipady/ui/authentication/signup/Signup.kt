package com.example.maipady.ui.authentication.signup

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import co.paystack.android.PaystackSdk
import com.example.maipady.R
import com.example.maipady.databinding.SignupFragmentBinding
import com.example.maipady.ui.authentication.AuthSharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
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

        binding.registerButton.setOnClickListener {
            authSharedViewModel.email.observe(viewLifecycleOwner, { email ->
                authSharedViewModel.regNo.observe(viewLifecycleOwner, { regNo ->
                    register(email, regNo)
                })
            })
        }
    }

    private fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(ContentValues.TAG, "createWithEmail:success")
                Toast.makeText(context,"success!", Toast.LENGTH_SHORT).show()
            }else {
                // If sign in fails, display a message to the user.
                Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                Toast.makeText(PaystackSdk.applicationContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(PaystackSdk.applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

//    private fun radioButtonValue() {
//        if (binding.pointRadio5.isChecked){
//            radioButtonValue() => 5
//        }else if (binding.pointRadio4.isChecked){
//            return 4
//        }
//    }
}