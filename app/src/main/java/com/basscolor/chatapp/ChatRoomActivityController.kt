package com.basscolor.chatapp

import android.app.Activity
import android.content.ContentValues.TAG
import android.graphics.BitmapFactory
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.size
import com.github.bassaer.chatmessageview.model.ChatUser
import com.github.bassaer.chatmessageview.model.Message
import com.github.bassaer.chatmessageview.view.ChatView
import com.github.bassaer.chatmessageview.view.MessageView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatRoomActivityController(override val activity: Activity,override val chatRoom:ChatRoom) :ChatRoomActivityListener ,receiveMessageListener{


    private lateinit var chatView: MessageView
    private lateinit var me: ChatUser
    private lateinit var you: ChatUser

    private var count = 0

    init {

        chatRoom.interfase = this
        chatRoom.receiveMessage()

        chatUserSetting()
        chatViewSetting()
    }

    private fun chatUserSetting(){
        val myIcon = BitmapFactory.decodeResource(activity.resources, R.drawable.icon)
        val yourIcon = BitmapFactory.decodeResource(activity.resources, R.drawable.icon)

        val currentUser = FirebaseAuth.getInstance().currentUser!!
        val userNames = chatRoom.document["userNames"] as ArrayList<String>
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
        chatView.setRightBubbleColor(ContextCompat.getColor(activity,R.color.colorPrimary))
        chatView.setLeftBubbleColor(ContextCompat.getColor(activity,R.color.gray200))
        chatView.setBackgroundColor(ContextCompat.getColor(activity,R.color.blueGray200))
        chatView.setRightMessageTextColor(ContextCompat.getColor(activity,R.color.messageText_black))
        chatView.setLeftMessageTextColor(ContextCompat.getColor(activity,R.color.messageText_black))
        chatView.setSendTimeTextColor(ContextCompat.getColor(activity,R.color.timeText_white))
    }

    override fun onInputMessage(message: String) {

    }

    override fun toSpeak(message: String) {
        if(message == "")return
        chatRoom.sendMessage(message)
    }


    override fun receive(snapshots: QuerySnapshot) {

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