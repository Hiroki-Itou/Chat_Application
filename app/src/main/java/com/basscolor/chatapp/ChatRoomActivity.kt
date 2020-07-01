package com.basscolor.chatapp

import android.app.Activity
import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import com.google.firebase.firestore.QuerySnapshot

class ChatRoomActivity:Activity() {

    private lateinit var chatRoomActivityListener: ChatRoomActivityListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatroom)

        val chatroom = intent.getSerializableExtra("chatroom") as Chatroom
        val listView = findViewById<ListView>(R.id.chatListView)
        chatRoomActivityListener = ChatRoomActivityController(this, chatroom, listView)

        val editText = findViewById<EditText>(R.id.editMessage)
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p: Editable?) {
                chatRoomActivityListener.onInputMessage(p.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        val sendButton = findViewById<ImageButton>(R.id.sendButton)
        sendButton.setOnClickListener {

            editText.editableText.clear()
            chatRoomActivityListener.sendMessage()
        }
    }
}