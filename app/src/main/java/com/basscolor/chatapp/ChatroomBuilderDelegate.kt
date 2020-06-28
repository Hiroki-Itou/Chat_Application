package com.basscolor.chatapp

interface ChatroomBuilderDelegate {

    fun BuildSuccess()
    fun BuildError(e:Exception)

}