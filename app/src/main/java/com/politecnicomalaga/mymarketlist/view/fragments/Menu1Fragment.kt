package com.politecnicomalaga.mymarketlist.view.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.view.activity.ListActivity
import com.politecnicomalaga.mymarketlist.view.activity.ProductsActivity

class Menu1Fragment(private val fromActivity: Activity) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnMakeList: Button = view.findViewById(R.id.btnMakeList)
        btnMakeList.setOnClickListener {
            startActivityForResult(Intent(fromActivity, ProductsActivity::class.java), 0)
        }

        val btnShowLists: Button = view.findViewById(R.id.btnShowList)
        btnShowLists.setOnClickListener {
            startActivityForResult(Intent(fromActivity, ListActivity::class.java), 1)
        }
    }

    fun errorData(error: String?) {
        Snackbar.make(fromActivity.findViewById(android.R.id.content), "ss", Snackbar.LENGTH_LONG)
            .show()
    }
}