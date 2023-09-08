package com.politecnicomalaga.mymarketlist.view.vActivities

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.MainController
import com.politecnicomalaga.mymarketlist.controller.cEntities.ServerData

class SuggestActivity : AppCompatActivity() {

    companion object {
        private var mySuggest: SuggestActivity? = null

        fun getInstance(): SuggestActivity {
            if (mySuggest == null) {
                mySuggest = SuggestActivity()
            }
            return mySuggest!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggest)
        MainController().setControllers(this@SuggestActivity, R.string.suggest, "")

        val txtFldSubject: TextInputLayout = findViewById(R.id.txtFldSubject)
        val txtFldMessage: TextInputLayout = findViewById(R.id.txtFldMessage)
        val btnSend: Button = findViewById(R.id.btnSend)

        txtFldMessage.editText!!.text.clear()
        txtFldSubject.editText!!.text.clear()

        txtFldMessage.editText?.addTextChangedListener {
            if (it?.isNotEmpty() == true) {
                txtFldMessage.error = null
            }
        }

        txtFldSubject.editText?.addTextChangedListener {
            if (it?.isNotEmpty() == true) {
                txtFldSubject.error = null
            }
        }

        btnSend.setOnClickListener {
            if (txtFldSubject.editText!!.text.isNullOrEmpty()) {
                txtFldSubject.error = getString(R.string.set_subject)
                return@setOnClickListener
            }
            if (txtFldMessage.editText!!.text.isNullOrEmpty()) {
                txtFldMessage.error = resources.getString(R.string.set_message)
                return@setOnClickListener
            }
            if (MainController().isConnected(this@SuggestActivity)) {
                ServerData(this@SuggestActivity).saveSuggest(
                    txtFldSubject.editText!!.text.toString(),
                    txtFldMessage.editText!!.text.toString()
                )
            }
        }
    }

    fun endSuggest(fromActivity: Activity) {
        fromActivity.setResult(RESULT_OK)
        fromActivity.finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                MainController().exitDialog(this@SuggestActivity)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}