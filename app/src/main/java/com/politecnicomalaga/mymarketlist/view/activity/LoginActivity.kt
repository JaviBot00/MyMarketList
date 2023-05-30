package com.politecnicomalaga.mymarketlist.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.MainController
import com.politecnicomalaga.mymarketlist.controller.SQLite.ClientSQLite
import com.politecnicomalaga.mymarketlist.controller.entities.ServerData
import com.politecnicomalaga.mymarketlist.controller.http.MyRequest
import java.util.Locale

class LoginActivity : AppCompatActivity() {

    companion object {
        private var myLogin: LoginActivity? = null

        fun getInstance(): LoginActivity {
            if (myLogin == null) {
                myLogin = LoginActivity()
            }
            return myLogin!!
        }
    }

    private val REGISTER_REQUEST = 1
    private val CONTROLPANEL_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyRequest(this@LoginActivity).checkNetworkConnectivity()
        installSplashScreen()
        setContentView(R.layout.activity_login)
        MainController().setAppBar(this@LoginActivity, resources.getString(R.string.app_name))
//        ServerData(this@LoginActivity).getServerProductTables()

        checkAccess(this@LoginActivity)

        val textInputUser: TextInputLayout = findViewById(R.id.txtFldUser)
        val textInputPass: TextInputLayout = findViewById(R.id.txtFldPass)
        val btnAccess: Button = findViewById(R.id.btnAccess)
        val btnRegister: Button = findViewById(R.id.btnRegister)

        textInputUser.editText!!.text.clear()
        textInputPass.editText!!.text.clear()

        textInputUser.editText?.addTextChangedListener {
            if (it?.isNotEmpty() == true) {
                textInputUser.error = null
            }
        }

        textInputPass.editText?.addTextChangedListener {
            if (it?.isNotEmpty() == true) {
                textInputPass.error = null
            }
        }

        btnAccess.setOnClickListener {
            if (textInputUser.editText!!.text.isNullOrEmpty()) {
                textInputUser.error = resources.getString(R.string.set_user)
                return@setOnClickListener
            }
            if (textInputPass.editText!!.text.isNullOrEmpty()) {
                textInputPass.error = getString(R.string.set_password)
                return@setOnClickListener
            }
            ServerData(this@LoginActivity).getServerUser(
                textInputUser.editText?.text.toString().lowercase(Locale.ROOT),
                textInputPass.editText?.text.toString().lowercase(Locale.ROOT)
            )
        }

        btnRegister.setOnClickListener {
            startActivityForResult(
                Intent(this@LoginActivity, RegisterActivity::class.java), REGISTER_REQUEST
            )
        }
    }

    private fun checkAccess(fromActivity: Activity) {
        val mySQLite = ClientSQLite(fromActivity)
        mySQLite.setReadable()
        if (mySQLite.getUser().moveToNext()) {
            doAccess(fromActivity)
        }
        mySQLite.getDb().close()
    }

    fun doAccess(fromActivity: Activity) {
        fromActivity.startActivityForResult(
            Intent(fromActivity, ControlPanelActivity::class.java), CONTROLPANEL_REQUEST
        )
        fromActivity.findViewById<TextInputLayout>(R.id.txtFldUser).editText!!.text.clear()
        fromActivity.findViewById<TextInputLayout>(R.id.txtFldPass).editText!!.text.clear()
    }

    fun setError(fromActivity: Activity, message: Int) {
        MainController().showToast(
            fromActivity, message
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REGISTER_REQUEST -> {
                when (resultCode) {
                    RESULT_OK -> {
                        MainController().showToast(
                            this@LoginActivity, R.string.user_successfully_registered
                        )
                        doAccess(this@LoginActivity)
                    }

                    RESULT_CANCELED -> {
                        MainController().showToast(this@LoginActivity, R.string.user_not_registered)
                    }
                }
            }

            CONTROLPANEL_REQUEST -> {
                if (resultCode == RESULT_OK) {
                    MainController().showToast(this@LoginActivity, R.string.successful_log_out)
                }
            }
        }
    }
}