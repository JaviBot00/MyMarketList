package com.politecnicomalaga.mymarketlist.controller.cAdapter.rvList

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.model.Lists

class ListRVAdapter(
    private val fromActivity: Activity, private val lists: ArrayList<Lists>
) : RecyclerView.Adapter<ListRVHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListRVHolder {
        return ListRVHolder(
            LayoutInflater.from(fromActivity).inflate(R.layout.rv_items_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListRVHolder, position: Int) {
        val myProduct: Lists = lists[position]

        holder.checkBox.isEnabled = false
        holder.txtProduct.text = myProduct.sName.replace("_", " ", false)
        holder.checkBox.isChecked = myProduct.bOnline
    }

    override fun getItemCount(): Int {
        return lists.size
    }

}