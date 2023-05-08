package com.politecnicomalaga.mymarketlist.controller.adapter

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.politecnicomalaga.mymarketlist.R

class ProductsRVHolder(itemView: View, productsRVAdapter: ProductsRVAdapter) :
    RecyclerView.ViewHolder(itemView) {
    //    var imgProfile: ImageView = itemView.findViewById(R.id.imgProfile)
    var txtProduct: TextView = itemView.findViewById(R.id.txtProduct)

    //    var txtBirthday: TextView = itemView.findViewById(R.id.txtBirthday)
//    var btnEdit: Button = itemView.findViewById(R.id.btnEdit)
//    var btnDel: Button = itemView.findViewById(R.id.btnDel)
//    var cardFeatures: CardView = itemView.findViewById(R.id.cardFeatures)
//    var myAdapter: com.politecnicomalaga.mymarketlist.controller.adapter.ProductsRVAdapter = productsRVAdapter
    var checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
}