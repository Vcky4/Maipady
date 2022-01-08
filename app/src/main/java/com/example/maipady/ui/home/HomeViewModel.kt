package com.example.maipady.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _cgpa = MutableLiveData<String>()

    fun setCgpa(cgpa: String){
        _cgpa.value = cgpa
    }
    val cgpa: LiveData<String> = _cgpa
}