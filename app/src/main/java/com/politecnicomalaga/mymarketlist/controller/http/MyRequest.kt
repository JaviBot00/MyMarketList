package com.politecnicomalaga.mymarketlist.controller.http

import android.os.Handler
import android.os.Looper
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MyRequest {

    private val host: String = "http://192.168.1.175/SqlPhp/"
    private lateinit var myData: String

    companion object {
        const val PRODUCTS_QUERY = "ConsultaProductos.php"
        const val PRODUCTS_TABLE_QUERY = "ConsultaTablasProductos.php"
    }

    fun sendRequest(consulta: String) {
        val url = host + consulta
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val myCall = client.newCall(request)
        myCall.enqueue(object : Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val theAnswer = response.body!!.string()
                val myHandler = Handler(Looper.getMainLooper())
                myHandler.post {
                    myData = theAnswer
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                val myHandler = Handler(Looper.getMainLooper())
                myHandler.post {
                }
            }
        })
    }

    fun getMyData(): String {
        return myData
    }
}