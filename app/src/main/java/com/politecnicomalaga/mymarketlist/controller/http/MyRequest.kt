package com.politecnicomalaga.mymarketlist.controller.http

import android.app.Activity
import android.os.Handler
import android.os.Looper
import com.google.android.material.snackbar.Snackbar
import com.politecnicomalaga.mymarketlist.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class MyRequest(val fromActivity: Activity) {

    private val host: String = "http://192.168.1.175/PhpSql/"

    //    private val host: String = "http://192.168.58.30/PhpSql/"
//    private val host: String = "http://79.147.83.111:8080/PhpSql/"
    private val client: OkHttpClient = OkHttpClient()

    fun phpQuery(query: String, requestBody: MultipartBody, onResponseReceived: (String) -> Unit) {
        val url = host + query
        val request =
            Request.Builder().url(url).post(requestBody).addHeader("cache-control", "no-cache").build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val myHandler = Handler(Looper.getMainLooper())
                myHandler.post {
                    Snackbar.make(
                        fromActivity.findViewById(android.R.id.content),
                        fromActivity.getString(R.string.host_unreachable),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val myHandler = Handler(Looper.getMainLooper())
                myHandler.post {
                    onResponseReceived(response.body.string())
                }
            }
        })
    }
}