package com.basscolor.chatapp.Model.FireBase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


class Authentication {


    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    @Synchronized
    fun signin(email:String,password:String,success:(String)->Unit ,failure:(Exception)->Unit ){

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                success("サインイン完了")
            }.addOnFailureListener { e ->
                failure(e)
            }
    }

    @Synchronized
    fun login(email:String,password:String,success:(String)->Unit ,failure:(Exception)->Unit ){

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                success("ログイン完了")
            }.addOnFailureListener { e ->
                failure(e)
            }

    }

    @Synchronized
    fun setProfile(userName:String,uri: Uri,success:(String)->Unit ,failure:(Exception)->Unit ){

        val currentUser = firebaseAuth.currentUser
        val request = UserProfileChangeRequest.Builder()
            .setDisplayName(userName)
            .setPhotoUri(uri)
            .build()
        currentUser!!.updateProfile(request).addOnSuccessListener {
            success("プロフィールの登録が完了しました")
        }.addOnFailureListener { e->
            failure(e)
        }
    }

    fun isLogin():Boolean {
        return firebaseAuth.currentUser != null
    }
}