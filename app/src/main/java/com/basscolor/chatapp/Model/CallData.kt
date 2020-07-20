package com.basscolor.chatapp.Model

import io.skyway.Peer.MediaConnection
import io.skyway.Peer.Peer
import java.io.Serializable

class CallData: Serializable{

    var peer: Peer? = null
    var peerUserID:String? = null
    var mediaConnection: MediaConnection? = null
}
