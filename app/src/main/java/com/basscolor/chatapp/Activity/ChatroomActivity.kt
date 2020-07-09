package com.basscolor.chatapp.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import com.basscolor.chatapp.Chatroom
import com.basscolor.chatapp.Controller.ChatroomActivityController
import com.basscolor.chatapp.Listener.ChatroomActivityListener
import com.basscolor.chatapp.R

class ChatroomActivity:Activity() {

    private lateinit var chatRoomActivityListener: ChatroomActivityListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatroom)

        val intent = getIntent()
        val chatRoom = intent.getSerializableExtra("chatRoom") as Chatroom

        chatRoomActivityListener =
            ChatroomActivityController(this, chatRoom)

        val callButton = findViewById<ImageButton>(R.id.callButton)
        val editText = findViewById<EditText>(R.id.editText)
        val sendButton = findViewById<ImageButton>(R.id.sendButton)

        callButton.setOnClickListener {
            val intent = Intent(this, VideocallActivity::class.java)
            this.startActivity(intent)
        }

        sendButton.setOnClickListener {

            if(editText.text == null)return@setOnClickListener

            chatRoomActivityListener.toSpeak(editText.text.toString())
            editText.text.clear()
        }
    }
}