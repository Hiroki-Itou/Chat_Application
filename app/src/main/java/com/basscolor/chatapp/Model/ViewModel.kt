package com.basscolor.chatapp.Model


import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.basscolor.chatapp.Deta.Chatroom
import com.google.firebase.auth.FirebaseAuth
import io.skyway.Peer.Browser.Canvas
import io.skyway.Peer.Browser.MediaConstraints
import io.skyway.Peer.Browser.MediaStream
import io.skyway.Peer.Browser.Navigator
import io.skyway.Peer.MediaConnection
import io.skyway.Peer.Peer
import io.skyway.Peer.PeerOption
import org.json.JSONArray
import java.lang.NullPointerException


class ViewModel(val activity: Activity, val localStreamView:Canvas, val remoteStreamView:Canvas,val chatroom: Chatroom ){

    companion object {
        val API_KEY = "920114e2-7d0f-4dc9-bc37-421e04b651fb"
        val DOMAIN  = "com.basscolor.chatapp"
    }

    private var peer:Peer? = null
    private var currentPeerID : String? = null
    private var localStream:MediaStream? = null
    private var mediaConnection:MediaConnection? = null

    fun checkPermission(){
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity,
                Manifest.permission.RECORD_AUDIO) !== PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), 0)
        } else {
            this.setupPeer()
        }
    }

    fun setupPeer(){
        val option = PeerOption()
        option.key = API_KEY
        option.domain = DOMAIN
        option.debug = Peer.DebugLevelEnum.ALL_LOGS
        this.peer = Peer(activity,FirebaseAuth.getInstance().currentUser!!.uid, option)
        this.setupPeerCallBack()
    }

    fun toCall(){
        getPeerIDs({list->
            for(id in list){
                val ids = chatroom.getPeerUserID()
                if(id == ids){
                    peer!!.call(id, localStream)
                    return@getPeerIDs
                }
            }
            Log.e(TAG,"接続に失敗しました")
        },{e->
            Log.e(TAG,"接続先PeerIDListの取得に失敗しました$e")
        })

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

    private fun setupPeerCallBack(){
        this.peer?.on(Peer.PeerEventEnum.OPEN) { p0 ->//自分の画面を出す
            (p0 as? String)?.let{ peerID ->
                Log.d("debug", "peerID: $peerID")
                currentPeerID = peerID
                startLocalStream()
            }
        }
        this.peer?.on(Peer.PeerEventEnum.ERROR
        ) { p0 -> Log.d("debug", "peer error $p0") }
        this.peer?.on(Peer.PeerEventEnum.CALL) { p0 ->//かかってきた時
            (p0 as? MediaConnection)?.let{
                this@ViewModel.mediaConnection = it
                this@ViewModel.setupMediaCallBack()
                this@ViewModel.mediaConnection?.answer(localStream)
            }
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

    private fun setupMediaCallBack(){
        mediaConnection?.on(MediaConnection.MediaEventEnum.STREAM) { p0 ->
            (p0 as? MediaStream)?.addVideoRenderer(remoteStreamView, 0)
        }
    }
}
