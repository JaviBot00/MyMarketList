package com.politecnicomalaga.mymarketlist.controller.cAdapter.rvProducts

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.cSQLite.ClientSQLite
import com.politecnicomalaga.mymarketlist.model.Product
import com.politecnicomalaga.mymarketlist.view.vActivities.CatalogueActivity
import com.politecnicomalaga.mymarketlist.view.vActivities.EditActivity

class ProductsRVAdapter(
    private val fromActivity: Activity, private val myProductsList: ArrayList<Product>
) : RecyclerView.Adapter<ProductsRVHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsRVHolder {
        return ProductsRVHolder(
            LayoutInflater.from(fromActivity).inflate(R.layout.rv_catalogue, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductsRVHolder, position: Int) {
        val myProduct: Product = myProductsList[position]

        holder.txtItems.text = myProduct.sName

        when (fromActivity) {
            is CatalogueActivity -> {
                holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        ClientSQLite.myProductsList.add(myProduct)
                    } else if (!isChecked && ClientSQLite.myProductsList.contains(myProduct)) {
                        ClientSQLite.myProductsList.remove(myProduct)
                    }
                }

                if (ClientSQLite.myProductsList.contains(myProduct)) {
                    holder.checkBox.isChecked = true
                } else if (!ClientSQLite.myProductsList.contains(myProduct)) {
                    holder.checkBox.isChecked = false
                }
            }

            is EditActivity -> {
                holder.checkBox.isEnabled = false
                holder.checkBox.isVisible = false
            }
        }

    }

    override fun getItemCount(): Int {
        return myProductsList.size
    }

}