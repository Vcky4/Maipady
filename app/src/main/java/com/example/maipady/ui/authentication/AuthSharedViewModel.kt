package com.example.maipady.ui.authentication

import android.R.attr
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.firebase.database.DatabaseError

import androidx.annotation.NonNull

import android.R.attr.data

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener




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
        database.child(userId).child("email").setValue(email)
        database.child(userId).child("Results")
    }

    fun getUser(userId: String){
        //get reg no.
        database.child(userId).child("RegNo")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    regNoValue(dataSnapshot.child("value").value.toString())
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        //get email
        database.child(userId).child("email")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    emailValue(dataSnapshot.child("value").value.toString())
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        //get private key
        database.child(userId).child("privateKey")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    privateKeyValue(dataSnapshot.child("value").value.toString())
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        database.child(userId).child("GradeSystem")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    gradeSystemValue(dataSnapshot.child("value").value.toString())
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }
    private val _amount = MutableLiveData<String>()
    val amount: LiveData<String> = _amount
    fun getAmount(){
        database.child("amount")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    _amount.value = snapshot.value.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}