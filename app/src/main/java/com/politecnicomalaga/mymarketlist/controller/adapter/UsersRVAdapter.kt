package com.politecnicomalaga.mymarketlist.controller.adapter

import UsersRVHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.politecnicomalaga.mymarketlist.R


class UsersRVAdapter(private val fromActivity: View, private val myProductsList: Set<String>) :
    RecyclerView.Adapter<UsersRVHolder>() {

    //    private var myUsers: ArrayList<>
//    var myContext: Context
//    private lateinit var interEditUser: EditUser
//    private lateinit var interDelUser: DelUser
//
//    interface EditUser {
//        fun editUser(user: UserFeatures)
//    }
//
//    interface DelUser {
//        fun delUser(context: Context, user: UserFeatures, position: Int)
//    }
//
//    init {
//        this.myUsers = myUsersList
//        myContext = fromActivity
//    }
//
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersRVHolder {
        return UsersRVHolder(
            LayoutInflater.from(fromActivity.context)
                .inflate(R.layout.rv_items_list, parent, false),
            this@UsersRVAdapter
        )
    }

    //
    override fun onBindViewHolder(holder: UsersRVHolder, position: Int) {
        val myProduct: String = myProductsList.elementAt(position)
//        val myOptions: Array<String> = MySQLiteManager.TUG_ROLES

//        if (myUser.getImgProfile() != null) {
//            holder.imgProfile.setImageBitmap(myUser.getImgProfile())
//        }
        holder.txtProduct.text = myProduct
//        holder.txtBirthday.text = myUser.getBirthday()

//        holder.btnEdit.setOnClickListener {
//            if (interEditUser != null) {
//                interEditUser.editUser(myUser)
//            }
//        }
//        holder.btnDel.setOnClickListener {
//            interDelUser.delUser(myContext, myUser, position)
//        }
//
//        when (myUser.getUserRol()) {
//            myOptions[0] -> holder.cardFeatures.setCardBackgroundColor(Color.DKGRAY)
//            myOptions[1] -> holder.cardFeatures.setCardBackgroundColor(Color.CYAN)
//            myOptions[2] -> holder.cardFeatures.setCardBackgroundColor(Color.parseColor("#FFBB86FC"))
//        }
    }

    override fun getItemCount(): Int {
        return myProductsList.size
    }
//
//    fun setMyUsers(myUsers: ArrayList<UserFeatures>) {
//        this.myUsers = myUsers
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