package com.basscolor.chatapp.Controller

import android.app.Activity
import android.content.ContentValues.TAG
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.content.ContextCompat
import com.basscolor.chatapp.Chatroom
import com.basscolor.chatapp.FireBase.MessageDatabase
import com.basscolor.chatapp.Listener.ChatroomActivityListener
import com.basscolor.chatapp.R
import com.github.bassaer.chatmessageview.model.ChatUser
import com.github.bassaer.chatmessageview.model.Message
import com.github.bassaer.chatmessageview.view.MessageView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatroomActivityController(override val activity: Activity, override val chatroom: Chatroom) : ChatroomActivityListener {

    private lateinit var chatView: MessageView
    private lateinit var me: ChatUser
    private lateinit var you: ChatUser
    private lateinit var messageDatabase:MessageDatabase

    private var count = 0

    init {

        messageDatabase = MessageDatabase()
        messageDatabase.receiveMessage(chatroom.document) { snapshot ->
            receive(snapshot)
        }
        chatUserSetting()
        chatViewSetting()
    }

    private fun chatUserSetting(){
        val myIcon = BitmapFactory.decodeResource(activity.resources,
            R.drawable.ic_user
        )
        val yourIcon = BitmapFactory.decodeResource(activity.resources,
            R.drawable.ic_user
        )

        val currentUser = FirebaseAuth.getInstance().currentUser!!
        val userNames = chatroom.document["userNames"] as ArrayList<String>
        if(userNames[0] == currentUser.displayName){
            me = ChatUser(0, userNames[0], myIcon)
            you = ChatUser(1,userNames[1] , yourIcon)
        }else{
            you = ChatUser(0, userNames[0], myIcon)
            me = ChatUser(1,userNames[1] , yourIcon)
        }
    }

    private fun chatViewSetting(){
        chatView = activity.findViewById(R.id.message_view)
        chatView.setRightBubbleColor(ContextCompat.getColor(activity,
            R.color.colorPrimary
        ))
        chatView.setLeftBubbleColor(ContextCompat.getColor(activity,
            R.color.gray200
        ))
        chatView.setBackgroundColor(ContextCompat.getColor(activity,
            R.color.blueGray200
        ))
        chatView.setRightMessageTextColor(ContextCompat.getColor(activity,
            R.color.messageText_black
        ))
        chatView.setLeftMessageTextColor(ContextCompat.getColor(activity,
            R.color.messageText_black
        ))
        chatView.setSendTimeTextColor(ContextCompat.getColor(activity,
            R.color.timeText_white
        ))
    }

    override fun onInputMessage(message: String) {

    }

    override fun toSpeak(message: String) {
        if(message == "")return
        messageDatabase.sendMessage(chatroom.document,message,
            {s->
                Log.e(TAG, s)

            },{e->
                Log.e(TAG, "メッセージの送信に失敗しました", e)
            })
    }

    private fun receive(snapshots: QuerySnapshot) {

        if(count == 0){

            for (s in snapshots){

                val sdf = SimpleDateFormat("MM-dd HH:mm")
                val d = s["date"] as Timestamp
                val timeStamp = sdf.format(d.toDate())
                val userID = s["userID"] as String

                if(userID == FirebaseAuth.getInstance().currentUser!!.uid){
                    rightBalloonDisplay(s)
                }else{
                    leftBalloonDisplay(s)
                }

            }
            count = snapshots.count()
            chatView.scrollToEnd()
            return
        }
        if(count < snapshots.count()){

            val snapshot =snapshots.documents[snapshots.count()-1]
            val sdf = SimpleDateFormat("MM-dd HH:mm")
            val d = snapshot["date"] as Timestamp
            val timeStamp = sdf.format(d.toDate())
            val userID = snapshot["userID"] as String

            if(userID == FirebaseAuth.getInstance().currentUser!!.uid){
                rightBalloonDisplay(snapshot)
            }else{
                leftBalloonDisplay(snapshot)
            }
            count = snapshots.count()
        }
        chatView.scrollToEnd()
    }

    private fun rightBalloonDisplay(snapshot:DocumentSnapshot){

        val date = snapshot["date"] as Timestamp
        val calendar = Calendar.getInstance()
        calendar.time = date.toDate()
        val message = Message.Builder()
            .setUser(me)
            .setUsernameVisibility(false)
            .setRight(true)
            .setText(snapshot["message"] as String)
            .hideIcon(true)
            .setSendTime(calendar)
            .build()
        chatView.setMessage(message)
    }

    private fun leftBalloonDisplay(snapshot:DocumentSnapshot){

        val date = snapshot["date"] as Timestamp
        val calendar = Calendar.getInstance()
        calendar.time = date.toDate()

        val receivedMessage = Message.Builder()
            .setUser(you)
            .setUsernameVisibility(false)
            .setRight(false)
            .setText(snapshot["message"] as String)
            .hideIcon(false)
            .setSendTime(calendar)
            .build()
        chatView.setMessage(receivedMessage)
    }

}