package com.basscolor.chatapp.Controller

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.basscolor.chatapp.Activity.VideocallActivity
import com.basscolor.chatapp.Deta.Chatroom
import com.basscolor.chatapp.Model.CustomMessageView
import com.basscolor.chatapp.Model.FireBase.MessageDatabase
import com.basscolor.chatapp.Listener.ChatroomActivityListener
import com.basscolor.chatapp.Model.CallData
import com.basscolor.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot
import io.skyway.Peer.MediaConnection
import io.skyway.Peer.Peer
import io.skyway.Peer.PeerOption

class ChatroomActivityController(override val activity: Activity, override val chatroom: Chatroom) : ChatroomActivityListener {

    companion object {
        val API_KEY = "920114e2-7d0f-4dc9-bc37-421e04b651fb"
        val DOMAIN  = "com.basscolor.chatapp"
    }

    private var chatView: CustomMessageView
    private var messageDatabase:MessageDatabase = MessageDatabase()
    private val callData = CallData()
    private val incomingView : ConstraintLayout

    private var count = 0

    init {
        messageDatabase.receiveMessage(chatroom) { snapshot -> receive(snapshot)}
        incomingView = activity.findViewById(R.id.IncomingView)

        chatView = CustomMessageView(
            activity.findViewById(R.id.message_view),
            activity,
            chatroom
        )
        checkPermission()
    }

    override fun onInputMessage(message: String) {}

    override fun toSpeak(message: String) {
        if(message == "")return
        messageDatabase.sendMessage(chatroom,message, {s-> Log.e(TAG, s) },{e-> Log.e(TAG, "メッセージの送信に失敗しました", e) })
    }

    override fun toCall() {
        callData.peerUserID = chatroom.getPeerUserID()
        transition()
    }

    override fun toReject() {
        incomingView.visibility = View.INVISIBLE
    }

    private fun receive(snapshots: QuerySnapshot) {

        if(count == 0){
            for (s in snapshots){
                chatView.createBalloon(s)
            }
            count = snapshots.count()
            chatView.messageView.scrollToEnd()
            return
        }
        if(count < snapshots.count()){

             chatView.createBalloon(snapshots.documents[snapshots.count()-1])
            count = snapshots.count()
        }
        chatView.messageView.scrollToEnd()
    }

    private fun checkPermission(){
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity,
                Manifest.permission.RECORD_AUDIO) !== PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), 0)
        } else {
            setupPeer()
        }
    }

    override fun setupPeer(){//パーミッションの許可が得られた後に行う初期設定
        val option = PeerOption()
        option.key = API_KEY
        option.domain = DOMAIN
        option.debug = Peer.DebugLevelEnum.ALL_LOGS
        callData.peer = Peer(activity, FirebaseAuth.getInstance().currentUser!!.uid, option)
        connectServer()
        setIncoming()
    }

    private fun connectServer(){

        callData.peer?.on(Peer.PeerEventEnum.OPEN) { p0 ->
            (p0 as? String)?.let{ it ->
                Log.d(TAG, "サーバに接続しました: $it")
            }
        }
        callData.peer?.on(Peer.PeerEventEnum.ERROR
        ) { p0 -> Log.d(TAG, "サーバ接続中にエラーが発生しました: $p0") }
    }

    private fun setIncoming(){//着信があった時の処理設定
        callData.peer?.on(Peer.PeerEventEnum.CALL) { p0 ->
            (p0 as? MediaConnection)?.let{it->
                callData.mediaConnection = it
                incomingView.visibility = View.VISIBLE
            }
        }
    }

    override fun transition(){
        val intent = Intent(activity, VideocallActivity::class.java)
        intent.putExtra("callData",callData)
        activity.startActivity(intent)
    }



    override fun toDestroy(){

        if(callData.peer != null){
            if(!callData.peer!!.isDisconnected){
                callData.peer!!.disconnect()
            }
            if(!callData.peer!!.isDestroyed){
                callData.peer!!.destroy()
            }
            unsetPeerCallback(callData.peer!!)
        }
    }

    fun unsetPeerCallback(peer: Peer) {

        peer.on(Peer.PeerEventEnum.OPEN, null)
        peer.on(Peer.PeerEventEnum.CONNECTION, null)
        peer.on(Peer.PeerEventEnum.CALL, null)
        peer.on(Peer.PeerEventEnum.CLOSE, null)
        peer.on(Peer.PeerEventEnum.DISCONNECTED, null)
        peer.on(Peer.PeerEventEnum.ERROR, null)
    }


}