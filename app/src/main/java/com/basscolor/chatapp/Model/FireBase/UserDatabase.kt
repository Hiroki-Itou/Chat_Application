package com.basscolor.chatapp.Model.FireBase

import com.basscolor.chatapp.Deta.UserData
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class UserDatabase {

    private val firestore = FirebaseFirestore.getInstance()

    @Synchronized
    suspend fun registration(userData: UserData):String = suspendCoroutine{cont->
        val registrationData = hashMapOf("name" to userData.getName(), "email" to userData.getEmail(), "userID" to userData.getUserID())
        firestore.collection("users").document(userData.getUserID()).set(registrationData).addOnSuccessListener { cont.resume("ユーザー登録が完了しました") }
            .addOnFailureListener { e -> cont.resumeWithException(e) }
    }

    @Synchronized
    suspend fun nameSearch(searchName:String):UserData? = suspendCoroutine{cont->
        userDataSearcher("name",searchName,{cont.resume(it)},{ cont.resume(null)},{cont.resumeWithException(it)})
    }

    @Synchronized
    suspend fun isEnabledEmail(email: String): Boolean = suspendCoroutine { cont ->
        userDataSearcher("email",email,{cont.resume(false) },{cont.resume(true)},{cont.resumeWithException(it)})
    }

    @Synchronized
    suspend fun isEnabledName(name: String): Boolean = suspendCoroutine {cont ->
        userDataSearcher("name",name,{cont.resume(false) },{cont.resume(true)},{cont.resumeWithException(it)})
    }

    private fun userDataSearcher(genre:String,searchWord:String, found:(UserData)->Unit, notFound:(String)->Unit, failure:(Exception)->Unit ){
        firestore.collection("users").whereEqualTo(genre,searchWord).get().addOnSuccessListener { result ->
            if(result.isEmpty){
                notFound("ユーザーが見つかりませんでした")
            }

            for (doc in result) {
                found(UserData(doc.data))
            }
        }.addOnFailureListener { e -> failure(e) }
    }
}