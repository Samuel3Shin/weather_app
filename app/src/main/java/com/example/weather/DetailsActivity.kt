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
import org.json.JSONObject
import org.json.JSONTokener


class DetailsActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    private lateinit var json_data: String
    private lateinit var city: String
    private lateinit var state: String

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
//            Log.d("TAG", "detailActivity josn get?")
//            Log.d("TAG", json_data)

            city = extras.getString("city").toString()
            state = extras.getString("state").toString()
            //The key argument here must match that used in the other activity
        }

        // calling the action bar
        var actionBar = supportActionBar
        actionBar?.title = city + ", " + state

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

                Log.d("TAG", "back button clicked!")
                onBackPressed()
            }
            R.id.twitter_button -> {

                Log.d("TAG", "twitter button clicked!")

                val jsonObject = JSONTokener(json_data).nextValue() as JSONObject
                val current_data = jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals").getJSONObject(0).getJSONObject("values")
                val warmth = current_data.getString("temperature").toString() + "°F"

                var url = "https://twitter.com/intent/tweet?text=Check out " + city + ", " + state + "'s weather! It is " + warmth + "!        " + "\n%23CSCI571WeatherSearch"
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            }

        }
        return true
    }

    // Twitter button
    // create an action bar button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // If you don't have res/menu, just create a directory named "menu" inside res
        menuInflater.inflate(R.menu.twitter_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun getJsonData(): String? {
        return json_data
    }
}