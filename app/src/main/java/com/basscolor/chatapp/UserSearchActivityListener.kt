package com.basscolor.chatapp

import android.app.Activity

interface UserSearchActivityListener {

    val activity : Activity
    fun search(userName:String)
    fun addRoom()

}