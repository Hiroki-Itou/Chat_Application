package com.basscolor.chatapp.Listener

import android.app.Activity

interface VideocallActivityListener{

    val activity : Activity

    val action : String

    fun toHangUp()

}