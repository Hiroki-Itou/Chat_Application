package com.basscolor.chatapp.Listener

import android.app.Activity
import com.basscolor.chatapp.Deta.Chatroom

interface VideocallActivityListener{

    val activity : Activity
    val chatroom : Chatroom

    fun Call()

    fun HangUp()

    fun VideoSetup()

}