package com.politecnicomalaga.mymarketlist.view.vFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.politecnicomalaga.mymarketlist.R

class Nav2DetailsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav2_details, container, false)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val makeList: Button = view.findViewById(R.id.btnMakeList)
//        makeList.setOnClickListener {
//            startActivityForResult(Intent(fromActivity, CatalogueActivity::class.java), 2)
//        }
//    }

}