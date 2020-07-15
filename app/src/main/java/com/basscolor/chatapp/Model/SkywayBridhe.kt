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
import io.skyway.Peer.CallOption
import io.skyway.Peer.MediaConnection
import io.skyway.Peer.Peer
import io.skyway.Peer.PeerOption
import org.json.JSONArray
import java.lang.NullPointerException




class SkywayBridhe(val activity: Activity, val localStreamView:Canvas, val remoteStreamView:Canvas, val peerUserID: String ){

    companion object {
        val API_KEY = "920114e2-7d0f-4dc9-bc37-421e04b651fb"
        val DOMAIN  = "com.basscolor.chatapp"
    }

    private lateinit var peer:Peer
    private lateinit var currentPeerID : String
    private lateinit var localStream:MediaStream
    private lateinit var remoteStream:MediaStream
    private lateinit var mediaConnection:MediaConnection

    fun call(success:(String)->Unit,notFind:(String)->Unit,failure: (Exception) -> Unit){
        getPeerIDs({list->
            for(id in list){
                val ids = peerUserID
                if(id == ids){

                    mediaConnection = peer.call(id, localStream)
                    setupMediaCallBack()
                    success("接続を開始します")
                    return@getPeerIDs
                }
            }
            notFind("接続先が見つかりませんでした")
        },{e->
            failure(e)
        })
    }

    fun setReceiver(receive:(String)->Unit){
        peer.on(Peer.PeerEventEnum.CALL) { p0 ->
            (p0 as? MediaConnection)?.let{it->
                mediaConnection = it
                receive("電話を受信しました")
            }
        }
    }

    fun answer(){
        setupMediaCallBack()
        mediaConnection.answer(localStream)//相手に映像を送信
    }

    fun hangUp(delegate:()->Unit){
        mediaConnection.close()
        delegate()
    }

    fun destroy() {
        localStream.removeVideoRenderer(localStreamView,0)
        localStream.close()
        remoteStream.removeVideoRenderer(remoteStreamView,0)
        remoteStream.close()
        if(mediaConnection.isOpen)mediaConnection.close()
    }

    fun checkPermission(){
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity,
                Manifest.permission.RECORD_AUDIO) !== PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), 0)
        } else {
            this.setupPeer()
        }
    }

    fun setupPeer(){//パーミッションの許可が得られた後に行う初期設定
        val option = PeerOption()
        option.key = API_KEY
        option.domain = DOMAIN
        option.debug = Peer.DebugLevelEnum.ALL_LOGS
        peer = Peer(activity,FirebaseAuth.getInstance().currentUser!!.uid, option)
        setupPeerCallBack()
    }

    private fun setupPeerCallBack(){

        peer.on(Peer.PeerEventEnum.OPEN) { p0 ->//自分の画面を出す
            (p0 as? String)?.let{ it ->
                Log.d("debug", "peerID: $it")
                currentPeerID = it
                startLocalStream()
            }
        }
        peer.on(Peer.PeerEventEnum.ERROR
        ) { p0 -> Log.d("debug", "peer error $p0") }

        peer.on(Peer.PeerEventEnum.CLOSE){p0->
            (p0 as? MediaConnection)?.let{
                mediaConnection.close()
            }
        }

    }

    private fun setupMediaCallBack(){//相手の映像を表示
        mediaConnection.on(MediaConnection.MediaEventEnum.STREAM) { p0 ->
            (p0 as? MediaStream)?.let{it->
                remoteStream = it
                remoteStream.addVideoRenderer(remoteStreamView, 0)
                Log.d("debug", "相手の映像を受信しました")
            }
        }
    }

    private fun startLocalStream(){//自分の映像を表示
        val constraints = MediaConstraints()
        constraints.maxWidth = 960
        constraints.maxHeight = 540
        constraints.cameraPosition = MediaConstraints.CameraPositionEnum.FRONT
        Navigator.initialize(peer)
        localStream = Navigator.getUserMedia(constraints)
        localStream.addVideoRenderer(localStreamView, 0)
    }



    private fun getPeerIDs(found:(ArrayList<String>)->Unit,failure:(Exception)->Unit){

        peer.listAllPeers {p->

            if (p !is JSONArray) {
                failure(NullPointerException())
                return@listAllPeers
            }

            val listPeerIds = arrayListOf<String>()
            var peerId: String

            Log.d(TAG, p.length().toString()+"peersの数")
            for (i in 0 until p.length()){
                try {
                    peerId = p.getString(i)
                    if (!currentPeerID.equals(peerId)) {
                        listPeerIds.add(peerId)
                    }
                } catch (e: Exception) {
                    failure(e)
                }
            }
            found(listPeerIds)
        }
    }





}
