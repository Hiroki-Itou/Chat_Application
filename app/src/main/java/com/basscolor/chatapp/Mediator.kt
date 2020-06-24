package com.basscolor.chatapp

interface Mediator {

    fun cureateColleagues()
    fun colleagueSuccess(colleague:Colleague)
    fun colleagueFailure(colleague:Colleague)
    fun colleagueError(colleague:Colleague)
}