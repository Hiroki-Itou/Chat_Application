package com.basscolor.chatapp.Model

import android.annotation.SuppressLint
import io.skyway.Peer.MediaConnection
import io.skyway.Peer.Peer

class CallData{


    companion object {
        val API_KEY = "920114e2-7d0f-4dc9-bc37-421e04b651fb"
        val DOMAIN  = "com.basscolor.chatapp"
        @SuppressLint("StaticFieldLeak")
        var peer: Peer? = null
        var peerUserID:String? = null
        var mediaConnection: MediaConnection? = null
    }

}
