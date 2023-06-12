package com.politecnicomalaga.mymarketlist.controller.cAdapter

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.politecnicomalaga.mymarketlist.model.Catalogue
import com.politecnicomalaga.mymarketlist.view.vFragments.CatalogueFragment

class TabsVPAdapter(
    fm: FragmentManager, private val fromActivity: Activity,
    private val myCatalogue: ArrayList<Catalogue>
) :
    FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int {
        return myCatalogue.size
    }

    override fun getItem(position: Int): Fragment {
        return CatalogueFragment(fromActivity, myCatalogue[position].oProducts)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return myCatalogue[position].sType
    }

}