package com.basscolor.chatapp

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class DataBase{

    private val firestore = FirebaseFirestore.getInstance()


    fun registration(registData:Map<String,String?>,registration_Delegate:Registration_Delegate){

        firestore.collection("users")
            .document(registData["uid"]as String)
            .set(registData)
            .addOnSuccessListener {
                registration_Delegate.success()
            }
            .addOnFailureListener { e ->
                registration_Delegate.error(e)
            }
    }

    fun get_currentUserDatabase(uid:String,get_currentUserDatabase_Delegate:Get_currentUserDatabase_Delegate){

        firestore.collection("users").document(uid).get().addOnSuccessListener { result ->
            val currentUser = UserContainer(result.data!!)
            get_currentUserDatabase_Delegate.success(currentUser)
        }.addOnFailureListener { e->
            get_currentUserDatabase_Delegate.error(e)
        }
    }


    fun search_User(searchName:String,search_User_Delegate:Search_User_Delegate){

        firestore.collection("users").whereEqualTo("name",searchName).get().addOnSuccessListener { result ->

            if(result.isEmpty){
                search_User_Delegate.failure()
            }

            for (doc in result) {
                val container = UserContainer(doc.data)
                search_User_Delegate.success(container)
            }

        }.addOnFailureListener { e ->
            search_User_Delegate.error(e)

            Log.d(ContentValues.TAG,"検索に失敗しました ",e)
        }
    }
    interface Get_currentUserDatabase_Delegate{

        fun success(user:UserContainer)
        fun error(exception: Exception)
    }

    interface Search_User_Delegate{

        fun success(searchUser:UserContainer)
        fun failure()
        fun error(exception: Exception)
    }

    interface Registration_Delegate{
        fun success()
        fun error(exception: Exception)
    }

}