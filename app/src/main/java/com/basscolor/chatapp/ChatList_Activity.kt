package com.basscolor.chatapp

import android.app.Activity
import android.os.Bundle
import android.widget.ListView

class ChatList_Activity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatlist)

        val chatList = findViewById<ListView>(R.id.chatList)


    }

}