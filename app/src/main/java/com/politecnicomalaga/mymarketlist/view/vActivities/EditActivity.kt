package com.politecnicomalaga.mymarketlist.view.vActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.MainController
import com.politecnicomalaga.mymarketlist.controller.cAdapter.rvList.ListRVAdapter
import com.politecnicomalaga.mymarketlist.controller.cAdapter.rvProducts.ProductsRVAdapter
import com.politecnicomalaga.mymarketlist.controller.cSQLite.ClientSQLite
import com.politecnicomalaga.mymarketlist.model.List

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        MainController().setControllers(this@EditActivity, R.string.in_maintenance)

        val myRecyclerView: RecyclerView = this@EditActivity.findViewById(R.id.rvList)

        val myAdapter = ProductsRVAdapter(this@EditActivity, ClientSQLite(this@EditActivity).getProducts(ListActivity.list2edit))
        myRecyclerView.layoutManager =
            LinearLayoutManager(this@EditActivity, LinearLayoutManager.VERTICAL, false)
        myRecyclerView.adapter = myAdapter

    }
}