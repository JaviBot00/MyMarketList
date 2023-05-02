package com.politecnicomalaga.mymarketlist.view.productsMenu

import android.database.Cursor
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.adapter.ManagerSQLite
import com.politecnicomalaga.mymarketlist.controller.adapter.ViewPagerAdapter
import java.util.SortedSet

class ProductsActivity : AppCompatActivity() {

    private val myProducts = sortedMapOf<String, SortedSet<String>>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_product)
        setAppBar()

        loadData()
        val viewPager: ViewPager = findViewById(R.id.viewPager)
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, myProducts)
        viewPager.adapter = viewPagerAdapter
        val tabLayout: TabLayout = findViewById(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun loadData() {
        val mySQLite = ManagerSQLite(this@ProductsActivity)
        mySQLite.setWritable()
        val tc: Cursor = mySQLite.getTables()
        while (tc.moveToNext()) {
            if (tc.getString(0) != "android_metadata") {
                val product = sortedSetOf<String>()
                if (tc.getString(0) != "android_metadata") {
                    val pc: Cursor = mySQLite.getProducts(tc.getString(0))
                    while (pc.moveToNext()) {
                        product.add(pc.getString(0))
                    }
                }
                myProducts[tc.getString(0)] = product
            }
        }
    }

    private fun setAppBar(){
        if (supportActionBar != null) {
            if (parentActivityIntent == null) {
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                supportActionBar!!.setDisplayShowHomeEnabled(false)
            } else {
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                supportActionBar!!.setDisplayShowHomeEnabled(true)
            }
        }

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@ProductsActivity, R.color.marine_blue)))
        val spannableString = SpannableString(resources.getString(R.string.make_list))
        spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(Color.WHITE), 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        supportActionBar?.title = spannableString
    }

}