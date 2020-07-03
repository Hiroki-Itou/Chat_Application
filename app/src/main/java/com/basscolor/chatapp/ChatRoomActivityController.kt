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

class ChatRoomActivityController(override val activity: Activity,override val chatRoom:ChatRoom) :ChatRoomActivityListener ,receiveMessageListener{

    private lateinit var chatView: MessageView
    private lateinit var me: ChatUser
    private lateinit var you: ChatUser

    private var count = 0

    init {

        chatRoom.interfase = this
        chatRoom.receiveMessage()
        var message = ""



        val myID = 0
        val yourID = 1
        val myIcon = BitmapFactory.decodeResource(activity.resources,R.drawable.setuna)
        val yourIcon = BitmapFactory.decodeResource(activity.resources,R.drawable.aoi)
        val myName = "せつな"
        val yourName = "葵"

        me = ChatUser(myID,myName,myIcon)
        you = ChatUser(yourID,yourName,yourIcon)



        chatView = activity.findViewById(R.id.message_view)
        chatView.setRightBubbleColor(ContextCompat.getColor(activity,R.color.green500))
        chatView.setLeftBubbleColor(ContextCompat.getColor(activity,R.color.gray200))
        chatView.setBackgroundColor(ContextCompat.getColor(activity,R.color.blueGray200))


        chatView.setRightMessageTextColor(Color.BLACK)
        chatView.setLeftMessageTextColor(Color.BLACK)
        chatView.setUsernameTextColor(Color.BLACK)
        chatView.setSendTimeTextColor(Color.BLACK)
        chatView.setMessageMarginTop(5)
        chatView.setMessageMarginBottom(5)

        val editText = activity.findViewById<EditText>(R.id.editText)
        val sendButton = activity.findViewById<ImageButton>(R.id.sendButton)

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p: Editable?) {

               message = p.toString()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        sendButton.setOnClickListener {

            if(message == "")return@setOnClickListener

            chatRoom.toSpeak(message)
            editText.text.clear()
        }

    }

    override fun receive(snapshots: QuerySnapshot) {



        if(count == 0){

            for (s in snapshots){

                Log.d(TAG,"snapshotsがまわってます")
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
            .setRight(true)
            .setText(snapshot["message"] as String)
            .hideIcon(false)
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
            .setRight(false)
            .setText(snapshot["message"] as String)
            .hideIcon(false)
            .setSendTime(calendar)
            .build()
        chatView.setMessage(receivedMessage)
    }

}