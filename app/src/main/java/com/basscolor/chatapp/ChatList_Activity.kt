package com.basscolor.chatapp

import android.app.Activity
import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast

class ChatList_Activity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatlist)

        val chatList = findViewById<ListView>(R.id.chatList)

        val room_list: MutableList<MutableMap<String,Any>> = mutableListOf()
        val room = mutableMapOf("imageView" to R.drawable.icon,"name" to "バスカラ", "last_message" to "こんにちは")
        (1..20).forEach loop@{ _ ->
            room_list.add(room)
        }

// SimpleAdapter第4引数from用データの用意
        val from = arrayOf("imageView","name", "last_message")
        // SimpleAdapter第5引数to用データの用意
        val to = intArrayOf(R.id.imageView,R.id.friend_name, R.id.last_message)

        val adapter = SimpleAdapter(this,room_list,R.layout.list_items,from,to)

        chatList.adapter = adapter

//        Toast.makeText(baseContext, "Chatへようこそ！！！",
//            Toast.LENGTH_LONG).show()

    }

}