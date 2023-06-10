package com.politecnicomalaga.mymarketlist.controller.cAdapter.rvList

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
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
        val green = ContextCompat.getColor(fromActivity, android.R.color.holo_green_dark)
        val yellow = ContextCompat.getColor(fromActivity, R.color.colorAccent)
//        ContextCompat.getColor(fromActivity, R.color.colorPrimary)
        val myProduct: Lists = lists[position]
        holder.checkBox.isVisible = false

        holder.txtProduct.text = myProduct.sName.replace("_", " ", false)
        if (myProduct.bOnline) {
            holder.txtProduct.setBackgroundColor(green)
            holder.checkBox.setBackgroundColor(green)
        } else {
            holder.txtProduct.setBackgroundColor(yellow)
            holder.checkBox.setBackgroundColor(yellow)
        }
    }

    override fun getItemCount(): Int {
        return lists.size
    }

}