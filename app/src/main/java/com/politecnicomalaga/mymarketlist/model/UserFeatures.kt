package com.politecnicomalaga.mymarketlist.model

class UserFeatures() {
    var userName: String = ""
    var passWord: String = ""
    var email: String = ""
    var imgProfile: ByteArray? = null
    var imgProfileWeb: String = ""

    constructor(
        userName: String, passWord: String, birthDay: String, imgProfile: ByteArray?, imgProfileWeb: String
    ) : this() {
        this.userName = userName
        this.passWord = passWord
        this.email = birthDay
        this.imgProfile = imgProfile
        this.imgProfileWeb = imgProfileWeb
    }

}