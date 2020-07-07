package com.basscolor.chatapp.FireBase

import com.basscolor.chatapp.UserData
import com.google.firebase.firestore.FirebaseFirestore

class UserDatabase {


    private val firestore = FirebaseFirestore.getInstance()

    fun registration(userData: UserData,success:(String)->Unit ,failure:(Exception)->Unit ){

        val registData = hashMapOf(
            "name" to userData.getName(),
            "email" to userData.getEmail(),
            "userID" to userData.getUserID()
        )

        firestore.collection("users")
            .document(userData.getUserID())
            .set(registData)
            .addOnSuccessListener {
               success("ユーザー登録が完了しました")
            }
            .addOnFailureListener { e ->
                failure(e)
            }
    }

    fun nameSearch(searchName:String,found:(UserData)->Unit,notFound:(String)->Unit,failure:(Exception)->Unit ){


        firestore.collection("users").whereEqualTo("name",searchName).get().addOnSuccessListener { result ->

            if(result.isEmpty){
                notFound("ユーザーが見つかりませんでした")
            }

            for (doc in result) {
                found(UserData(doc.data))
            }

        }.addOnFailureListener { e ->
            failure(e)
        }
    }

}