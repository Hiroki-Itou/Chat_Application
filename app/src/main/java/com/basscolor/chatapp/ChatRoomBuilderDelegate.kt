package com.basscolor.chatapp

interface ChatRoomBuilderDelegate {

    fun BuildSuccess()
    fun BuildError(e:Exception)

}