package com.basscolor.chatapp.Listener

import android.app.Activity
import com.basscolor.chatapp.Chatroom

interface ChatroomActivityListener {

    val activity : Activity
    val chatroom : Chatroom

    fun onInputMessage(message:String)

    fun toSpeak(message:String)

}