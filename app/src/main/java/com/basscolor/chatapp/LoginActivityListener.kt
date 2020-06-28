package com.basscolor.chatapp

import android.app.Activity

interface LoginActivityListener {


    val activity : Activity
    fun loginCheck()
    fun onInputMailAddress(email:String)
    fun onInputPassword(password:String)
    fun onLogIn()
    fun toUserRregistration()

}