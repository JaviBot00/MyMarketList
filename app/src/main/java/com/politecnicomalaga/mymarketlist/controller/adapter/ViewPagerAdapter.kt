package com.politecnicomalaga.mymarketlist.controller.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.politecnicomalaga.mymarketlist.view.selectProducts.Frutas
import com.politecnicomalaga.mymarketlist.view.selectProducts.Verduras

class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int {
        return 6
    }

    override fun getItem(position: Int): Fragment {
        val fragment: Fragment = if ((position % 2) == 0) {
            Frutas()
        } else {
            Verduras()
        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "OBJECT ${(position + 1)}"
    }
}