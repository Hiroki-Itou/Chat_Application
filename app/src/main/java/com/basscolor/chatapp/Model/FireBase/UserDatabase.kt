package com.basscolor.chatapp.Model.FireBase

import com.basscolor.chatapp.Deta.UserData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class UserDatabase {


    private val firestore = FirebaseFirestore.getInstance()

    @Synchronized
    suspend fun registration(userData: UserData):String{
        val registrationData = hashMapOf(
            "name" to userData.getName(),
            "email" to userData.getEmail(),
            "userID" to userData.getUserID()
        )
        return suspendCoroutine { cont ->
            firestore.collection("users")
                .document(userData.getUserID())
                .set(registrationData)
                .addOnSuccessListener {
                    cont.resume("ユーザー登録が完了しました")
                }
                .addOnFailureListener { e ->
                    cont.resumeWithException(e)
                }
        }
    }

    fun nameSearch(searchName:String, found:(UserData)->Unit, notFound:(String)->Unit, failure:(Exception)->Unit ){

        userDataSearcher("name",searchName,{found(it)},{notFound(it)},{failure(it)})
    }

    @Synchronized
    suspend fun isEnabledEmail(email: String): Boolean {
        return suspendCoroutine { cont ->
            userDataSearcher("email",email,{cont.resume(false) },{cont.resume(true)},{cont.resumeWithException(it)})
        }
    }
    @Synchronized
    suspend fun isEnabledName(name: String): Boolean {
        return suspendCoroutine { cont ->
            userDataSearcher("name",name,{cont.resume(false) },{cont.resume(true)},{cont.resumeWithException(it)})
        }
    }

    private fun userDataSearcher(genre:String,searchWord:String, found:(UserData)->Unit, notFound:(String)->Unit, failure:(Exception)->Unit ){

        firestore.collection("users").whereEqualTo(genre,searchWord).get().addOnSuccessListener { result ->

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