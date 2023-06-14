package com.politecnicomalaga.mymarketlist.view.vActivities

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.MainController
import com.politecnicomalaga.mymarketlist.controller.cSQLite.ClientSQLite
import com.politecnicomalaga.mymarketlist.model.List

class StatsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var myLists: ArrayList<List>
    private lateinit var spYears: Spinner
    private lateinit var spMonths: Spinner
    private var years = listOf<String>()
    private var filteredList: ArrayList<List> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        MainController().setControllers(this@StatsActivity, R.string.stats, "")
        myLists = ClientSQLite(this@StatsActivity).getLists()

        spYears = findViewById(R.id.spYears)
        spMonths = findViewById(R.id.spMonths)

        years = getUniqueYears(myLists)
        val yearsAdapter =
            ArrayAdapter(this@StatsActivity, android.R.layout.simple_spinner_item, years)
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spYears.onItemSelectedListener = this@StatsActivity
        spYears.adapter = yearsAdapter
        spYears.setSelection(0)

        spMonths.onItemSelectedListener = this@StatsActivity
        spMonths.isEnabled = false

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent!!.id) {
            R.id.spYears -> {
                val selectedYear = years[position]
                if (selectedYear.isNotEmpty()) {
                    val months = getMonthsForYear(myLists, selectedYear)
                    val monthsAdapter = ArrayAdapter(
                        this@StatsActivity, android.R.layout.simple_spinner_item, months
                    )
                    monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spMonths.isEnabled = true
                    spMonths.adapter = monthsAdapter
                } else {
                    spMonths.isEnabled = false
                    spMonths.adapter = null
                }
            }

            R.id.spMonths -> {
                val selectedYear = years[spYears.selectedItemPosition]
                val selectedMonth = spMonths.selectedItem as? String
                if (selectedYear.isNotEmpty() && !selectedMonth.isNullOrEmpty()) {
                    filteredList = myLists.filter {
                        it.dRealized!!.startsWith(selectedYear) && it.dRealized!!.substring(
                            5, 7
                        ) == selectedMonth
                    } as ArrayList<List>
                } else {
                    filteredList.clear()
                }
                setGraphic()
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun getUniqueYears(myLists: ArrayList<List>): MutableList<String> {
        val years =
            myLists.asSequence().filter { it.nPrice > 0 }.map { it.dRealized!!.substring(0, 4) }
                .distinct().sortedDescending().toMutableList()

        years.add(0, "")

        return years
    }

    private fun getMonthsForYear(
        myLists: ArrayList<List>, year: String
    ): ArrayList<String> {
//        val months = myLists.filter { it.dRealized!!.startsWith(year) }
//            .map { getMonthName(it.dRealized!!.substring(5, 7)) }.distinct()
        val months = myLists.filter { it.dRealized!!.startsWith(year) }
            .map { it.dRealized!!.substring(5, 7) }.distinct()
        return months as ArrayList<String>
    }

    private fun getMonthName(month: String): String {
        val monthNames = arrayOf(
            "Enero",
            "Febrero",
            "Marzo",
            "Abril",
            "Mayo",
            "Junio",
            "Julio",
            "Agosto",
            "Septiembre",
            "Octubre",
            "Noviembre",
            "Diciembre"
        )
        val monthIndex = month.toIntOrNull()?.let { it - 1 } ?: 0
        return monthNames.getOrNull(monthIndex) ?: ""
    }

    private fun setGraphic() {
        val gvSensor: GraphView = findViewById(R.id.gvGraphic)

        val series = LineGraphSeries<DataPoint>()
        filteredList.sortedBy { it.dRealized!!.substring(8, 10) }.forEach {
            if (it.nPrice > 0) {
                series.appendData(
                    DataPoint(
                        it.dRealized!!.substring(8, 10).toDouble(), it.nPrice.toDouble()
                    ), true, filteredList.size
                )
            }
        }

        series.thickness = 5
        series.color = Color.parseColor("#FFFF3C3C")
        series.backgroundColor = Color.parseColor("#40CC7777")
        series.isDrawDataPoints = true
        series.isDrawBackground = true
        series.dataPointsRadius = 10f
        series.setAnimated(true)

        series.title = resources.getString(R.string.bills)
        gvSensor.legendRenderer.isVisible = true
        gvSensor.legendRenderer.align = LegendRenderer.LegendAlign.TOP
        gvSensor.legendRenderer.backgroundColor = R.color.colorBackground

//        gvSensor.legendRenderer.width = 90
//        gvSensor.legendRenderer.margin = 25
//        gvSensor.legendRenderer.padding = 26
        gvSensor.legendRenderer.spacing = 15
        gvSensor.legendRenderer.textSize = 32f
        gvSensor.legendRenderer.textColor = Color.WHITE

        gvSensor.viewport.isYAxisBoundsManual = true
        gvSensor.viewport.setMinY(0.0)
        gvSensor.viewport.setMaxY(filteredList.maxOfOrNull { it.nPrice.toDouble() } ?: 0.0)

        gvSensor.viewport.isXAxisBoundsManual = true
        gvSensor.viewport.setMinX(1.0)
        gvSensor.viewport.setMaxX(30.0)
//
//        val minX = dataPoints.minOfOrNull { it.x } ?: 1.0
//        val maxX = dataPoints.maxOfOrNull { it.x } ?: 30.0
//        gvSensor.viewport.setMinX(minX)
//        gvSensor.viewport.setMaxX(maxX)


//        gvSensor.viewport.isScrollable = true
        gvSensor.viewport.isScalable = true
        gvSensor.viewport.setDrawBorder(true)
        gvSensor.viewport.borderColor = Color.BLACK

        gvSensor.removeAllSeries()
        gvSensor.addSeries(series)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this@StatsActivity.setResult(RESULT_CANCELED)
                this@StatsActivity.finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
