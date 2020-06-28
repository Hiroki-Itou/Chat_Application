package com.basscolor.chatapp

import com.google.firebase.auth.FirebaseAuth
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


}