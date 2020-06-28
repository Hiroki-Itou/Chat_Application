package com.basscolor.chatapp

import java.lang.Exception

interface LoginDelegate {

    fun loginSuccess()
    fun loginFailure()
    fun loginError(e:Exception?)

}