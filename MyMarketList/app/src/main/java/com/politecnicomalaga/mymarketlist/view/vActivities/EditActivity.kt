package com.politecnicomalaga.mymarketlist.view.vActivities

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.MainController
import com.politecnicomalaga.mymarketlist.controller.cAdapter.rvProducts.ProductsRVAdapter
import com.politecnicomalaga.mymarketlist.controller.cEntities.ServerData
import com.politecnicomalaga.mymarketlist.controller.cSQLite.ClientSQLite
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EditActivity : AppCompatActivity() {

    companion object {
        private var myEdit: EditActivity? = null

        fun getInstance(): EditActivity {
            if (myEdit == null) {
                myEdit = EditActivity()
            }
            return myEdit!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        MainController().setControllers(
            this@EditActivity, 0, ListActivity.list2edit.sName.replace("_", " ", false)
        )

        val textInputPrice: TextInputLayout = findViewById(R.id.txtFldPrice)
        val textInputDate: TextInputLayout = findViewById(R.id.txtFldDate)
        val fabSaveList: FloatingActionButton = findViewById(R.id.fabSaveList)

        val myRecyclerView: RecyclerView = this@EditActivity.findViewById(R.id.rvList)
        val myAdapter = ProductsRVAdapter(
            this@EditActivity, ClientSQLite(this@EditActivity).getProducts(ListActivity.list2edit)
        )
        myRecyclerView.layoutManager =
            LinearLayoutManager(this@EditActivity, LinearLayoutManager.VERTICAL, false)
        myRecyclerView.adapter = myAdapter

        textInputPrice.editText?.addTextChangedListener {
            if (it?.isNotEmpty() == true) {
                textInputPrice.error = null
            }
        }

        textInputDate.editText?.addTextChangedListener {
            if (it?.isNotEmpty() == true) {
                textInputDate.error = null
            }
        }

        textInputPrice.editText!!.setText(if (ListActivity.list2edit.nPrice == 0F) "" else ListActivity.list2edit.nPrice.toString())
        textInputDate.editText!!.setText(ListActivity.list2edit.dRealized)

        textInputDate.editText!!.setOnClickListener {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("es", "ES"))
            val dCreated = Calendar.getInstance()
            dCreated.time = simpleDateFormat.parse(ListActivity.list2edit.dCreated) as Date

            val today = MaterialDatePicker.todayInUtcMilliseconds()
            val compositeValidator = CompositeDateValidator.allOf(
                listOf(
                    DateValidatorPointForward.from(dCreated.timeInMillis),
                    DateValidatorPointBackward.before(today)
                )
            )
            val constraintsBuilder =
                CalendarConstraints.Builder().setStart(dCreated.timeInMillis).setEnd(today)
                    .setValidator(compositeValidator).build()

            val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date")
                .setSelection(today).setCalendarConstraints(constraintsBuilder).build()
            datePicker.show(supportFragmentManager, "tag")

            datePicker.addOnPositiveButtonClickListener {
                textInputDate.editText!!.setText(simpleDateFormat.format(Date(it)))
            }
        }

        fabSaveList.setOnClickListener {
            val regex = Regex("^\\d+(\\.\\d{1,2})?$")
            val validPrice = regex.matches(textInputPrice.editText!!.text)
            if (textInputPrice.editText!!.text.isNullOrEmpty() && !validPrice) {
                textInputPrice.error = resources.getString(R.string.set_price)
                return@setOnClickListener
            }
            if (textInputDate.editText!!.text.isNullOrEmpty()) {
                textInputDate.error = getString(R.string.set_date)
                return@setOnClickListener
            }
            ListActivity.list2edit.nPrice = (textInputPrice.editText!!.text.toString()).toFloat()
            ListActivity.list2edit.dRealized = textInputDate.editText!!.text.toString()
            MainController().showToast(this@EditActivity, R.string.in_maintenance)
            ClientSQLite(this@EditActivity).updateList(arrayListOf(ListActivity.list2edit), 0)
            if (MainController().isConnected(this@EditActivity)) {
                ServerData(this@EditActivity).updateServerList(ListActivity.list2edit)
            }
        }
    }

    fun endEdit(fromActivity: Activity){

    }
}