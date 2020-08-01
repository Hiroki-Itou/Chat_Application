package com.basscolor.chatapp.Controller

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import com.basscolor.chatapp.Activity.VideocallActivity
import com.basscolor.chatapp.Deta.Chatroom
import com.basscolor.chatapp.Model.CustomMessageView
import com.basscolor.chatapp.Model.FireBase.MessageDatabase
import com.basscolor.chatapp.Listener.ChatroomActivityListener
import com.basscolor.chatapp.Deta.CallData
import com.basscolor.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot
import io.skyway.Peer.DataConnection
import io.skyway.Peer.Peer
import io.skyway.Peer.PeerOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ChatroomActivityController(override val activity: Activity, override val chatroom: Chatroom) : ChatroomActivityListener {

    private var chatView: CustomMessageView
    private var messageDatabase:MessageDatabase = MessageDatabase()
    private val incomingView : ConstraintLayout
    private val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
    private val ringtone = RingtoneManager.getRingtone(activity, uri)
    private var dataConnection : DataConnection? = null

    private var count = 0

    init {

        messageDatabase.receiveMessage(chatroom) { snapshot -> receive(snapshot)}

        incomingView = activity.findViewById(R.id.IncomingView)
        val nameView = activity.findViewById<TextView>(R.id.nameView)
        nameView.text = chatroom.getPeerUserName()

        chatView = CustomMessageView(
            activity.findViewById(R.id.message_view),
            activity,
            chatroom
        )
    }

    override fun onInputMessage(message: String) {}

    override fun toSpeak(message: String) {
        if(message == "")return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val log = messageDatabase.sendMessage(chatroom,message)
                Log.d(TAG,log)
            }catch (e:Exception){
                Log.e(TAG, "メッセージの送信に失敗しました", e)
            }
        }
    }

    override fun toCall() {
        CallData.peerUserID = chatroom.getPeerUserID()
        transition("CALL")
    }

    override fun toReply() {
        dataConnection?.send("REPLY")
        dataConnection = null
        transition("REPLY")
    }

    override fun toReject() {

        dataConnection?.send("REJECT")
        dataConnection = null


        ringtone.stop()
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

    override fun checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(activity,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(activity,
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), 0)
            } else {
                setupPeer()
            }
        }else{
            setupPeer()
        }
    }

    override fun setupPeer(){//パーミッションの許可が得られた後に行う初期設定
        val option = PeerOption()
        option.key = CallData.API_KEY
        option.domain = CallData.DOMAIN
        option.debug = Peer.DebugLevelEnum.ALL_LOGS
        CallData.peer = Peer(activity, FirebaseAuth.getInstance().currentUser!!.uid, option)
        setupPeerEvent()
    }

    private fun setupPeerEvent(){

        CallData.peer?.on(Peer.PeerEventEnum.OPEN) { p0 ->
            (p0 as? String)?.let{ it ->
                Log.d(TAG, "サーバに接続しました: $it")
            }
        }
        CallData.peer?.on(Peer.PeerEventEnum.ERROR
        ) { p0 -> Log.d(TAG, "サーバ接続中にエラーが発生しました: $p0") }

        CallData.peer?.on(Peer.PeerEventEnum.CONNECTION){ p0->
            (p0 as? DataConnection)?.let{it->
                dataConnection = it
                setuoDataConnectionCallBack(dataConnection!!)
                ringtone.play()
                incomingView.visibility = View.VISIBLE
            }
        }
    }
    private fun setuoDataConnectionCallBack(dataConnection: DataConnection){

        dataConnection.on(DataConnection.DataEventEnum.CLOSE){
            dataConnection.on(DataConnection.DataEventEnum.CLOSE, null)
            this.dataConnection = null
            ringtone.stop()
            incomingView.visibility = View.INVISIBLE
        }
    }

    private fun transition(action:String){
        ringtone.stop()
        incomingView.visibility = View.INVISIBLE
        val intent = Intent(activity, VideocallActivity::class.java)
        intent.putExtra("ACTION",action)
        activity.startActivity(intent)
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun toDestroy(){

        CallData.peerUserID = null
        if(CallData.peer != null){
            if(!CallData.peer!!.isDisconnected){
                CallData.peer!!.disconnect()
            }
            if(!CallData.peer!!.isDestroyed){
                CallData.peer!!.destroy()
            }
            unsetPeerCallback(CallData.peer!!)
            CallData.peer = null
        }
    }

    private fun unsetPeerCallback(peer: Peer) {
        peer.on(Peer.PeerEventEnum.OPEN, null)
        peer.on(Peer.PeerEventEnum.CONNECTION, null)
        peer.on(Peer.PeerEventEnum.CALL, null)
        peer.on(Peer.PeerEventEnum.CLOSE, null)
        peer.on(Peer.PeerEventEnum.DISCONNECTED, null)
        peer.on(Peer.PeerEventEnum.ERROR, null)
    }
}