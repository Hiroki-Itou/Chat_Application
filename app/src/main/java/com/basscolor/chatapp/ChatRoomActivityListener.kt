package com.basscolor.chatapp

import android.app.Activity
import android.widget.ListView

interface ChatRoomActivityListener {

    val activity : Activity
    val chatroom : Chatroom
    val listView : ListView

    fun onInputMessage(message:String)
    fun sendMessage()

}