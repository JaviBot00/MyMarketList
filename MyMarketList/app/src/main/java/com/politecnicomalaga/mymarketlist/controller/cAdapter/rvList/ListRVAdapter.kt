package com.politecnicomalaga.mymarketlist.controller.cAdapter.rvList

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.model.List
import java.text.SimpleDateFormat
import java.util.Locale

class ListRVAdapter(
    private val fromActivity: Activity, private val lists: ArrayList<List>
) : RecyclerView.Adapter<ListRVHolder>() {

    private val simpleDateFormatEN = SimpleDateFormat("yyyy-MM-dd", Locale("es", "ES"))
    private val simpleDateFormatES = SimpleDateFormat("dd-MM-yyyy", Locale("es", "ES"))
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
        val red = ContextCompat.getColor(fromActivity, android.R.color.holo_red_dark)
//        ContextCompat.getColor(fromActivity, R.color.colorPrimary)
        val myList: List = lists[position]

        holder.txtLists.text = myList.sName.replace("_", " ", false)
        holder.txtPrice.text = myList.nPrice.toString()
        holder.txtDate.text =
            if (myList.dRealized.isNullOrEmpty()) "" else simpleDateFormatES.format(
                simpleDateFormatEN.parse(myList.dRealized!!)!!
            )



        if (myList.bOnline) {
            holder.txtLists.setTextColor(green)
            holder.txtPrice.setTextColor(green)
            holder.txtDate.setTextColor(green)
        } else {
            holder.txtLists.setTextColor(red)
            holder.txtPrice.setTextColor(red)
            holder.txtDate.setTextColor(red)
        }

        holder.txtLists.setOnClickListener {
            if (interEditList != null) {
                interEditList.editList(fromActivity, myList)
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