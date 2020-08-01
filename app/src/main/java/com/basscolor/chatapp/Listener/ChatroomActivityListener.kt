package com.basscolor.chatapp.Listener

import android.app.Activity
import com.basscolor.chatapp.Deta.Chatroom

interface ChatroomActivityListener {

    val activity : Activity
    val chatroom : Chatroom

    fun onInputMessage(message:String)

    fun toSpeak(message:String)

    fun toCall()

    fun toReject()

    fun toReply()

    fun toDestroy()

    fun setupPeer()

    fun checkPermission()



}