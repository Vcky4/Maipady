package com.example.maipady.payment

import android.app.Application
import co.paystack.android.PaystackSdk

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        //Initialize Paystack
        PaystackSdk.initialize(applicationContext)

    }
}