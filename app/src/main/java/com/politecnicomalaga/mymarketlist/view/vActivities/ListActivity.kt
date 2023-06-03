package com.politecnicomalaga.mymarketlist.view.vActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.MainController
import com.politecnicomalaga.mymarketlist.controller.cAdapter.rvList.ListRVAdapter
import com.politecnicomalaga.mymarketlist.controller.cSQLite.ClientSQLite

class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        MainController().setAppBar(this@ListActivity, resources.getString(R.string.my_list))
        val myRecyclerView: RecyclerView = this@ListActivity.findViewById(R.id.rvList)

        val myAdapter =
            ListRVAdapter(this@ListActivity, ClientSQLite(this@ListActivity).getLists())
        myRecyclerView.layoutManager =
            LinearLayoutManager(this@ListActivity, LinearLayoutManager.VERTICAL, false)
        myRecyclerView.adapter = myAdapter

    }
}