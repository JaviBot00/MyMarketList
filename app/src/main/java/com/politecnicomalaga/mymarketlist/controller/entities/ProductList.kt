package com.politecnicomalaga.mymarketlist.controller.entities

import ClientSQLite
import MarketListSQLite
import android.app.Activity
import android.database.Cursor
import com.politecnicomalaga.mymarketlist.model.ClassifiedProducts
import com.politecnicomalaga.mymarketlist.model.MyLists
import com.politecnicomalaga.mymarketlist.model.Product


class ProductList {
    private var myProducts = arrayListOf(ClassifiedProducts())
    private var myLists = arrayListOf(MyLists())

    companion object {
        val myList = sortedSetOf<Product>()
    }

    private fun loadProducts(fromActivity: Activity) {
        myProducts.clear()
        val mySQLite = MarketListSQLite(fromActivity)
        mySQLite.setWritable()
        val tc: Cursor = mySQLite.getTables()
        while (tc.moveToNext()) {
            val type = ClassifiedProducts()
            type.category = tc.getString(Integer.parseInt(MarketListSQLite.TCategories_TYPE[1]))
            val pc: Cursor = mySQLite.getProducts(type.category)
            while (pc.moveToNext()) {
                type.products += Product(pc.getString(Integer.parseInt(MarketListSQLite.TProduct_ES[1])))
            }
            myProducts.add(type)
        }
        mySQLite.getDb().close()
    }

    fun getMyProducts(fromActivity: Activity): ArrayList<ClassifiedProducts> {
        loadProducts(fromActivity)
        return myProducts
    }

    private fun loadLists(fromActivity: Activity) {
        myLists.clear()
//        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("es","ES"))
        val mySQLite = ClientSQLite(fromActivity)
        mySQLite.setWritable()
        val lc: Cursor = mySQLite.getLists()
        while (lc.moveToNext()) {
            val list = MyLists()
            list.name = lc.getString(Integer.parseInt(ClientSQLite.TList_NAME[1]))
            list.dCreated = lc.getString(Integer.parseInt(ClientSQLite.TList_DCREATED[1]))
            list.dRealized = lc.getString(Integer.parseInt(ClientSQLite.TList_DREALIZED[1]))
            list.price = lc.getFloat(Integer.parseInt(ClientSQLite.TList_Price[1]))
            list.online = (lc.getInt(Integer.parseInt(ClientSQLite.TList_OnLine[1])) == 1)
            myLists.add(list)
        }
        mySQLite.getDb().close()
    }

    fun getMyLists(fromActivity: Activity): ArrayList<MyLists> {
        loadLists(fromActivity)
        return myLists
    }
}