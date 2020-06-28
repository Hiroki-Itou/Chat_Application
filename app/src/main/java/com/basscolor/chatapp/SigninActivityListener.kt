package com.basscolor.chatapp

import android.app.Activity

interface SigninActivityListener {

    val activity : Activity
    fun onInputUserName(name:String)
    fun onInputMailAddress(email:String)
    fun onInputPassword(password:String)
    fun onInputConfirmationPass(confirmationPass:String)
    fun onSignIn()


}