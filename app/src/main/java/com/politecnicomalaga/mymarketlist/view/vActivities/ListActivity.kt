package com.politecnicomalaga.mymarketlist.view.vActivities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.MainController
import com.politecnicomalaga.mymarketlist.controller.cAdapter.rvList.ListRVAdapter
import com.politecnicomalaga.mymarketlist.controller.cEntities.ServerData
import com.politecnicomalaga.mymarketlist.controller.cSQLite.ClientSQLite
import com.politecnicomalaga.mymarketlist.model.List

class ListActivity : AppCompatActivity() {

    companion object {
        var list2edit = List()
        private var myListAct: ListActivity? = null

        fun getInstance(): ListActivity {
            if (myListAct == null) {
                myListAct = ListActivity()
            }
            return myListAct!!
        }
    }

    val EDIT_REQUEST = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        MainController().setControllers(this@ListActivity, R.string.my_list)

        this@ListActivity.setRecycler(this@ListActivity)
        this@ListActivity.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh).setOnRefreshListener {
            if (MainController().isConnected(this@ListActivity)) {
                if (ClientSQLite(this@ListActivity).getLists().isNotEmpty()) {
                    ServerData(this@ListActivity).setServerLists()
                } else {
                    ServerData(this@ListActivity).getServerLists()
                }
            }
        }

    }

    fun setRecycler(fromActivity: Activity) {
        val myRecyclerView: RecyclerView = fromActivity.findViewById(R.id.rvList)

        val myAdapter = ListRVAdapter(fromActivity, ClientSQLite(fromActivity).getLists())
        myRecyclerView.layoutManager =
            LinearLayoutManager(fromActivity, LinearLayoutManager.VERTICAL, false)
        myRecyclerView.adapter = myAdapter

        myAdapter.setInterEditList(object : ListRVAdapter.EditList {
            override fun editList(fromActivity: Activity, list: List) {
                val myIntent = Intent(fromActivity, EditActivity::class.java)
                list2edit = list
                fromActivity.startActivityForResult(myIntent, EDIT_REQUEST)
            }
        })
    }

    fun endRefresh(fromActivity: Activity) {
        if (fromActivity is ListActivity) {
            this@ListActivity.setRecycler(fromActivity)
            fromActivity.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh).isRefreshing = false
        }
    }

}