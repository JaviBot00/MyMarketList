package com.politecnicomalaga.mymarketlist.model

class Product : Comparable<Product> {

    var nId: Int = 0
    var sName: String = ""
    var nIdType: Int = 0 // Para el catalago

    var nIdList: Int = 0 // Para  guardar en la lista

    override fun compareTo(other: Product): Int {
        return sName.compareTo(other.sName)
    }
}
