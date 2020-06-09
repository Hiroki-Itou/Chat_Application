package com.basscolor.chatapp

import android.app.Activity
import android.content.Intent

interface Login_Activity_Listener {

    fun login_Check(activity: Activity)
    fun get_mailAddress(email:String)
    fun get_password(password:String)
    fun log_in(activity: Activity)
    fun to_user_registration_screen(activity: Activity)

}