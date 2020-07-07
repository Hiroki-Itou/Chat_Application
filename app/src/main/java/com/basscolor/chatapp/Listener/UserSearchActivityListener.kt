package com.basscolor.chatapp.Listener

import android.app.Activity

interface UserSearchActivityListener {

    val activity : Activity
    fun search(userName:String)
    fun addRoom()

}