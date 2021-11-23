package com.example.weather

import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_today_tab.*


class DetailsActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    private lateinit var json_data: String

    private val tabIcons = arrayOf<Int>(
        R.drawable.calendar_today,
        R.drawable.trending_up,
        R.drawable.thermometer_low
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // get json data
        val extras = intent.extras
        if (extras != null) {
            json_data = extras.getString("json_data").toString()
            Log.d("TAG", "detailActivity josn get?")
            Log.d("TAG", json_data)
            //The key argument here must match that used in the other activity
        }

        // calling the action bar
        var actionBar = supportActionBar
        actionBar?.title = "Seattle, Washington"

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
//                Toast.makeText(
//                    applicationContext,
//                    "Back button clicked",
//                    Toast.LENGTH_SHORT
//                ).show()
                Log.d("TAG", "back button clicked!")
                onBackPressed()
            }
            R.id.twitter_button -> {
//                Toast.makeText(
//                    applicationContext,
//                    "Back button clicked",
//                    Toast.LENGTH_SHORT
//                ).show()
                Log.d("TAG", "twitter button clicked!")
//                onBackPressed()

//                var strWindowFeatures = "menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes";
//                var url = "https://twitter.com/intent/tweet?text=The temperature in ${this.city}, ${this.state} on ${this.date} is ${this.weatherDetails[3]["value"]}. The weather conditions are ${this.weatherDetails[0]["value"]} %23CSCI571WeatherForecast"
                var url = "https://twitter.com/intent/tweet?text=The temperature"
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            }

        }
        return true
    }

    // Twitter button
    // create an action bar button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        menuInflater.inflate(R.menu.twitter_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }



    fun getJsonData(): String? {
        return json_data
    }
}