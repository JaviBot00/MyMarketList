package com.politecnicomalaga.mymarketlist.model

import android.graphics.Bitmap
import java.io.Serializable

class UserFeatures() : Serializable {
    var userName: String = ""
    var passWord: String = ""
    var birthDay: String = ""
    var imgProfile: Bitmap? = null

    constructor(
        userName: String,
        passWord: String,
        birthDay: String,
        imgProfile: Bitmap?
    ) : this() {
        this.userName = userName
        this.passWord = passWord
        this.birthDay = birthDay
        this.imgProfile = imgProfile
    }

}