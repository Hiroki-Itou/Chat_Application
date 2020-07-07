package com.basscolor.chatapp.Listener

import android.app.Activity
import com.basscolor.chatapp.ChatRoom

interface ChatRoomActivityListener {

    val activity : Activity
    val chatRoom : ChatRoom

    fun onInputMessage(message:String)

    fun toSpeak(message:String)

}