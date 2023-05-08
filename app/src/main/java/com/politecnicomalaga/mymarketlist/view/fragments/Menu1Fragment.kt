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
import com.politecnicomalaga.mymarketlist.view.activity.ProductsActivity

class Menu1Fragment() : Fragment() {

    private lateinit var fromActivity: Activity
    private var mState: String? = null

    constructor(fromActivity: Activity) : this() {
        this.fromActivity = fromActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            mState = savedInstanceState.getString("my_state")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val makeList: Button = view.findViewById(R.id.btnMakeList)
        makeList.setOnClickListener {
            startActivityForResult(Intent(fromActivity, ProductsActivity::class.java), 2)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("my_state", mState)
    }

    fun errorData(error: String?) {
        Snackbar.make(fromActivity.findViewById(android.R.id.content), "ss", Snackbar.LENGTH_LONG)
            .show()
    }
}