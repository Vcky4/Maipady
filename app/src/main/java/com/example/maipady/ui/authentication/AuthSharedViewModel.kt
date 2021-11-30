package com.example.maipady.ui.authentication

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthSharedViewModel: ViewModel() {
    private val _email = MutableLiveData<String>()
    private val _regNo = MutableLiveData<String>()
    private val _privateKey = MutableLiveData<String>()
    private val _gradeSystem = MutableLiveData<String>()
    private val database: DatabaseReference = Firebase.database.reference
    private val auth: FirebaseAuth = Firebase.auth



    // set email
    fun emailValue(email: String) {
        _email.value = email
    }
    // set regNo
    fun regNoValue(regNo: String){
        _regNo.value = regNo
    }
    //set private key value
    fun privateKeyValue(privateKey: String){
        _privateKey.value = privateKey
    }
    fun gradeSystemValue(gradeSystem: String){
        _gradeSystem.value = gradeSystem
    }
    //public reg no value
    val regNo: LiveData<String> = _regNo
    // public email value
    val email: LiveData<String> = _email
    val privateKey: LiveData<String> = _privateKey
    val gradeSystem: LiveData<String> = _gradeSystem


    fun writeNewUser(userId: String) {

        database.child(userId).child("RegNo").setValue(regNo)
        database.child(userId).child("privateKey").setValue(privateKey)
        database.child(userId).child("GradeSystem").setValue(gradeSystem)
        database.child(userId).child("Results")
    }



}