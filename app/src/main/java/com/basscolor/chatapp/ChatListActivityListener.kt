package com.basscolor.chatapp

import android.app.Activity
import android.widget.ListView
import android.widget.SimpleAdapter

interface ChatListActivityListener {

    val activity : Activity
    var listView : ListView

    fun load_ChatList()
    fun add_Chatlist()
    fun select_ChatList()
    fun logOut()
    fun reload()


}