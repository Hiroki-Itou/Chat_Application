package com.basscolor.chatapp

import android.app.Activity
import android.widget.ScrollView

interface ChatRoomActivityListener {

    val activity : Activity
    val chatRoom : ChatRoom

    fun onInputMessage(message:String)

    fun toSpeak(message:String)

}