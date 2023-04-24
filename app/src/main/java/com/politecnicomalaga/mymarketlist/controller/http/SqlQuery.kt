package com.politecnicomalaga.mymarketlist.controller.http

import com.politecnicomalaga.mymarketlist.controller.http.MyRequest.Companion.PRODUCTS_QUERY
import com.politecnicomalaga.mymarketlist.controller.http.MyRequest.Companion.PRODUCTS_TABLE_QUERY
import java.util.SortedMap
import java.util.SortedSet

class SqlQuery {

    private val request = MyRequest()

//    private val myProductsList: SortedMap<String?, SortedSet<String?>?>? = null
    private val myProductsList = sortedMapOf<String, SortedSet<String>>()
    private val myProducts = sortedSetOf<String>()

    private fun getTables(): String {
//        val client = OkHttpClient()
//        val request =
//            Request.Builder().url("http://192.168.1.175/SqlPhp/ConsultaTablasProductos.php").get().build()
//        val response = client.newCall(request).execute()
//        return response.body?.string() ?: ""
        request.sendRequest(PRODUCTS_TABLE_QUERY)
        return request.getMyData()
    }

    fun getProducts() {
        val tables = getTables().split(";")
//        val client = OkHttpClient()
        tables.forEach { t ->
//            val request =
//                Request.Builder().url("http://192.168.1.175/SqlPhp/ConsultaProductos.php?tables=$t").get()
//                    .build()
//            val response = client.newCall(request).execute()
//            val getProducts = response.body?.string() ?: ""
            request.sendRequest("$PRODUCTS_QUERY?tables=$t")
            val getProducts = request.getMyData()
            getProducts.split(";").forEach { p ->
                myProducts.add(p)
            }
            myProductsList[t] = myProducts
        }
    }

    fun getMyProductList(): SortedMap<String, SortedSet<String>> {
        return myProductsList
    }

    fun getMyProduct(): SortedSet<String> {
        return myProducts
    }
}