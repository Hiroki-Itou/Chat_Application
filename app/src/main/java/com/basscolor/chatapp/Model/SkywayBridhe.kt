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
import org.json.JSONArray


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
        getPeerID(peer!!,{ id->
            peerID = id
            dataConnection = peer?.connect(peerID)
            setuoDataConnectionCallBack(dataConnection!!)
            Log.d(TAG,"接続確認を開始します")
        },{
            val msg = "接続先が見つかりませんでした"
            Log.d(TAG,msg)
            AlertDialog.Builder(activity).setTitle("Error")
                .setMessage(msg)
                .setPositiveButton("OK") { _ , _ ->
                    toPreviousView()
                }.setCancelable(false).show()
        })
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

    private fun setuoDataConnectionCallBack(dataConnection: DataConnection){
        
        dataConnection.on(DataConnection.DataEventEnum.DATA){

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

    private fun getPeerID(peer: Peer,found:(String)->Unit,failure:()->Unit){

        peer.listAllPeers {p->
            if (p !is JSONArray) {
                return@listAllPeers
            }
            Log.d(TAG, p.length().toString()+"peersの数")
            for (i in 0 until p.length()){
                try {
                    val peerId = p.getString(i)
                    if (CallData.peerUserID == peerId) {
                        found(peerId)
                        return@listAllPeers
                    }
                } catch (e: Exception) {
                    Log.e(TAG,"getPeerID内でエラーが発生")
                }
            }
            failure()
        }
    }
//--------------------------destroy-------------------------//

    private fun destroyStream() {
        closeRemoteStream()
        closeLocalStream()
        Navigator.terminate()
        if(dataConnection != null){
            if(dataConnection!!.isOpen){
                dataConnection!!.close()
            }
            unsetDataCallbacks(dataConnection!!)
            dataConnection = null
        }
        if(mediaConnection != null){
            if (mediaConnection!!.isOpen){
                mediaConnection!!.close()
            }
            unsetMediaCallbacks(mediaConnection!!)
            mediaConnection = null
        }
    }

    private fun closeLocalStream(){
        if(localStream == null)return
        localStream!!.removeVideoRenderer(remoteStreamView,0)
        localStream!!.close()
        Log.d(TAG,"localStreamの解放が完了しました")
    }

    private fun closeRemoteStream(){
        if(remoteStream == null)return
        remoteStream!!.removeVideoRenderer(remoteStreamView,0)
        remoteStream!!.close()
        Log.d(TAG,"remoteStreamの解放が完了しました")
    }

    private fun unsetMediaCallbacks(mediaConnection: MediaConnection){

        mediaConnection.on(MediaConnection.MediaEventEnum.REMOVE_STREAM,null)
        mediaConnection.on(MediaConnection.MediaEventEnum.STREAM, null)
        mediaConnection.on(MediaConnection.MediaEventEnum.CLOSE, null)
        mediaConnection.on(MediaConnection.MediaEventEnum.ERROR, null)
    }

    private fun unsetDataCallbacks(dataConnection: DataConnection){

        dataConnection.on(DataConnection.DataEventEnum.OPEN, null)
        dataConnection.on(DataConnection.DataEventEnum.DATA,null)
        dataConnection.on(DataConnection.DataEventEnum.CLOSE, null)
        dataConnection.on(DataConnection.DataEventEnum.ERROR, null)
    }

}
