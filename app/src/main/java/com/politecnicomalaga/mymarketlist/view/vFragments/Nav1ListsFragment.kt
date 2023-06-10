package com.politecnicomalaga.mymarketlist.view.vFragments

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
import com.politecnicomalaga.mymarketlist.controller.MainController
import com.politecnicomalaga.mymarketlist.controller.cEntities.ServerData
import com.politecnicomalaga.mymarketlist.view.vActivities.CatalogueActivity
import com.politecnicomalaga.mymarketlist.view.vActivities.ListActivity
import com.politecnicomalaga.mymarketlist.view.vActivities.StatsActivity

class Nav1ListsFragment : Fragment {

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
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav1_lists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnMakeList: Button = view.findViewById(R.id.btnMakeList)
        btnMakeList.setOnClickListener {
            fromActivity.startActivityForResult(Intent(fromActivity, CatalogueActivity::class.java), 0)
        }

        val btnShowLists: Button = view.findViewById(R.id.btnShowList)
        btnShowLists.setOnClickListener {
            fromActivity.startActivityForResult(Intent(fromActivity, ListActivity::class.java), 1)
        }

        val btnStats: Button = view.findViewById(R.id.btnStats)
        btnStats.setOnClickListener {
//            ServerData(fromActivity).getServerLists()
        fromActivity.startActivityForResult(Intent(fromActivity, StatsActivity::class.java), 2)
        }

        val btnSuggest: Button = view.findViewById(R.id.btnSuggest)
        btnSuggest.setOnClickListener {
//            ServerData(fromActivity).setServerLists()
            MainController().showToast(fromActivity, R.string.in_maintenance)
        }
    }

}