package com.example.maipady.ui.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class example: ViewModel() {

    private var  auth: FirebaseAuth? = null
    private var storage: FirebaseStorage
    private var storageReference: StorageReference
    private  var rootNode: FirebaseDatabase
    private  var reference: DatabaseReference
    var loading: MutableLiveData<Boolean> = MutableLiveData()
    init {

        auth = FirebaseAuth.getInstance()    //Authentication
        storage = FirebaseStorage.getInstance()   //File/image uploads
        storageReference = storage.reference    //Reference to the storage
        rootNode = FirebaseDatabase.getInstance()   // Realtime DB
        reference = rootNode.getReference("taxInfo") //Reference to DBTable
        loading.postValue(false)

    }

   fun register(email: String, password: String){
       auth?.createUserWithEmailAndPassword(email,password)
           ?.addOnCompleteListener { task: Task<AuthResult> ->
               if(!task.isSuccessful){
//                   println("Registration Failed with ${task.exception}")
//                   _registrationStatus.postValue(ResultOf.Success("Registration Failed with ${task.exception}"))
               }else{
                  // _registrationStatus.postValue(ResultOf.Success("UserCreated"))

               }
               loading.postValue(false)
           }
   }

    fun login(email: String, password: String){
        auth?.let{ login->
            login.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {task: Task<AuthResult> ->

                    if(!task.isSuccessful){
//                        println("Login Failed with ${task.exception}")
//                        _signInStatus.postValue(ResultOf.Success("Login Failed with ${task.exception}"))
                    }else{
//                        _signInStatus.postValue(ResultOf.Success("Login Successful"))

                    }
                    loading.postValue(false)
                }

        }
    }

    fun signOut(){
        auth?.let {authentation ->
            authentation.signOut()
//            _signOutStatus.postValue(ResultOf.Success("Signout Successful"))
            loading.postValue(false)
        }
    }

    fun saveDoc(){
        
    }
}