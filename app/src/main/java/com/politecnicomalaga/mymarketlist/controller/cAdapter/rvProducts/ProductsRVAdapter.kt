package com.politecnicomalaga.mymarketlist.controller.cAdapter.rvProducts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.cSQLite.ClientSQLite
import com.politecnicomalaga.mymarketlist.model.Product

class ProductsRVAdapter(
    private val fromActivity: View, private val myProductsList: ArrayList<Product>
) : RecyclerView.Adapter<ProductsRVHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsRVHolder {
        return ProductsRVHolder(
            LayoutInflater.from(fromActivity.context).inflate(R.layout.rv_items_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductsRVHolder, position: Int) {
        val myProduct: Product = myProductsList[position]

        holder.txtProduct.text = myProduct.sName

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

    override fun getItemCount(): Int {
        return myProductsList.size
    }

}