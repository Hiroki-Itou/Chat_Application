package com.basscolor.chatapp.Model.FireBase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class Authentication {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    @Synchronized
    suspend fun signin(email:String,password:String) = suspendCoroutine<String> {cont->
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener { cont.resume("サインイン完了") }.addOnFailureListener { e -> cont.resumeWithException(e) }
    }

    @Synchronized
    suspend fun login(email:String,password:String) = suspendCoroutine<String> {cont->
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener { cont.resume("ログイン完了") }.addOnFailureListener { e -> cont.resumeWithException(e) }
    }

    @Synchronized
    suspend fun  setProfile(userName:String,uri: Uri) = suspendCoroutine<String> {cont->
        val currentUser = firebaseAuth.currentUser
        val request = UserProfileChangeRequest.Builder().setDisplayName(userName).setPhotoUri(uri).build()
        currentUser!!.updateProfile(request).addOnSuccessListener { cont.resume("プロフィールの登録が完了しました") }.addOnFailureListener { e-> cont.resumeWithException(e) }
    }

    fun isLogin():Boolean {
        return firebaseAuth.currentUser != null
    }
}