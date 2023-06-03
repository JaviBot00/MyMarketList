package com.politecnicomalaga.mymarketlist.controller.cAdapter.rvList

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.politecnicomalaga.mymarketlist.R

class ListRVHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var txtProduct: TextView = itemView.findViewById(R.id.txtItems)
    var checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
}