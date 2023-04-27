package com.politecnicomalaga.mymarketlist.controller

import android.content.Context
import com.politecnicomalaga.mymarketlist.controller.adapter.ManagerSQLite
import com.politecnicomalaga.mymarketlist.controller.http.MyRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class MainController {

//    private val myProductsList = sortedMapOf<String, SortedSet<String>>()

    fun getTables(context: Context) {
        val request1 = MyRequest()
        request1.phpQuery(MyRequest.PRODUCTS_TABLE_QUERY, object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val tables = response.body?.string()
                val mySQLite = ManagerSQLite(context)
                mySQLite.setWritable()
                mySQLite.setReadable()
                tables?.split(";")?.forEach { t ->
//                    val request2 = MyRequest()
//                    request2.phpQuery(MyRequest.SHOW_CREATE_TABLE + "?table=" + t,
//                        object : Callback {
//                            override fun onResponse(call: Call, response: Response) {
//                                val schema = response.body?.string()
                    if (t.isNotEmpty()) {
                        mySQLite.createTable(t)
                    }
//                            }
//
//                            override fun onFailure(call: Call, e: IOException) {
//                                e.printStackTrace()
//                            }
//                        })
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

}