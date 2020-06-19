package com.basscolor.chatapp

class UserContainer(val dec:Map<String, Any>) {

    fun get_email():String{
        var email =  dec["email"] as String?
        if (email == null) {
            email = "no_email"
        }
        return email
    }
    fun get_name():String{
        var name = dec["name"] as String?
        if (name == null) {
            name = "anknown"
        }
        return name
    }
    fun get_uid():String{
        var uid = dec["uid"] as String?
        if (uid == null) {
            uid = "no_ID"
        }
          return uid
    }

}