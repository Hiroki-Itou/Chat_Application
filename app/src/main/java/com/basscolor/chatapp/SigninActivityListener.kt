package com.basscolor.chatapp

import android.app.Activity

interface SigninActivityListener {

    val activity : Activity
    fun get_userName(name:String)
    fun get_mailAddress(email:String)
    fun get_password(password:String)
    fun get_confirmationPass(confirmationPass:String)
    fun sign_in()

}