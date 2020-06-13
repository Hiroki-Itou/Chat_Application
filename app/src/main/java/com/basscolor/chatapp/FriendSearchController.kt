package com.basscolor.chatapp

import android.app.Activity
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_friend_search.*

class FriendSearchController(override val activity: Activity) :FriendSearchActivityListener{

    private val TAG = this.toString()
    private val _firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var searchUser : User? = null
    override fun search(userName:String){

       _firestore.collection("users").whereEqualTo("name",userName).get().addOnSuccessListener { result ->

           if(result.isEmpty){
               Log.d(TAG,"入力したユーザーは見つかりませんでした")
               activity.Friend_View.visibility = View.INVISIBLE
               return@addOnSuccessListener
           }

           for (doc in result) {
               searchUser = User(doc.data)

              Log.d(TAG,"入力したユーザーが見つかりました"+searchUser!!.get_uid())
               activity.Friend_Name.text = searchUser!!.get_name()
               activity.Friend_View.visibility = View.VISIBLE
           }

       }.addOnFailureListener { e ->

           activity.Friend_View.visibility = View.INVISIBLE
           Log.d(TAG,"検索に失敗しました ",e)
       }
    }

    override fun addRoom() {

        val friend = hashMapOf(
            "message" to "",
            "name" to searchUser!!.get_name(),
            "uid" to searchUser!!.get_uid()
        )

        _firestore.collection("users")
            .document(_firebaseAuth.currentUser!!.uid)
            .collection("chatrooms")
            .add(friend).addOnSuccessListener {
                activity.finish()
            }.addOnFailureListener {e ->
                Log.d(TAG,"ルーム作成に失敗しました ",e)
            }
    }


}