package com.basscolor.chatapp

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.widget.ListView
import android.widget.SimpleAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlin.Exception
import kotlin.collections.ArrayList

class ChatRoomListActivityController(override val activity: Activity, override var listView: ListView) :ChatRoomListActivityListener,LandlordDelegate {



    private val firebaseAuth = FirebaseAuth.getInstance()


    override fun loadChatList() {

        val landlord = Landlord(firebaseAuth.currentUser!!.uid,this)

        landlord.loadChatroomListInformation()


    }


    override fun readInformationSuccess(chatRooms: ArrayList<ChatRoom>) {

        val roomList: MutableList<MutableMap<String,Any?>> = mutableListOf()

        val currentName = firebaseAuth.currentUser!!.displayName

        chatRooms.forEach Loop@{room ->

            val userNames = room.document["userNames"] as ArrayList<String>
            val roomName :String
            roomName = if(userNames[0] == currentName){
                userNames[1]
            }else{
                userNames[0]
            }

            val roomData = mutableMapOf("imageView" to R.drawable.aoi,"roomName" to roomName, "doorMessagePlate" to room.document["doorMessagePlate"], "class" to room)
            roomList.add(roomData)
        }

        val from = arrayOf("imageView","roomName", "doorMessagePlate")
        val to = intArrayOf(R.id.imageView,R.id.roomName, R.id.doorMessagePlate)
        val adapter = SimpleAdapter(activity,roomList,R.layout.list_items,from,to)
        listView.adapter = adapter
    }

    override fun readInformationFailure() {
        Log.w(TAG, "まだチャットルームは作成されていません")
    }

    override fun readInformationError(e: Exception) {
        Log.w(TAG, "チャットリストの取得に失敗しました", e)
    }


    override fun addChatlist() {
        activity.startActivity(Intent(activity, UserSearchActivity::class.java))

    }

    override fun selectChatRoom(mutableMap:MutableMap<String,Any>) {

        val chatroom = mutableMap["class"] as ChatRoom

        val intent = Intent(activity,ChatRoomActivity::class.java)
        intent.putExtra("chatRoom",chatroom)
        activity.startActivity(intent)

    }

    override fun logOut() {

        firebaseAuth.signOut()
        activity.finish()
    }

    override fun reload() {

        loadChatList()
    }




}