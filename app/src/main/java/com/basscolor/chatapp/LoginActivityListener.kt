package com.basscolor.chatapp

import android.app.Activity

interface LoginActivityListener {


    val activity : Activity
    fun login_Check()
    fun get_mailAddress(email:String)
    fun get_password(password:String)
    fun log_in()
    fun to_user_registration_screen()

}