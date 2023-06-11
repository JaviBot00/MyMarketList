package com.politecnicomalaga.mymarketlist.view.vFragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.cAdapter.rvProducts.ProductsRVAdapter
import com.politecnicomalaga.mymarketlist.model.Product


class ProductListFragment(private val fromActivity: Activity, private val myProducts: ArrayList<Product>) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rv_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myRecyclerView: RecyclerView = view.findViewById(R.id.rvList)
        val myAdapter = ProductsRVAdapter(fromActivity, myProducts)
        myRecyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        myRecyclerView.adapter = myAdapter
    }
}
