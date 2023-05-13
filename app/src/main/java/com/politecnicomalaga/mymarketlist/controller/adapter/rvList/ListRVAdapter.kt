package com.politecnicomalaga.mymarketlist.controller.adapter.rvList

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.model.MyLists

class ListRVAdapter(
    private val fromActivity: Activity, private val myLists: ArrayList<MyLists>
) : RecyclerView.Adapter<ListRVHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListRVHolder {
        return ListRVHolder(
            LayoutInflater.from(fromActivity).inflate(R.layout.rv_items_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListRVHolder, position: Int) {
        val myProduct: MyLists = myLists[position]
        holder.checkBox.isEnabled = false

        holder.txtProduct.text = myProduct.name.replace("_", " ", false)
        holder.checkBox.isChecked = myProduct.online

    }

    override fun getItemCount(): Int {
        return myLists.size
    }

}