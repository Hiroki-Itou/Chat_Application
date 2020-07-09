package com.basscolor.chatapp.Activity

import android.app.Activity
import android.content.pm.PackageManager
import io.skyway.Peer.Browser.Canvas
import io.skyway.Peer.Browser.MediaConstraints
import io.skyway.Peer.Browser.MediaStream
import io.skyway.Peer.Browser.Navigator
import io.skyway.Peer.CallOption
import io.skyway.Peer.MediaConnection
import io.skyway.Peer.OnCallback
import io.skyway.Peer.Peer
import io.skyway.Peer.PeerError
import io.skyway.Peer.PeerOption
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Handler
import android.os.Looper
import android.view.Window.FEATURE_NO_TITLE
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.Window
import com.basscolor.chatapp.R
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Button
import android.widget.ImageButton
import com.basscolor.chatapp.ViewModel

class VideocallActivity :Activity(){

    lateinit var viewModel: ViewModel
    lateinit var localStreamView:Canvas
    lateinit var remoteStreamView:Canvas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videocall)

        localStreamView = findViewById(R.id.localStreamView) as Canvas
        remoteStreamView = findViewById(R.id.remoteStreamView) as Canvas

        viewModel = ViewModel(this, localStreamView, remoteStreamView)
        viewModel.setup()

        val videoButton = findViewById<ImageButton>(R.id.videoButton)
        videoButton.setOnClickListener {

            viewModel.toCall()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> {
                if (grantResults.count() > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    viewModel.setupPeer()
                } else {
                    print("Error")
                }
            }
        }
    }
}