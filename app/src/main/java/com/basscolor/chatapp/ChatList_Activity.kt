package com.basscolor.chatapp

import android.app.Activity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast

class ChatList_Activity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatlist)

        val chatList = findViewById<ListView>(R.id.chatList)

        Toast.makeText(baseContext, "Chatへようこそ！！！",
            Toast.LENGTH_LONG).show()

    }

}