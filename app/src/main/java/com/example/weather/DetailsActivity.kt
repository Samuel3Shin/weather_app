package com.example.weather

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout


class
DetailsActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager

    val tabIcons = arrayOf<Int>(
        R.drawable.calendar_today,
        R.drawable.trending_up,
        R.drawable.thermometer_low
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // calling the action bar
        var actionBar = supportActionBar
        actionBar?.title = "Los Angeles, California"

        // showing the back button in action bar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        // tab implement
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        // tab ripple effect
        tabLayout.setTabRippleColorResource(R.color.white)

        // tab set text
        tabLayout.addTab(tabLayout.newTab().setText("TODAY"))
        tabLayout.addTab(tabLayout.newTab().setText("WEEKLY"))
        tabLayout.addTab(tabLayout.newTab().setText("WEATHER DATA"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = TabAdapter(this, supportFragmentManager,
            tabLayout.tabCount)

        // tab set icon

        for (i in tabIcons.indices) {
            val mIcon = getDrawable(tabIcons[i]);
            mIcon!!.setColorFilter(
                ResourcesCompat.getColor(resources, R.color.white, null),
                PorterDuff.Mode.MULTIPLY
            )
            tabLayout.getTabAt(i)?.icon = mIcon;
        }

//        val mIcon = getDrawable(tabIcons[0])
//
////        mIcon.setColorFilter("#FFFFFF", )
//        mIcon!!.setColorFilter(
//            ResourcesCompat.getColor(getResources(), R.color.white, null),
//            PorterDuff.Mode.MULTIPLY
//        )
//
//        tabLayout.getTabAt(0)?.setIcon(mIcon);
//        tabLayout.getTabAt(1)?.setIcon(tabIcons[1]);
//        tabLayout.getTabAt(2)?.setIcon(tabIcons[2]);


        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    // back button clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                Toast.makeText(
                    applicationContext,
                    "Back button clicked",
                    Toast.LENGTH_SHORT
                ).show()
                onBackPressed()
            }

        }
        return true
    }

    // Twitter button
    // create an action bar button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        menuInflater.inflate(R.menu.mymenu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}