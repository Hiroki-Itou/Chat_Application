package com.basscolor.chatapp

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_chatlist.*

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
    }


    override fun onRestart() {
        super.onRestart()

        chatListController.reload()
    }

}