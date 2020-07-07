package com.basscolor.chatapp.Listener

import android.app.Activity
import android.widget.ListView
import android.widget.SimpleAdapter

interface ChatRoomListActivityListener {

    val activity : Activity
    var listView : ListView

    fun loadChatList()
    fun addChatlist()
    fun selectChatRoom(mutableMap:MutableMap<String,Any>)
    fun logOut()
    fun reload()

}