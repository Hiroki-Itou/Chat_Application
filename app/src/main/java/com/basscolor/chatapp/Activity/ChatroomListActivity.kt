package com.basscolor.chatapp.Activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import com.basscolor.chatapp.Controller.ChatroomListActivityController
import com.basscolor.chatapp.Listener.ChatroomListActivityListener
import com.basscolor.chatapp.R

class ChatroomListActivity : Activity() {

    private lateinit var chatListController: ChatroomListActivityListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatlist)
        val chatList = findViewById<ListView>(R.id.chatList)
        chatListController =
            ChatroomListActivityController(this, chatList)

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


    override fun onDestroy() {
        super.onDestroy()
        Log.d("Destroy",this.localClassName+"は破壊されました")
    }

    override fun onBackPressed() {
        
    }
}