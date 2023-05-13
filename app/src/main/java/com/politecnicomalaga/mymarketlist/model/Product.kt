package com.politecnicomalaga.mymarketlist.model

class Product(val name: String, var isAdd: Boolean = false) : Comparable<Product> {
    override fun compareTo(other: Product): Int {
        return name.compareTo(other.name)
    }
}
