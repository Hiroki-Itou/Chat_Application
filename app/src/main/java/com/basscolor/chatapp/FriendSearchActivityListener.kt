package com.basscolor.chatapp

import android.app.Activity

interface FriendSearchActivityListener {

    val activity : Activity
    fun search(userName:String)

}