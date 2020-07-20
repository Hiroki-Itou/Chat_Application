package com.basscolor.chatapp.Model

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import io.skyway.Peer.Browser.Canvas
import io.skyway.Peer.Browser.MediaConstraints
import io.skyway.Peer.Browser.MediaStream
import io.skyway.Peer.Browser.Navigator
import io.skyway.Peer.MediaConnection
import io.skyway.Peer.Peer
import org.json.JSONArray
import java.lang.NullPointerException


class SkywayBridhe(val activity: Activity, val localStreamView:Canvas, val remoteStreamView:Canvas, val callData: CallData ){

    private var peer:Peer? = null
    private lateinit var currentPeerID : String
    private var localStream:MediaStream? = null
    private var remoteStream:MediaStream? = null
    private var mediaConnection:MediaConnection? = null

    init {
        peer = callData.peer
        setupPeerCallBack()
        startLocalStream()
        if(callData.mediaConnection == null){
            call()
        }else{
            mediaConnection = callData.mediaConnection
            setupMediaCallBack()
            mediaConnection?.answer(localStream)//相手に映像を送信
        }
    }


    private fun call(){
        getPeerIDs({list->
            for(id in list){
                if(id == callData.peerUserID){
                    mediaConnection = peer?.call(id, localStream)//相手に映像を送信
                    setupMediaCallBack()
                    Log.d(TAG,"接続を開始します")
                    return@getPeerIDs
                }
            }
            Log.d(TAG,"接続先が見つかりませんでした")
        },{e->
            Log.e(TAG,"$this でエラーが発生: $e")
        })
    }
    fun destroy(){
        destroyStream()
    }

    private fun setupPeerCallBack(){

        peer?.on(Peer.PeerEventEnum.CLOSE){p0->
            (p0 as? MediaConnection)?.let{
                activity.finish()
            }
        }
    }
    private fun setupMediaCallBack(){//相手の映像を表示
        mediaConnection?.on(MediaConnection.MediaEventEnum.STREAM) { p0 ->
            (p0 as? MediaStream)?.let{it->
                remoteStream = it
                remoteStream!!.addVideoRenderer(remoteStreamView, 0)
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
        localStream!!.addVideoRenderer(localStreamView, 0)
    }

    private fun getPeerIDs(found:(ArrayList<String>)->Unit,failure:(Exception)->Unit){

        peer?.listAllPeers {p->

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
//--------------------------destroy-------------------------//

    private fun destroyStream() {
        closeRemoteStream()
        closeLocalStream()
        if(callData.mediaConnection != null){
            if (callData.mediaConnection!!.isOpen){
                callData.mediaConnection!!.close()
            }
            unsetMediaCallbacks(callData.mediaConnection!!)
            callData.mediaConnection = null
        }
    }

    private fun closeLocalStream(){
        if(localStream == null)return
        localStream!!.removeVideoRenderer(remoteStreamView,0)
        localStream!!.close()
    }

    private fun closeRemoteStream(){
        if(remoteStream == null)return
        remoteStream!!.removeVideoRenderer(remoteStreamView,0)
        remoteStream!!.close()
    }

    private fun unsetMediaCallbacks(mediaConnection: MediaConnection){

        mediaConnection.on(MediaConnection.MediaEventEnum.STREAM, null)
        mediaConnection.on(MediaConnection.MediaEventEnum.CLOSE, null)
        mediaConnection.on(MediaConnection.MediaEventEnum.ERROR, null)
    }

}
