package com.politecnicomalaga.mymarketlist.controller.http

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.MainController
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MyRequest(private val fromActivity: Activity) {
    companion object {
        private val IPs = arrayOf("192.168.58.30", "192.168.1.175", "79.147.88.210:8080")
        private var currentIP = IPs[0]
    }

    private var host: String = "http://$currentIP/PhpSql/"

    private val client: OkHttpClient = OkHttpClient()

    fun phpQuery(
        query: String,
        requestBody: MultipartBody,
        onResponseReceived: (String) -> Unit,
        onFail: () -> Unit
    ) {
        val url = "$host$query"
        val request =
            Request.Builder().url(url).post(requestBody).addHeader("cache-control", "no-cache")
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    Log.e("NET", e.toString())
                    onFail()
                    MainController().showToast(fromActivity, R.string.host_unreachable)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    onResponseReceived(response.body.string())
                }
            }
        })
    }

    fun checkIP() {
        try {
            val processBuilder = ProcessBuilder("/system/bin/ping", "-c", "1", currentIP)
            val process = processBuilder.start()
            val inputStream = process.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            var reach = false

            while (reader.readLine().also { line = it } != null) {
                // Lee la salida del proceso de ping
                if (line!!.contains("1 packets transmitted, 1 received")) {
                    Log.e("NET", "OK")
                    reach = true
                    break
                }
            }
            if (!reach) {
                Log.e("NET", "FAIL")
                if (currentIP != IPs.last()) {
                    currentIP = IPs[IPs.indexOf(currentIP) + 1]
                    checkIP()
                } else {
                    MainController().showToast(fromActivity, R.string.host_unreachable)
                }
            }
        } catch (_: Exception) {
        }
    }
}
