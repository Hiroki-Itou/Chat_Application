package com.basscolor.chatapp

import android.app.Activity
import android.util.Log
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_friend_search.*
import java.lang.Exception

class FriendSearchController(override val activity: Activity) :FriendSearchActivityListener{

    private val TAG = this.toString()
    private val _firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var _searchUser : UserContainer? = null
    override fun search(userName:String){

        val dataBase = DataBase()
        dataBase.search_User(userName,object : DataBase.Search_User_Delegate {
            override fun success(searchUser: UserContainer) {
                Log.d(TAG,"入力したユーザーが見つかりました"+searchUser.get_uid())
                _searchUser = searchUser
                activity.Friend_Name.text = _searchUser!!.get_name()
                activity.Friend_View.visibility = View.VISIBLE
            }

            override fun failure() {
                Log.d(TAG,"入力したユーザーは見つかりませんでした")
                activity.Friend_View.visibility = View.INVISIBLE
            }

            override fun error(exception: Exception) {
                activity.Friend_View.visibility = View.INVISIBLE
                Log.d(TAG,"検索に失敗しました ",exception)
            }
        })
    }

    override fun addRoom() {

        val currentUser = CurrentUser.getCurrentUserData()


        val searchUserData = hashMapOf(
            "message" to  CurrentUser.getCurrentUserData().name+"さんがチャットルームを作成しました",
            "name" to CurrentUser.getCurrentUserData().name,
            "uid" to CurrentUser.getCurrentUserData().uid
        )
        val currentData = hashMapOf(
            "message" to  CurrentUser.getCurrentUserData().name+"さんがチャットルームを作成しました",
            "name" to _searchUser!!.get_name(),
            "uid" to _searchUser!!.get_uid()
        )

        val batch = _firestore.batch()

        val currentRoom = _firestore.collection("users")
            .document(currentUser.uid)
            .collection("chatrooms")
            .document(_searchUser!!.get_uid())

        val searchUserRoom = _firestore.collection("users")
            .document(_searchUser!!.get_uid())
            .collection("chatrooms")
            .document(currentUser.uid)

        batch.set(currentRoom,currentData)
        batch.set(searchUserRoom,searchUserData)

        batch.commit().addOnSuccessListener {
            activity.finish()
        }.addOnFailureListener {e ->
            Log.d(TAG,"ルーム作成に失敗しました ",e)
        }
    }


}