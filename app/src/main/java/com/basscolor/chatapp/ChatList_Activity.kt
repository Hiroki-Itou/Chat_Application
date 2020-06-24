package com.basscolor.chatapp

import android.app.Activity
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import java.lang.reflect.Array

class ChatList_Activity : Activity() {

    private lateinit var chatListController: ChatListActivityListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatlist)

        val createRoomButton = findViewById<Button>(R.id.create_room)
        createRoomButton.setOnClickListener {
            chatListController.add_Chatlist()
        }

        val logOutButton = findViewById<Button>(R.id.logout_Button)
        logOutButton.setOnClickListener {
            chatListController.logOut()
        }
        val chatList = findViewById<ListView>(R.id.chatList)
        chatListController = ChatListController(this,chatList)
        chatListController.load_ChatList()
        chatList.setOnItemClickListener{parent,view,position,id->

            val item = parent.adapter.getItem(position) as MutableMap<String,Any>
            chatListController.select_ChatList(item)
        }

    }

    override fun onRestart() {
        super.onRestart()
        chatListController.reload()
    }

}