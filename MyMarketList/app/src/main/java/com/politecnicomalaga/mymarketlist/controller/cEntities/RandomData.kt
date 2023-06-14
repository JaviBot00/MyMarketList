package com.politecnicomalaga.mymarketlist.controller.cEntities

import android.app.Activity
import com.politecnicomalaga.mymarketlist.controller.cSQLite.ClientSQLite
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random

class RandomData {

    fun main(fromActivity: Activity) {
        val random = Random()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val priceFormat = DecimalFormat("###.##")

        val records = mutableListOf<String>()

        repeat(30) {
            val rr = generateRandomString(15)
            val startDate = generateRandomDate(2023, ((Math.random() * 6) + 1).toInt(), ((Math.random() * 30) + 1).toInt(), random)
            val endDate = generateRandomDate(2023, ((Math.random() * 12) + 1).toInt(), ((Math.random() * 30) + 1).toInt(), random)
            val price = generateRandomPrice(random)

            val record ="INSERT INTO List (sName, dCreated, dRealized, nPrice, bOnLine) VALUES ('$rr', '${dateFormat.format(startDate)}', '${dateFormat.format(endDate)}', '${priceFormat.format(price)}', 1)"
            records.add(record)
        }

        records.forEach { ClientSQLite(fromActivity).writableDatabase.execSQL(it) }
    }

    private fun generateRandomString(length: Int): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    private fun generateRandomDate(year: Int, month: Int, day: Int, random: Random): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, day)

        val startMillis = calendar.timeInMillis
        val endMillis = startMillis + (random.nextInt(24 * 60 * 60 * 1000)).toLong()

        return Date(startMillis + (random.nextDouble() * (endMillis - startMillis)).toLong())
    }

    private fun generateRandomPrice(random: Random): Double {
        return random.nextDouble() * 240.99
    }

}