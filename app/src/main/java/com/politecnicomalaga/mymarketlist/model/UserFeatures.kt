package com.politecnicomalaga.mymarketlist.model

class UserFeatures() {
    var userName: String = ""
    var passWord: String = ""
    var email: String = ""
    var imgProfile: ByteArray? = null

    constructor(
        userName: String, passWord: String, birthDay: String, imgProfile: ByteArray
    ) : this() {
        this.userName = userName
        this.passWord = passWord
        this.email = birthDay
        this.imgProfile = imgProfile
    }

}