package com.politecnicomalaga.mymarketlist.controller.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.entities.ProductList
import com.politecnicomalaga.mymarketlist.model.Product


class ProductsRVAdapter(
    private val fromActivity: View,
    private val myProductsList: ArrayList<Product>
) :
    RecyclerView.Adapter<ProductsRVHolder>() {

    //    private var myUsers: ArrayList<>
//    var myContext: Context
//    private lateinit var interEditUser: EditUser
//    private lateinit var interDelUser: DelUser
//
//    interface EditUser {
//        fun editUser(user: com.politecnicomalaga.mymarketlist.model.UserFeatures)
//    }
//
//    interface DelUser {
//        fun delUser(context: Context, user: com.politecnicomalaga.mymarketlist.model.UserFeatures, position: Int)
//    }
//
//    init {
//        this.myUsers = myUsersList
//        myContext = fromActivity
//    }
//
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsRVHolder {
        return ProductsRVHolder(
            LayoutInflater.from(fromActivity.context)
                .inflate(R.layout.rv_items_list, parent, false),
            this@ProductsRVAdapter
        )
    }

    override fun onBindViewHolder(holder: ProductsRVHolder, position: Int) {
        val myProduct: Product = myProductsList[position]

        holder.txtProduct.text = myProduct.name

        holder.checkBox.isChecked = myProduct.isAdd
//        holder.checkBox.jumpDrawablesToCurrentState()

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                ProductList.myList.add(myProduct)
            } else if (!isChecked && ProductList.myList.contains(myProduct)) {
                ProductList.myList.remove(myProduct)
            }
        }

        if (ProductList.myList.contains(myProduct)) {
            holder.checkBox.isChecked = true
        } else if (!ProductList.myList.contains(myProduct)) {
            holder.checkBox.isChecked = false
        }
    }

    override fun getItemCount(): Int {
        return myProductsList.size
    }
//
//    fun clearMyItems() {
//        notifyDataSetChanged()
//    }
//
//    fun setInterEditUser(interEditUser: EditUser) {
//        this.interEditUser = interEditUser
//    }
//
//    fun setInterDelUser(interDelUser: DelUser) {
//        this.interDelUser = interDelUser
//    }

}