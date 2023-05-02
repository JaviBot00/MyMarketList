package com.politecnicomalaga.mymarketlist.controller.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.politecnicomalaga.mymarketlist.view.productsMenu.ProductList

class ViewPagerAdapter(fm: FragmentManager, private val myProducts: Map<String, Set<String>>) :
    FragmentStatePagerAdapter(fm) {


    override fun getCount(): Int {
        return myProducts.size
    }

    override fun getItem(position: Int): Fragment {
        return ProductList(myProducts[getPageTitle(position)]!!)
    }

    override fun getPageTitle(position: Int): CharSequence {
        val tables = myProducts.keys.toTypedArray()
        return tables[position]
    }

}