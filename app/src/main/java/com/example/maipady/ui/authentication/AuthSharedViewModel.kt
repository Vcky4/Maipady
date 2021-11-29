package com.example.maipady.ui.authentication

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthSharedViewModel: ViewModel() {
    private val _email = MutableLiveData<String>()
    private val _regNo = MutableLiveData<String>()




    // set email
    fun emailValue(email: String) {
        _email.value = email
    }
    //public reg no value
    val regNo: LiveData<String> = _regNo
    // public email value
    val email: LiveData<String> = _email

    // set regNo
    fun regNoValue(regNo: String){
        _regNo.value = regNo
    }

}