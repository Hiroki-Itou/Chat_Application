package com.basscolor.chatapp

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView

class ChatRoomListActivity : Activity() {

    private lateinit var chatListController: ChatRoomListActivityListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatlist)
        val chatList = findViewById<ListView>(R.id.chatList)
        chatListController = ChatRoomListActivityController(this,chatList)

        val createRoomButton = findViewById<Button>(R.id.create_room)
        createRoomButton.setOnClickListener {
            chatListController.addChatlist()
        }

        val logOutButton = findViewById<Button>(R.id.logout_Button)
        logOutButton.setOnClickListener {
            chatListController.logOut()
        }


        chatListController.loadChatList()
        chatList.setOnItemClickListener{parent,view,position,id->

            val item = parent.adapter.getItem(position) as MutableMap<String,Any>
            chatListController.selectChatRoom(item)
        }

    }

    override fun onRestart() {
        super.onRestart()
        chatListController.reload()
    }

}