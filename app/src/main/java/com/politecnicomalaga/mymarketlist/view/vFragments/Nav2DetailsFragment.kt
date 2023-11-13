package com.politecnicomalaga.mymarketlist.view.vFragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.cHTTP.MyRequest
import com.politecnicomalaga.mymarketlist.controller.cSQLite.ClientSQLite
import com.politecnicomalaga.mymarketlist.view.vActivities.LoginActivity
//import com.squareup.picasso.Picasso

class Nav2DetailsFragment : Fragment {

    private val fromActivity: Activity

    constructor(fromActivity: Activity) : super() {
        this.fromActivity = fromActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav2_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = ClientSQLite(fromActivity).getUser()
        val imageView: ImageView = fromActivity.findViewById(R.id.imgProfileMM)
        val txtViewUsername: TextView = fromActivity.findViewById(R.id.txtViewUsername)
        val txtViewEmail: TextView = fromActivity.findViewById(R.id.txtViewEmail)
        val btnAbout: Button = fromActivity.findViewById(R.id.btnAbout)
//        val btnSetting: Button = fromActivity.findViewById(R.id.btnSetting)
        val btnLogOut: Button = fromActivity.findViewById(R.id.btnLogOut)

        val imageUrl = MyRequest.host + user.imgProfileWeb

//        Picasso.get().load(imageUrl).into(imageView)

        txtViewUsername.text = user.userName
        txtViewEmail.text = user.email

        btnAbout.setOnClickListener {
            MaterialAlertDialogBuilder(fromActivity).setTitle(fromActivity.resources.getString(R.string.about))
                .setMessage(fromActivity.resources.getString(R.string.about_me))
                .setPositiveButton(fromActivity.resources.getString(R.string.accept)) { dialog, _ ->
                    dialog.dismiss()
                }.setCancelable(false).show()
        }

//        btnSetting.setOnClickListener {
//            MainController().showToast(fromActivity, R.string.in_maintenance)
//        }

        btnLogOut.setOnClickListener {
            ClientSQLite(fromActivity).resetTables(
                ClientSQLite(fromActivity).writableDatabase, true, true
            )
            val result = Intent(fromActivity, LoginActivity::class.java)
            fromActivity.setResult(AppCompatActivity.RESULT_OK, result)
            fromActivity.finish()
        }
    }
}