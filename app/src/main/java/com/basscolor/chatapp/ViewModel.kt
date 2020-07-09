package com.basscolor.chatapp


import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.basscolor.chatapp.Activity.VideocallActivity
import io.skyway.Peer.Browser.Canvas
import io.skyway.Peer.Browser.MediaConstraints
import io.skyway.Peer.Browser.MediaStream
import io.skyway.Peer.Browser.Navigator
import io.skyway.Peer.MediaConnection
import io.skyway.Peer.OnCallback
import io.skyway.Peer.Peer
import io.skyway.Peer.PeerOption
import org.json.JSONArray
import java.lang.NullPointerException
import io.skyway.Peer.CallOption




class ViewModel(val activity: VideocallActivity, val localStreamView:Canvas, val remoteStreamView:Canvas){
    companion object {
        val API_KEY = "920114e2-7d0f-4dc9-bc37-421e04b651fb"
        val DOMAIN  = "com.basscolor.chatapp"
    }

    var peer:Peer? = null
    var currentPeerID : String? = null
    var remoteStream:MediaStream? = null
    var localStream:MediaStream? = null
    var mediaConnection:MediaConnection? = null

    fun setup(){
        checkPermission()
    }


    private fun getPeerIDs(found:(ArrayList<String>)->Unit,failure:(Exception)->Unit){

        if (null == peer|| null == currentPeerID){
            Toast.makeText(activity, "Your PeerID is null or invalid.", Toast.LENGTH_SHORT).show()
            failure(NullPointerException())
            return
        }

        peer!!.listAllPeers {p->

            if (p !is JSONArray) {
                failure(NullPointerException())
                return@listAllPeers
            }

            val peers = p as JSONArray
            val listPeerIds = arrayListOf<String>()
            var peerId: String

            for (i in 0 until peers.length()){
                try {
                    peerId = peers.getString(i)
                    if (!currentPeerID.equals(peerId)) {
                        listPeerIds.add(peerId)
                    }
                } catch (e: Exception) {
                    failure(e)
                }
            }
            found(listPeerIds)
        }

//            OnCallback { p->
//
//        }

    }

    fun toCall(){
        getPeerIDs({list->

            var partnerID:String? = null

            for(i in list){
                if(i != currentPeerID){
                    partnerID = i
                }
            }

            if(partnerID == null){
                Log.e(TAG,"partnerIDがnullです")
                return@getPeerIDs
            }

            peer!!.call(partnerID, localStream)

        },{e->
            Log.e(TAG,"接続先PeerIDListの取得に失敗しました$e")
        })

    }

    fun setupPeer(){
        val option = PeerOption()
        option.key = API_KEY
        option.domain = DOMAIN
        option.debug = Peer.DebugLevelEnum.ALL_LOGS
        this.peer = Peer(activity, option)

        this.setupPeerCallBack()
    }

    private fun setupPeerCallBack(){
        this.peer?.on(Peer.PeerEventEnum.OPEN) { p0 ->
            (p0 as? String)?.let{ peerID ->
                Log.d("debug", "peerID: $peerID")
                currentPeerID = peerID
                startLocalStream()
            }
        }
        this.peer?.on(Peer.PeerEventEnum.ERROR
        ) { p0 -> Log.d("debug", "peer error $p0") }
        this.peer?.on(Peer.PeerEventEnum.CALL) { p0 ->
            (p0 as? MediaConnection)?.let{
                this@ViewModel.mediaConnection = it
                this@ViewModel.setupMediaCallBack()
                this@ViewModel.mediaConnection?.answer(localStream)
            }
        }

    }

    private fun checkPermission(){
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity,
                Manifest.permission.RECORD_AUDIO) !== PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), 0)
        } else {
            this.setupPeer()
        }
    }

    private fun startLocalStream(){
        val constraints = MediaConstraints()
        constraints.maxWidth = 960
        constraints.maxHeight = 540
        constraints.cameraPosition = MediaConstraints.CameraPositionEnum.FRONT
        Navigator.initialize(peer)
        localStream = Navigator.getUserMedia(constraints)
        localStream?.addVideoRenderer(localStreamView, 0)
    }

    fun setupMediaCallBack(){
        mediaConnection?.on(MediaConnection.MediaEventEnum.STREAM) { p0 ->
            (p0 as? MediaStream)?.let{
                this@ViewModel.remoteStream = it
                this@ViewModel.remoteStream?.addVideoRenderer(remoteStreamView, 0)
            }
        }
    }
}
