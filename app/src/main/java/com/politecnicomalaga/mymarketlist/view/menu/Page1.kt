package com.politecnicomalaga.mymarketlist.view.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.politecnicomalaga.mymarketlist.MainActivity
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.view.selectProducts.SelectProductActivity

class Page1(fromActivity: MainActivity) : Fragment() {

    private val myContext: MainActivity

    init {
        myContext = fromActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_page1, container, false)

        val listButton: Button = view.findViewById(R.id.btnMakeList)
        listButton.setOnClickListener {
            startActivityForResult(Intent(myContext, SelectProductActivity::class.java), 2)
        }
        // Inflate the layout for this fragment
        return view
    }

}