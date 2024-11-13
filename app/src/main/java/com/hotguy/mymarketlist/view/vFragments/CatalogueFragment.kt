package com.hotguy.mymarketlist.view.vFragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hotguy.mymarketlist.controller.cAdapter.rvProducts.ProductsRVAdapter
import com.hotguy.mymarketlist.model.Product


class CatalogueFragment(
    private val fromActivity: Activity,
    private val myProducts: ArrayList<Product>
) : Fragment() {

    private var _binding: FragmentRvCatalogueBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRvCatalogueBinding.inflate(inflater, container , false)
        return binding.root
//        return inflater.inflate(R.layout.fragment_rv_catalogue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val myRecyclerView: RecyclerView = view.findViewById(R.id.rvList)
        val myRecyclerView: RecyclerView = binding.rvList
        val myAdapter = ProductsRVAdapter(fromActivity, myProducts)
        myRecyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        myRecyclerView.adapter = myAdapter
    }
}
