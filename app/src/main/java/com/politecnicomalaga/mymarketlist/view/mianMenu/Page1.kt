package com.politecnicomalaga.mymarketlist.view.mianMenu

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
import com.politecnicomalaga.mymarketlist.view.productsMenu.ProductsActivity

class Page1(private val fromActivity: Activity) : Fragment() {
//    constructor(fromActivity: MainActivity) : this() {
//        this.myContext = fromActivity
//    }
//
//    private lateinit var myContext: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_page1, container, false)

        val makeList: Button = view.findViewById(R.id.btnMakeList)
        makeList.setOnClickListener {
            startActivityForResult(Intent(fromActivity, ProductsActivity::class.java), 2)
        }

//        val showList: Button = view.findViewById(R.id.btnShowList)
//        showList.setOnClickListener {
//            CloudProductsTables(fromActivity).getCloudProductTables()
//
//            println(fromActivity.getString(R.string.host_unreachable))
//        }


        return view
    }

    fun errorData(error: String?) {
        Snackbar.make(fromActivity.findViewById(android.R.id.content), "ss", Snackbar.LENGTH_LONG)
            .show()

    }

}