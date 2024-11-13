package com.hotguy.mymarketlist.view.vFragments;

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hotguy.mymarketlist.R
import com.hotguy.mymarketlist.controller.MainController
import com.hotguy.mymarketlist.controller.cAdapter.rvProducts.ProductsRVAdapter
import com.hotguy.mymarketlist.controller.cEntities.ServerData
import com.hotguy.mymarketlist.view.MainActivity

class SingInFragment: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         installSplashScreen()

        // Inicializar Firebase Auth
        val myAuth: FirebaseAuth = FirebaseAuth.getInstance()

        // Verificar si el usuario ya ha iniciado sesión
        val currentUser: FirebaseUser? = myAuth.currentUser
        if (currentUser != null) {
            // Usuario ya logueado, redirigir a MainActivity
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

//        // Comprobar si el usuario ya ha iniciado sesión
//        val prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
//        val isLoggedIn = prefs.getBoolean("isLoggedIn", false)
//
//        if (isLoggedIn) {
//            // Redirigir a MainActivity si ya está logueado
//            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//            finish() // Cierra LoginActivity para que no se pueda regresar a ella
//        }

        setContentView(R.layout.activity_login)
//        checkAccess(this@LoginActivity)
        MainController().setControllers(this@LoginActivity, R.string.app_name, "")

        ServerData(this@LoginActivity).getServerProductTables()

//        val textInputEmail: TextInputLayout = findViewById(R.id.txtFldUser)
//        val textInputPass: TextInputLayout = findViewById(R.id.txtFldPass)
//        val btnAccess: Button = findViewById(R.id.btnAccess)
//        val btnRegister: Button = findViewById(R.id.btnRegister)
//
//        textInputEmail.editText!!.text.clear()
//        textInputPass.editText!!.text.clear()
//
//        textInputEmail.editText?.addTextChangedListener {
//            if (it?.isNotEmpty() == true) {
//                textInputEmail.error = null
//            }
//        }
//
//        textInputPass.editText?.addTextChangedListener {
//            if (it?.isNotEmpty() == true) {
//                textInputPass.error = null
//            }
//        }
//
//        btnAccess.setOnClickListener {
//            if (textInputEmail.editText!!.text.isNullOrEmpty()) {
//                textInputEmail.error = resources.getString(R.string.set_user)
//                return@setOnClickListener
//            }
//            if (textInputPass.editText!!.text.isNullOrEmpty()) {
//                textInputPass.error = getString(R.string.set_password)
//                return@setOnClickListener
//            }
//            if (textInputEmail.editText!!.text.toString() == "admin" && textInputPass.editText!!.text.toString() == "admin") {
//                val user = UserFeatures()
//                user.userName = "admin"
//                user.passWord = "admin"
//                user.email = "admin@admin.com"
//                user.imgProfileWeb = "/photos/admin.gif"
//                ClientSQLite(this@LoginActivity).setUser(user)
////                doAccess(this@LoginActivity, prefs)
//            }
//            if (MainController().isConnected(this@LoginActivity)) {
//                ServerData(this@LoginActivity).getServerUser(
//                    textInputUser.editText!!.text.toString().lowercase(Locale.ROOT),
//                    textInputPass.editText!!.text.toString().lowercase(Locale.ROOT)
//                )
//            }

//            myAuth.signInWithEmailAndPassword(
//                textInputEmail.editText!!.text.toString(), textInputPass.editText!!.text.toString()
//            ).addOnCompleteListener(this@LoginActivity) { task ->
//                if (task.isSuccessful) {
//                    // Inicio de sesión exitoso
//                    val user: FirebaseUser? = myAuth.currentUser
//                    if (user != null) {
//                        // Redirigir a MainActivity
//                        startActivity(
//                            Intent(
//                                this@LoginActivity, MainActivity::class.java
//                            )
//                        )
//                        finish()
//                    }
//                } else {
//                    // Si el inicio de sesión falla, mostrar un mensaje
//                    Toast.makeText(
//                        this@LoginActivity, "Authentication Failed.", Toast.LENGTH_SHORT
//                    ).show()
//                    Log.w("LoginActivity", "signInWithEmail:failure", task.exception)
//                }
//            }
//        }
//
//        btnRegister.setOnClickListener {
//            startActivityForResult(
//                Intent(this@LoginActivity, RegisterActivity::class.java), REGISTER_REQUEST
//            )
//        }
    }

//    private fun checkAccess(fromActivity: Activity) {
//        if (ClientSQLite(fromActivity).getUser().userName.isNotEmpty()) {
//            doAccess(fromActivity)
//        }
//    }

    fun doAccess(fromActivity: Activity, prefs: SharedPreferences) {
        // Aquí va la lógica de autenticación
        // Si es exitosa:
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putBoolean("isLoggedIn", true) // Marcar como logueado
        editor.apply()

        // Navegar a MainActivity
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()

//        if (fromActivity is LoginActivity) {
//            fromActivity.startActivityForResult(
//                Intent(fromActivity, MainActivity::class.java), CONTROLPANEL_REQUEST
//            )
//            fromActivity.findViewById<TextInputLayout>(R.id.txtFldUser).editText!!.text.clear()
//            fromActivity.findViewById<TextInputLayout>(R.id.txtFldPass).editText!!.text.clear()
//        }
    }
}
