package com.basscolor.chatapp.Activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import com.basscolor.chatapp.Deta.Chatroom
import com.basscolor.chatapp.Controller.ChatroomActivityController
import com.basscolor.chatapp.Listener.ChatroomActivityListener
import com.basscolor.chatapp.Model.setOnDelayClickListener
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

        replyButton.setOnDelayClickListener ({
            chatRoomActivityListener.toReply()
        })

        rejectButton.setOnDelayClickListener ({
            chatRoomActivityListener.toReject()
        })

        callButton.setOnDelayClickListener ({
          chatRoomActivityListener.toCall()
        })

        sendButton.setOnDelayClickListener( {

            if(editText.text == null)return@setOnDelayClickListener

            chatRoomActivityListener.toSpeak(editText.text.toString())
            editText.text.clear()
        })

    }

    override fun onStart() {
        super.onStart()
        chatRoomActivityListener.checkPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        chatRoomActivityListener.toDestroy()
        Log.d("Destroy",this.localClassName+"は破壊されました")
    }

    private fun toAppSettingsView(){
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package",packageName,null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> {
                if (grantResults.count() > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission","全てのパーミッションが許可されました")
                    chatRoomActivityListener.setupPeer()
                } else {
                    Log.d("Permission","パーミッションが許可されませんでした")

                    AlertDialog.Builder(this).setTitle("Error")
                        .setMessage("アプリを使用するにはアクセス権を許可してください")
                        .setPositiveButton("OK") { _ , _ ->
                            toAppSettingsView()
                        }.setCancelable(false).show()
                }
            }
        }
    }
}