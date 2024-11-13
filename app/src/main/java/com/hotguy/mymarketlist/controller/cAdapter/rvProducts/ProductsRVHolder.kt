package com.hotguy.mymarketlist.controller.cAdapter.rvProducts

import android.app.Activity
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.hotguy.mymarketlist.R

class ProductsRVHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = RvCatalogueBinding.bind(itemView)


    //    var txtItems: TextView = itemView.findViewById(R.id.txtItems)
    var txtItems: TextView = binding.txtItems
    var checkBox: CheckBox = binding.checkBox


    fun kk(fromActivity: Activity, myProduct: Product) {

        txtItems.text = myProduct.sName

        when (fromActivity) {
            is CatalogueActivity -> {
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        ClientSQLite.myProductsList.add(myProduct)
                    } else if (!isChecked && ClientSQLite.myProductsList.contains(myProduct)) {
                        ClientSQLite.myProductsList.remove(myProduct)
                    }
                }

                if (ClientSQLite.myProductsList.contains(myProduct)) {
                    checkBox.isChecked = true
                } else if (!ClientSQLite.myProductsList.contains(myProduct)) {
                    checkBox.isChecked = false
                }
            }

            is EditActivity -> {
                checkBox.isEnabled = false
                checkBox.isVisible = false
            }
        }
    }

    // Hasta aqui

}