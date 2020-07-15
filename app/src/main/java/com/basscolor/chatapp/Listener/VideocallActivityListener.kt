package com.basscolor.chatapp.Listener

import android.app.Activity

interface VideocallActivityListener{

    val activity : Activity
    val peerUserID : String

    fun toCall()

    fun toHangUp()

    fun toAnswer()

    fun toVideoSetup()

    fun todestroy()

}