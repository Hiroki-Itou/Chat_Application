package com.basscolor.chatapp

interface UserSearcherDelegate {

    fun found(userData: UserData)
    fun souldNotFound()
    fun searchError(e:Exception)

}