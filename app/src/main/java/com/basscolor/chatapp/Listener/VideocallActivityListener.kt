package com.basscolor.chatapp.Listener

import android.app.Activity
import com.basscolor.chatapp.Model.CallData

interface VideocallActivityListener{

    val activity : Activity
    val callData : CallData

    fun toHangUp()

}