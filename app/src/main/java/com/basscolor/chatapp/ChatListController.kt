package com.basscolor.chatapp

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.SimpleAdapter
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception
import kotlin.collections.ArrayList

class ChatListController(override val activity: Activity, override var listView: ListView) :ChatListActivityListener {

    private val _firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun load_ChatList() {

        val chatrooms = Chatrooms(CurrentUser.getCurrentUserData().uid)

        chatrooms.load_Chatrooms(object : Chatrooms.Load_Chatrooms_Delegate {

            override fun success(containers: ArrayList<UserContainer>) {

                val roomList: MutableList<MutableMap<String,Any>> = mutableListOf()

                val testClass = TestClass()
                testClass.name = "テストの名前"

                containers.forEach Loop@{user ->
                    Log.d(TAG, user.get_name()+"/"+user.get_email() +"/"+user.get_uid())
                    val room = mutableMapOf("imageView" to R.drawable.icon,"name" to user.get_name(), "last_message" to "チャットルームが作成されました" , "testClass" to testClass)

                    roomList.add(room)

                }

                val from = arrayOf("imageView","name", "last_message")
                val to = intArrayOf(R.id.imageView,R.id.friend_name, R.id.last_message)
                val adapter = SimpleAdapter(activity,roomList,R.layout.list_items,from,to)


                listView.adapter = adapter
            }
            override fun error(exception: Exception) {
            }
        })

//        _firestore.collection("users").addSnapshotListener{values,e ->
//         //リアルタイム取得方法
//        }
    }

    override fun add_Chatlist() {
        activity.startActivity(Intent(activity, FriendSearchActivity::class.java))

    }

    override fun select_ChatList(mutableMap:MutableMap<String,Any>) {


        val test = mutableMap["testClass"] as TestClass
        test.testMesod()

        Log.d(TAG,"チャットをクリックしました"+mutableMap["name"])


    }

    override fun logOut() {

        _firebaseAuth.signOut()
        activity.finish()
    }

    override fun reload() {

        load_ChatList()
    }


    class TestClass(){

        var name = "testName"

        fun testMesod(){
            Log.d("Test","テストmessageを表示"+name)
        }
    }


}