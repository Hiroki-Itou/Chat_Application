package com.basscolor.chatapp.Activity

import android.app.Activity
import android.content.pm.PackageManager
import io.skyway.Peer.Browser.Canvas
import android.os.Bundle
import android.util.Log
import com.basscolor.chatapp.R
import android.widget.ImageButton
import com.basscolor.chatapp.Controller.VideocallActivityController
import com.basscolor.chatapp.Deta.Chatroom
import com.basscolor.chatapp.Listener.VideocallActivityListener

class VideocallActivity :Activity(){


    private lateinit var videocallActivityController:VideocallActivityListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videocall)

        val intent = getIntent()
        val chatroom = intent.getSerializableExtra("chatroom") as Chatroom

        videocallActivityController = VideocallActivityController(this,chatroom)
        val videoButton = findViewById<ImageButton>(R.id.videoButton)
        videoButton.setOnClickListener {
            videocallActivityController.Call()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> {
                if (grantResults.count() > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    videocallActivityController.VideoSetup()
                } else {
                    print("Error")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Destroy",this.localClassName+"は破壊されました")
    }
}