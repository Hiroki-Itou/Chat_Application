package com.basscolor.chatapp

import android.app.Activity
import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout

interface LoginActivityListener {


    val progressView : ConstraintLayout
    val activity : Activity
    fun login_Check()
    fun get_mailAddress(email:String)
    fun get_password(password:String)
    fun log_in()
    fun to_user_registration_screen()

}