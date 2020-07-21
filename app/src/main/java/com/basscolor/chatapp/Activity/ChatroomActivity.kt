package com.basscolor.chatapp.Activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import com.basscolor.chatapp.Deta.Chatroom
import com.basscolor.chatapp.Controller.ChatroomActivityController
import com.basscolor.chatapp.Listener.ChatroomActivityListener
import com.basscolor.chatapp.R

class ChatroomActivity:Activity() {

    private lateinit var chatRoomActivityListener: ChatroomActivityListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatroom)

        val intent = intent
        val chatroom = intent.getSerializableExtra("chatroom") as Chatroom

        chatRoomActivityListener =
            ChatroomActivityController(this, chatroom)

        val callButton = findViewById<ImageButton>(R.id.callButton)
        val editText = findViewById<EditText>(R.id.editText)
        val sendButton = findViewById<ImageButton>(R.id.sendButton)
        val replyButton = findViewById<ImageButton>(R.id.replyButton)
        val rejectButton = findViewById<ImageButton>(R.id.rejectButton)

        replyButton.setOnClickListener {
            chatRoomActivityListener.transition()
        }

        rejectButton.setOnClickListener {
            chatRoomActivityListener.toReject()
        }

        callButton.setOnClickListener {
          chatRoomActivityListener.toCall()
        }

        sendButton.setOnClickListener {

            if(editText.text == null)return@setOnClickListener

            chatRoomActivityListener.toSpeak(editText.text.toString())
            editText.text.clear()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        chatRoomActivityListener.toDestroy()
        Log.d("Destroy",this.localClassName+"は破壊されました")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> {
                if (grantResults.count() > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    chatRoomActivityListener.setupPeer()
                } else {
                    Log.d("Permission","パーミッションエラーが発生しました")
                }
            }
        }
    }
}