package com.basscolor.chatapp

import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class UserSearcher(val userSearcherDelegate: UserSearcherDelegate){

    fun nameSearch(searchName:String){

        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("users").whereEqualTo("name",searchName).get().addOnSuccessListener { result ->

            if(result.isEmpty){
                userSearcherDelegate.souldNotFound()
            }

            for (doc in result) {
                val userData = UserData(doc.data)
                userSearcherDelegate.found(userData)
            }

        }.addOnFailureListener { e ->
            userSearcherDelegate.searchError(e)
        }
    }
}