package com.basscolor.chatapp.Model

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.util.Log
import com.basscolor.chatapp.Deta.CallData
import com.google.firebase.auth.FirebaseAuth
import io.skyway.Peer.Browser.Canvas
import io.skyway.Peer.Browser.MediaConstraints
import io.skyway.Peer.Browser.MediaStream
import io.skyway.Peer.Browser.Navigator
import io.skyway.Peer.DataConnection
import io.skyway.Peer.MediaConnection
import io.skyway.Peer.Peer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class SkywayBridhe(val activity: Activity, private val localStreamView:Canvas, private val remoteStreamView:Canvas){

    private var peer:Peer? = null
    private val currentPeerID : String
    private var peerID : String? = null
    private var localStream:MediaStream? = null
    private var remoteStream:MediaStream? = null
    private var mediaConnection:MediaConnection? = null
    private var dataConnection :DataConnection? = null

    init {
        peer = CallData.peer
        currentPeerID = FirebaseAuth.getInstance().currentUser!!.uid
        setupPeerCallBack()
        startLocalStream()
    }

    fun call(){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                peerID = withContext(Dispatchers.IO){ getPeerID(peer!!) }
                if(peerID != null){
                    dataConnection = peer!!.connect(peerID)
                    dataConnection!!.setupDataConnectionCallBack()
                    Log.d(TAG,"接続先へ接続中です")
                }else{
                    val msg = "接続先が見つかりませんでした"
                    Log.d(TAG,msg)
                    AlertDialog.Builder(activity).setTitle("Error")
                        .setMessage(msg)
                        .setPositiveButton("OK") { _ , _ ->
                            toPreviousView()
                        }.setCancelable(false).show()
                }

            }catch (e:Exception){
                val msg = "相手の接続を確認中にエラーが発生しました"
                Log.e(TAG, "$msg:$e")
                AlertDialog.Builder(activity).setTitle("Error")
                    .setMessage(msg)
                    .setPositiveButton("OK") { _ , _ ->
                        toPreviousView()
                    }.setCancelable(false).show()
            }
        }
    }

    fun hangUp(){
        toPreviousView()
    }

    private fun toPreviousView(){
        destroyStream()
        activity.finish()
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun connect(id:String){
        mediaConnection = peer?.call(id, localStream)//相手に映像を送信
        setupMediaConnectionCallBack(mediaConnection!!)
        Log.d(TAG,"接続を開始します")
    }
    
    private fun setupPeerCallBack(){

        Log.d(TAG, "Peerのセットアップを開始します")

        peer?.on(Peer.PeerEventEnum.CALL) { p0 ->//電話を受信したとき
            (p0 as? MediaConnection)?.let{it->
                mediaConnection = it
                mediaConnection?.answer(localStream)//相手に映像を送信
                setupMediaConnectionCallBack(mediaConnection!!)
            }
        }
        peer?.on(Peer.PeerEventEnum.CLOSE){p0->
            (p0 as? MediaConnection)?.let{
                toPreviousView()
            }
        }
    }

    private fun DataConnection.setupDataConnectionCallBack(){
        
        this.on(DataConnection.DataEventEnum.DATA){

            if(it == "REPLY"){
                connect(peerID!!)
            }else {
                val msg = "接続が切断されました"
                AlertDialog.Builder(activity).setTitle("接続失敗")
                    .setMessage(msg)
                    .setPositiveButton("OK") { _ , _ ->
                        toPreviousView()
                    }.setCancelable(false).show()
            }
        }
    }

    private fun setupMediaConnectionCallBack(mediaConnection:MediaConnection){//相手の映像を表示
        mediaConnection.on(MediaConnection.MediaEventEnum.STREAM) { p0 ->
            (p0 as? MediaStream)?.let{it->
                remoteStream = it
                remoteStream!!.addVideoRenderer(remoteStreamView, 0)

                Log.d("debug", "相手の映像を受信しました")
            }
        }
        mediaConnection.on(MediaConnection.MediaEventEnum.CLOSE){
            toPreviousView() }
        mediaConnection.on(MediaConnection.MediaEventEnum.REMOVE_STREAM){
            toPreviousView()}
        mediaConnection.on(MediaConnection.MediaEventEnum.ERROR){
            toPreviousView()
        }
    }

    private fun startLocalStream(){//自分の映像を表示
        val constraints = MediaConstraints()
        constraints.maxWidth = 960
        constraints.maxHeight = 540
        constraints.cameraPosition = MediaConstraints.CameraPositionEnum.FRONT
        Navigator.initialize(peer)
        localStream = Navigator.getUserMedia(constraints)
        localStream?.addVideoRenderer(localStreamView, 0)
        localStreamView.setZOrderMediaOverlay(true)
    }

    @Synchronized
    private suspend fun getPeerID(peer: Peer) = suspendCoroutine<String?>{ cont->
        peer.listAllPeers{p->
            if (p !is JSONArray) return@listAllPeers
            val no = p.length()
            Log.d(TAG, "現在$no 人がサーバーに接続しています")
            for (i in 0 until p.length()){
                try {
                    val peerId = p.getString(i)
                    if (CallData.peerUserID == peerId) {
                        cont.resume(peerId)
                        return@listAllPeers
                    }
                } catch (e: Exception) {
                    cont.resumeWithException(e)
                    return@listAllPeers
                }
            }
            cont.resume(null)
        }
    }
//--------------------------destroy-------------------------//

    private fun destroyStream() {
        closeRemoteStream()
        closeLocalStream()
        Navigator.terminate()
        unsetPeerCallback(peer)
        if(dataConnection != null){
            if(dataConnection!!.isOpen){
                dataConnection!!.close()
            }
            dataConnection!!.unsetDataCallbacks()
            dataConnection = null
        }
        if(mediaConnection != null){
            if (mediaConnection!!.isOpen){
                mediaConnection!!.close()
            }
            mediaConnection!!.unsetMediaCallbacks()
            mediaConnection = null
        }
    }

    private fun closeLocalStream(){
        localStream?:return
        localStream!!.removeVideoRenderer(remoteStreamView,0)
        localStream!!.close()
        Log.d(TAG,"localStreamの解放が完了しました")
    }

    private fun closeRemoteStream(){
        remoteStream?:return
        remoteStream!!.removeVideoRenderer(remoteStreamView,0)
        remoteStream!!.close()
        Log.d(TAG,"remoteStreamの解放が完了しました")
    }

    private fun MediaConnection.unsetMediaCallbacks(){
        this.on(MediaConnection.MediaEventEnum.REMOVE_STREAM,null)
        this.on(MediaConnection.MediaEventEnum.STREAM, null)
        this.on(MediaConnection.MediaEventEnum.CLOSE, null)
        this.on(MediaConnection.MediaEventEnum.ERROR, null)
    }

    private fun DataConnection.unsetDataCallbacks(){
        this.on(DataConnection.DataEventEnum.OPEN, null)
        this.on(DataConnection.DataEventEnum.DATA,null)
        this.on(DataConnection.DataEventEnum.CLOSE, null)
        this.on(DataConnection.DataEventEnum.ERROR, null)
    }


    private fun unsetPeerCallback(peer: Peer?) {
        peer?.on(Peer.PeerEventEnum.CALL, null)
        peer?.on(Peer.PeerEventEnum.CLOSE, null)
        Log.d(TAG,"peerの一部のイベントの解放が完了しました")
    }

}
