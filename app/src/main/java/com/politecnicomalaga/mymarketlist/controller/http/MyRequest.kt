package com.politecnicomalaga.mymarketlist.controller.http

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.MainController
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

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

        val handler = Handler(Looper.getMainLooper())
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                handler.post {
                    Log.e("NET", e.toString())
                    onFail()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                handler.post {
                    onResponseReceived(response.body.string())
                }
            }
        })
    }

    fun checkNetworkConnectivity() {
        val connectivityManager =
            fromActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        if (networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            checkIP()
        } else {
            MainController().showToast(fromActivity, R.string.error_network)
            MainController().showToast(fromActivity, R.string.check_network)
            fromActivity.window.statusBarColor =
                ContextCompat.getColor(fromActivity, R.color.colorPrimary)
        }
    }

    private fun checkIP() {
        val client = OkHttpClient.Builder().callTimeout(10, TimeUnit.SECONDS).build()

        val request =
            Request.Builder().url("http://$currentIP") // Reemplaza con la URL de tu servidor
                .build()

        val handler = Handler(Looper.getMainLooper())
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                handler.post {
                    // Error de conexión
                    Log.e("NET", "INVALID IP")
                    if (currentIP != IPs.last()) {
                        currentIP = IPs[IPs.indexOf(currentIP) + 1]
                        checkIP()
                    } else {
                        MainController().showToast(fromActivity, R.string.fail_connection)
                    }
                }
            }

            override fun onResponse(call: Call, response: Response) {
                handler.post {
                    // Respuesta exitosa
                    fromActivity.window.statusBarColor =
                        ContextCompat.getColor(fromActivity, android.R.color.holo_green_dark)
                    MainController().showToast(fromActivity, R.string.successful_connection)
                    Log.e("NET", "OK")
                    // Aquí puedes realizar las acciones necesarias cuando la conexión con el servidor es exitosa
                }
            }
        })
    }
}
