package com.politecnicomalaga.mymarketlist.controller.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.politecnicomalaga.mymarketlist.model.ClassifiedProducts
import com.politecnicomalaga.mymarketlist.view.fragments.ProductListFragment

class TabsVPAdapter(
    fm: FragmentManager,
    private val myProducts: ArrayList<ClassifiedProducts>
) :
    FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int {
        return myProducts.size
    }

    override fun getItem(position: Int): Fragment {
        return ProductListFragment(myProducts[position].products)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return myProducts[position].category
    }

}