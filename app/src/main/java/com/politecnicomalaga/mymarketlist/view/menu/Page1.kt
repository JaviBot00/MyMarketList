package com.politecnicomalaga.mymarketlist.view.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.politecnicomalaga.mymarketlist.MainActivity
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.http.SqlQuery
import com.politecnicomalaga.mymarketlist.view.selectProducts.SelectProductActivity

class Page1(fromActivity: MainActivity) : Fragment() {

    private val myContext: MainActivity

    init {
        myContext = fromActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_page1, container, false)

        val makeList: Button = view.findViewById(R.id.btnMakeList)
        makeList.setOnClickListener {
            startActivityForResult(Intent(myContext, SelectProductActivity::class.java), 2)
        }

        val showList: Button = view.findViewById(R.id.btnShowList)
        showList.setOnClickListener {
            val kk = SqlQuery().getProducts()
        }
        return view
    }

    fun errorData(error: String?) {
        Snackbar.make(myContext.findViewById(android.R.id.content), "ss", Snackbar.LENGTH_LONG)
            .show()

    }

}