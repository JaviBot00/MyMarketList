package com.politecnicomalaga.mymarketlist.model

class Product() {

    var name: String = ""
    var isAdd: Boolean = false

    constructor(name: String) : this() {
        this.name = name
    }

}