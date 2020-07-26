package com.basscolor.chatapp.Activity

import android.app.Activity
import android.content.pm.PackageManager
import android.media.AudioManager
import io.skyway.Peer.Browser.Canvas
import android.os.Bundle
import android.util.Log
import com.basscolor.chatapp.R
import android.widget.ImageButton
import com.basscolor.chatapp.Controller.VideocallActivityController
import com.basscolor.chatapp.Deta.Chatroom
import com.basscolor.chatapp.Listener.VideocallActivityListener
import com.basscolor.chatapp.Model.CallData

class VideocallActivity :Activity(){


    private lateinit var videocallActivityController:VideocallActivityListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videocall)

        val intent = intent
        val action = intent.getStringExtra("ACTION")
        videocallActivityController = VideocallActivityController(this,action)

        val hangUpButton = findViewById<ImageButton>(R.id.hangUpButton)
        hangUpButton.setOnClickListener {
            videocallActivityController.toHangUp()
        }



    }

    override fun onResume() {
        super.onResume()
        volumeControlStream = AudioManager.STREAM_VOICE_CALL
    }

    override fun onPause() {
        super.onPause()
        volumeControlStream = AudioManager.USE_DEFAULT_STREAM_TYPE
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Destroy",this.localClassName+"は破壊されました")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        videocallActivityController.toHangUp()
    }
}