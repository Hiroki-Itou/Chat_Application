package com.basscolor.chatapp

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class Authentication {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


    fun get_currentUid():String?{
        return if(isLogin()){
            firebaseAuth.currentUser!!.uid
        }else{
            null
        }
    }

    fun isLogin():Boolean {
        return firebaseAuth.currentUser != null
    }

    fun signin(email:String,password: String,signinDelegate: Signin_Delegate){

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    signinDelegate.success()
                } else {
                    signinDelegate.error(task.exception)
                }
            }
    }

    fun login(email:String,password:String,loginDelegate: Login_Delegate){

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task :Task<AuthResult>->
                if (task.isSuccessful) {
                    loginDelegate.success()
                } else {
                    loginDelegate.error(task.exception)
                }
            }
    }


    interface Signin_Delegate{

        fun success()

        fun error(exception: Exception?)
    }
    interface Login_Delegate{

        fun success()

        fun error(exception: Exception?)
    }

}