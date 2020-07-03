package com.basscolor.chatapp

import android.app.Activity
import android.os.Bundle

class ChatRoomActivity:Activity() {

    private lateinit var chatRoomActivityListener: ChatRoomActivityListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatroom)

        val intent = getIntent()
        val chatRoom = intent.getSerializableExtra("chatRoom") as ChatRoom
//
        chatRoomActivityListener = ChatRoomActivityController(this,chatRoom)

    }
}