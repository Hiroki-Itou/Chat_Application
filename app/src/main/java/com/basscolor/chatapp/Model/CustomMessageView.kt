package com.basscolor.chatapp.Model

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.content.ContextCompat
import com.basscolor.chatapp.Deta.Chatroom
import com.basscolor.chatapp.R
import com.github.bassaer.chatmessageview.model.ChatUser
import com.github.bassaer.chatmessageview.model.Message
import com.github.bassaer.chatmessageview.view.MessageView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CustomMessageView (view: MessageView,val activity:Activity,val chatroom: Chatroom) {

    var messageView : MessageView = view
    private lateinit var me: ChatUser
    private lateinit var you: ChatUser
    private lateinit var currentUser : FirebaseUser
    private val icon :Bitmap = BitmapFactory.decodeResource(activity.resources,
        R.mipmap.ic_user
    )

    init {

        messageView.setRightBubbleColor(ContextCompat.getColor(activity,
            R.color.colorPrimary
        ))
        messageView.setLeftBubbleColor(ContextCompat.getColor(activity,
            R.color.gray200
        ))
        messageView.setBackgroundColor(ContextCompat.getColor(activity,
            R.color.blueGray200
        ))
        messageView.setRightMessageTextColor(ContextCompat.getColor(activity,
            R.color.messageText_black
        ))
        messageView.setLeftMessageTextColor(ContextCompat.getColor(activity,
            R.color.messageText_black
        ))
        messageView.setSendTimeTextColor(ContextCompat.getColor(activity,
            R.color.timeText_white
        ))
        chatUserSetting()
    }

    private fun chatUserSetting(){

        currentUser = FirebaseAuth.getInstance().currentUser!!
        val userNames = chatroom.document["userNames"] as ArrayList<String>
        if(userNames[0] == currentUser.displayName){
            me = ChatUser(0, userNames[0], icon)
            you = ChatUser(1,userNames[1] , icon)
        }else{
            you = ChatUser(0, userNames[0], icon)
            me = ChatUser(1,userNames[1] , icon)
        }
    }


    @Synchronized
    fun createBalloon(snapshot: DocumentSnapshot){
        val sdf = SimpleDateFormat("MM-dd HH:mm")
        val d = snapshot["date"] as Timestamp
        val timeStamp = sdf.format(d.toDate())
        val userID = snapshot["userID"] as String

        if(userID == currentUser.uid){
            rightBalloonDisplay(snapshot)
        }else{
            leftBalloonDisplay(snapshot)
        }
    }


    private fun rightBalloonDisplay(snapshot: DocumentSnapshot){

        balloonDisplay(snapshot,me,true, hideIcon = true)
    }

    private fun leftBalloonDisplay(snapshot:DocumentSnapshot){

        balloonDisplay(snapshot,you, setRight = false, hideIcon = false)
    }

    private fun balloonDisplay(snapshot:DocumentSnapshot,user:ChatUser,setRight:Boolean,hideIcon:Boolean){

        val date = snapshot["date"] as Timestamp
        val calendar = Calendar.getInstance()
        calendar.time = date.toDate()
        val message = Message.Builder()
            .setUser(user)
            .setUsernameVisibility(false)
            .setRight(setRight)
            .setText(snapshot["message"] as String)
            .hideIcon(hideIcon)
            .setSendTime(calendar)
            .build()
        messageView.setMessage(message)

    }
}
