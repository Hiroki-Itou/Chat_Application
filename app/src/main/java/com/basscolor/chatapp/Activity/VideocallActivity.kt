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

class VideocallActivity :Activity(){


    private lateinit var videocallActivityController:VideocallActivityListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videocall)

        val intent = getIntent()
        val chatroom = intent.getSerializableExtra("chatroom") as Chatroom

        videocallActivityController = VideocallActivityController(this,chatroom.getPeerUserID())
        val videoButton = findViewById<ImageButton>(R.id.videoButton)
        videoButton.setOnClickListener {
            videocallActivityController.toCall()
        }

        val answerButton = findViewById<ImageButton>(R.id.answerButton)
        answerButton.setOnClickListener {
            videocallActivityController.toAnswer()
        }

        val refusalButton = findViewById<ImageButton>(R.id.refusalButton)
        refusalButton.setOnClickListener {
            videocallActivityController.toHangUp()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> {
                if (grantResults.count() > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    videocallActivityController.toVideoSetup()
                } else {
                    print("Error")
                }
            }
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
        videocallActivityController.todestroy()
        Log.d("Destroy",this.localClassName+"は破壊されました")
    }
}