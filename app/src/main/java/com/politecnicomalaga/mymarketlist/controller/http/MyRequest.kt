package com.politecnicomalaga.mymarketlist.controller.http

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

class MyRequest {

    private val host: String = "http://192.168.1.175/PhpSql/"

    fun phpQuery(query: String, callback: Callback) {
        val url = host + query
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("cache-control", "no-cache")
            .build()

        client.newCall(request).enqueue(callback)
    }

}