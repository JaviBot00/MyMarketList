package com.politecnicomalaga.mymarketlist.view.productsMenu

import com.politecnicomalaga.mymarketlist.controller.adapter.UsersRVAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.politecnicomalaga.mymarketlist.R


class ProductList(private val myProducts: Set<String>) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_rv_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myRecyclerView: RecyclerView = view.findViewById(R.id.rvList)
        val myAdapter = UsersRVAdapter(view, myProducts)
//        cargarVista()
        myRecyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        myRecyclerView.adapter = myAdapter
        myAdapter.notifyDataSetChanged()
    }

//    private fun cargarVista() {
//        myProducts.clear()
//        val mySQLite = MySQLiteManager(this)
//        mySQLite.setWritable()
//        mySQLite.setReadable()
//        val myCursor = mySQLite.getUsers()
//        while (myCursor.moveToNext()) {
//            val auxUserName = myCursor.getString(Integer.parseInt(MySQLiteManager.TUsers_USER[1]))
//            val auxPassWord = myCursor.getString(Integer.parseInt(MySQLiteManager.TUsers_PASSWORD[1]))
//            val auxBirthDay = myCursor.getString(
//                Integer.parseInt(MySQLiteManager.TUsers_BIRTHDATE[1])
//            )
//            val auxImgProfile =
//                getTheImage(myCursor.getBlob(Integer.parseInt(MySQLiteManager.TUsers_IMGPROFILE[1])))
//            val auxUserRol = myCursor.getString(
//                Integer.parseInt(MySQLiteManager.TUsers_ROLE[1])
//            )
//            val auxUser = UserFeatures(
//                auxUserName,
//                auxPassWord,
//                auxBirthDay,
//                auxImgProfile,
//                auxUserRol
//            )
//            myUsers.add(auxUser)
//        }
//        myAdapter.setMyUsers(myUsers)
//    }

}
