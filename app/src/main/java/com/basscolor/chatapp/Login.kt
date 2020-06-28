package com.basscolor.chatapp

import android.content.ContentValues
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class Login(val loginDelegate: LoginDelegate){



    fun loginToChat(email:String,password:String){

        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task : Task<AuthResult> ->
                if (task.isSuccessful) {
                    loginDelegate.loginSuccess()
                } else {
                    loginDelegate.loginError(task.exception)
                }
            }
    }

}