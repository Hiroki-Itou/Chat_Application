package com.basscolor.chatapp

import android.app.Activity
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_friend_search.*
import kotlin.Exception

class UserSearchActivityController(override val activity: Activity) :UserSearchActivityListener,ChatroomBuilderDelegate,UserSearcherDelegate{

    private val TAG = this.toString()
    private lateinit var searchUser : UserData
    private val searcher = UserSearcher(this)

    override fun search(userName:String){

        searcher.nameSearch(userName)
    }


    override fun found(userData: UserData) {
        Log.d(TAG,"入力したユーザーが見つかりました"+userData.getName())
        searchUser = userData
        activity.Friend_Name.text = searchUser.getName()
        activity.Friend_View.visibility = View.VISIBLE
    }

    override fun souldNotFound() {
        Log.d(TAG,"入力したユーザーは見つかりませんでした")
        activity.Friend_View.visibility = View.INVISIBLE
    }

    override fun searchError(e: Exception) {
        activity.Friend_View.visibility = View.INVISIBLE
        Log.d(TAG,"検索に失敗しました ",e)
    }



    override fun BuildSuccess() {
        activity.finish()
    }

    override fun BuildError(e: Exception) {
        Log.d(TAG,"ルーム作成に失敗しました ",e)
    }

    override fun addRoom() {

        val roomBuilder = ChatroomBuilder(this)
        val roomData = roomBuilder.makeRoomData(searchUser)
        roomBuilder.build(roomData)

//        val firebaseAuth = FirebaseAuth.getInstance()
//        val chatroomBuilder = ChatroomBuilder(this)
//        searcher.userID_Search(firebaseAuth.currentUser!!.uid)
//        val partnerRoom = chatroomBuilder.makeRoomData(searchUser,currentUser.getName())
//        val selfRoom = chatroomBuilder.makeRoomData(currentUser,currentUser.getName())

//        val currentUser = CurrentUser.getCurrentUserData()
//
//
//        val searchUserData = hashMapOf(
//            "message" to  CurrentUser.getCurrentUserData().name+"さんがチャットルームを作成しました",
//            "name" to CurrentUser.getCurrentUserData().name,
//            "uid" to CurrentUser.getCurrentUserData().uid
//        )
//        val currentData = hashMapOf(
//            "message" to  CurrentUser.getCurrentUserData().name+"さんがチャットルームを作成しました",
//            "name" to _searchUser!!.get_name(),
//            "uid" to _searchUser!!.get_uid()
//        )
//
//        val batch = _firestore.batch()
//
//        val currentRoom = _firestore.collection("users")
//            .document(currentUser.uid)
//            .collection("chatrooms")
//            .document(_searchUser!!.get_uid())
//
//
//        val searchUserRoom = _firestore.collection("users")
//            .document(_searchUser!!.get_uid())
//            .collection("chatrooms")
//            .document(currentUser.uid)

//        batch.set(currentRoom,currentData)
//        batch.set(searchUserRoom,searchUserData)
//
//        batch.commit().addOnSuccessListener {
//
//        }.addOnFailureListener {e ->
//
//        }
    }



}