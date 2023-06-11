package com.politecnicomalaga.mymarketlist.controller.cAdapter.rvList

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.model.List

class ListRVAdapter(
    private val fromActivity: Activity, private val lists: ArrayList<List>
) : RecyclerView.Adapter<ListRVHolder>() {

    private lateinit var interEditList: EditList

    interface EditList {
        fun editList(fromActivity: Activity, list: List)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListRVHolder {
        return ListRVHolder(
            LayoutInflater.from(fromActivity).inflate(R.layout.rv_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListRVHolder, position: Int) {
        val green = ContextCompat.getColor(fromActivity, android.R.color.holo_green_dark)
        val yellow = ContextCompat.getColor(fromActivity, android.R.color.holo_red_dark)
//        ContextCompat.getColor(fromActivity, R.color.colorPrimary)
        val myList: List = lists[position]

        holder.txtLists.text = myList.sName.replace("_", " ", false)
        holder.txtPrice.text = myList.nPrice.toString()

        if (myList.bOnline) {
            holder.txtLists.setTextColor(green)
            holder.txtPrice.setTextColor(green)
        } else {
            holder.txtLists.setTextColor(yellow)
            holder.txtPrice.setTextColor(yellow)
        }

        holder.txtLists.setOnClickListener {
            if (interEditList != null){
                interEditList.editList(fromActivity ,myList)
            }
        }
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    fun setInterEditList(interEditList: EditList) {
        this.interEditList = interEditList
    }
}