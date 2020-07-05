package com.basscolor.chatapp

import android.app.Activity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton

class ChatRoomActivity:Activity() {

    private lateinit var chatRoomActivityListener: ChatRoomActivityListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatroom)

        val intent = getIntent()
        val chatRoom = intent.getSerializableExtra("chatRoom") as ChatRoom

        chatRoomActivityListener = ChatRoomActivityController(this,chatRoom)

        val editText = findViewById<EditText>(R.id.editText)
        val sendButton = findViewById<ImageButton>(R.id.sendButton)


        sendButton.setOnClickListener {

            if(editText.text == null)return@setOnClickListener

            chatRoomActivityListener.toSpeak(editText.text.toString())
            editText.text.clear()
        }
    }
}