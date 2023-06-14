package com.politecnicomalaga.mymarketlist.view.vActivities

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
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
        val simpleDateFormatEN = SimpleDateFormat("yyyy-MM-dd", Locale("es", "ES"))
        val simpleDateFormatES = SimpleDateFormat("dd-MM-yyyy", Locale("es", "ES"))

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
        textInputDate.editText!!.setText(
            if (ListActivity.list2edit.dRealized.isNullOrEmpty()) "" else simpleDateFormatES.format(
                simpleDateFormatEN.parse(ListActivity.list2edit.dRealized!!)!!
            )
        )

        if (ListActivity.list2edit.nPrice > 0F) {
            textInputPrice.editText!!.isEnabled = false
            fabSaveList.isEnabled = false
        }
        if (ListActivity.list2edit.dRealized!!.isNotEmpty()) {
            textInputDate.editText!!.isEnabled = false
            fabSaveList.isEnabled = false
        }

        textInputDate.editText!!.setOnClickListener {
            val dCreated = Calendar.getInstance()
            dCreated.time = simpleDateFormatEN.parse(ListActivity.list2edit.dCreated) as Date

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
                textInputDate.editText!!.setText(simpleDateFormatES.format(Date(it)))
                ListActivity.list2edit.dRealized = simpleDateFormatEN.format(Date(it))
            }
        }

        fabSaveList.setOnClickListener {
            val regex = Regex("^\\d{0,3}(\\.\\d{1,2})?$")
            val validPrice = regex.matches(textInputPrice.editText!!.text)
            if (textInputPrice.editText!!.text.isNullOrEmpty() || !validPrice) {
                textInputPrice.error = resources.getString(R.string.set_valid_price)
                return@setOnClickListener
            }
            if (textInputDate.editText!!.text.isNullOrEmpty()) {
                textInputDate.error = getString(R.string.set_date)
                return@setOnClickListener
            }
            ListActivity.list2edit.nPrice = (textInputPrice.editText!!.text.toString()).toFloat()
            ClientSQLite(this@EditActivity).updateList(arrayListOf(ListActivity.list2edit), 0)
            if (MainController().isConnected(this@EditActivity)) {
                ServerData(this@EditActivity).updateServerLists()
            }
        }
    }

    fun endEdit(fromActivity: Activity) {
        fromActivity.setResult(RESULT_OK)
        fromActivity.finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (this@EditActivity.findViewById<TextInputLayout>(R.id.txtFldPrice).editText!!.isEnabled) {
                    MainController().exitDialog(this@EditActivity)
                } else {
                    this@EditActivity.finish()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}