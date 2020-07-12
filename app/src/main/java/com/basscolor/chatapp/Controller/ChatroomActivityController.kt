package com.basscolor.chatapp.Controller

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import com.basscolor.chatapp.Deta.Chatroom
import com.basscolor.chatapp.Model.CustomMessageView
import com.basscolor.chatapp.Model.FireBase.MessageDatabase
import com.basscolor.chatapp.Listener.ChatroomActivityListener
import com.basscolor.chatapp.R
import com.google.firebase.firestore.QuerySnapshot

class ChatroomActivityController(override val activity: Activity, override val chatroom: Chatroom) : ChatroomActivityListener {

    private lateinit var chatView: CustomMessageView
    private lateinit var messageDatabase:MessageDatabase

    private var count = 0

    init {
        messageDatabase = MessageDatabase()
        messageDatabase.receiveMessage(chatroom) { snapshot -> receive(snapshot)}

        chatView = CustomMessageView(
            activity.findViewById(R.id.message_view),
            activity,
            chatroom
        )
    }

    override fun onInputMessage(message: String) {}

    override fun toSpeak(message: String) {
        if(message == "")return
        messageDatabase.sendMessage(chatroom,message, {s-> Log.e(TAG, s) },{e-> Log.e(TAG, "メッセージの送信に失敗しました", e) })
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

    
}