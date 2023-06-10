package com.politecnicomalaga.mymarketlist.view.vActivities

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
//import com.jjoe64.graphview.GraphView
//import com.jjoe64.graphview.LegendRenderer
//import com.jjoe64.graphview.series.DataPoint
//import com.jjoe64.graphview.series.LineGraphSeries
import com.politecnicomalaga.mymarketlist.R
import kotlin.math.atan
import kotlin.math.cos


class StatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        val textView: TextView = findViewById(R.id.section_label)
        val gvSensor: GraphView = findViewById(R.id.gvGraphic)

        val miArray = ArrayList<DataPoint>()


        var x = 0.0
        var y: Double


        for (i in 0..239) {
            x += 0.1
            y = cos(x) * 3 + atan(x) * 50

            val newDP = DataPoint(x, y)
            miArray.add(newDP)

        }

        val series = LineGraphSeries(miArray.toTypedArray())

        series.thickness = 5 // points weight
        series.color = Color.argb(255, 255, 60, 60) // points color
        series.backgroundColor = Color.argb(100, 204, 119, 119) // area color
        series.isDrawDataPoints = false // points
        series.isDrawBackground = true // area

        // legend
        series.title = "Gastos %"
        gvSensor.legendRenderer.isVisible = true
        gvSensor.legendRenderer.align = LegendRenderer.LegendAlign.TOP
        gvSensor.legendRenderer.backgroundColor = R.color.colorBackground


        gvSensor.titleColor = Color.WHITE

        gvSensor.titleTextSize = 25f


        // set manual X bounds
        gvSensor.viewport.isYAxisBoundsManual = true
        gvSensor.viewport.setMinY(0.0)
        gvSensor.viewport.setMaxY(150.0)

        gvSensor.viewport.isXAxisBoundsManual = true
        gvSensor.viewport.setMinX(1.0)
        gvSensor.viewport.setMaxX(12.0)

        gvSensor.viewport.borderColor = Color.BLUE

        gvSensor.removeAllSeries()
        gvSensor.addSeries(series)

    }
}