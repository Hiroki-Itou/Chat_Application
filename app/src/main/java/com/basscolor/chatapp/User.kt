package com.basscolor.chatapp

class User(dec:Map<String, Any>) {

    private var email : String? = null
    private var name : String? = null
    private var lastMessage : String? = null

    init {

        this.email = dec["email"] as String?
        if (email == null) {
            this.email = "no_email"
        }
        this.name = dec["name"] as String?
            if (name == null) {
            this.name = "anknown"
        }
        this.lastMessage = dec["last_message"] as String?
        if (lastMessage == null) {
            this.lastMessage = ""
        }
    }

    fun get_email():String{
       return email!!
    }
    fun get_name():String{
      return name!!
    }
    fun get_lastMessage():String{
      return lastMessage!!
    }

}