package com.example.weather

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import org.json.JSONTokener
import java.util.*


class MainActivity : AppCompatActivity()  {

    val preference: SharedPreferences by lazy {getSharedPreferences("mainActivity", Context.MODE_PRIVATE)}

    val AUTOCOMPLETE_REQUEST_CODE = 1
    val ADD_TAB_REQUEST_CODE = 2

    private var NUM_PAGES = 1
    private lateinit var mPager: ViewPager
    private lateinit var pagerAdapter: ScreenSlidePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById(R.id.pager)

        // The pager adapter, which provides the pages to the view pager widget.
        pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
        mPager.adapter = pagerAdapter
        mPager.offscreenPageLimit = 10
    }

    fun removePage(position: Int) {
        mPager.removeViewAt(position)
        tabIndicator.visibility = View.INVISIBLE
        pagerAdapter.notifyDataSetChanged()
    }

    fun addPage() {
        tabIndicator.visibility = View.INVISIBLE
        pagerAdapter.notifyDataSetChanged()
    }

    fun setVisibleTabIndicator() {
        Log.d("TAG", "setVisible called")
        tabIndicator.visibility = View.VISIBLE
    }

    // Called before the activity is destroyed
    public override fun onDestroy() {
        super.onDestroy()
    }

    // create an action bar button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // If you don't have res/menu, just create a directory named "menu" inside res
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }


    // handle button activities
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                onSearchCalled()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> false
        }

    }

    fun onSearchCalled() {
        // Set the fields to specify which types of place data to return.
        val fields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG
        )

        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.OVERLAY, fields
        ).setCountry("US") // US
            .build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Log.i("TAG", place.toString())
                        Log.i("TAG", "Place: " + place.getName() + ", " + place.getId() + ", " + place.getAddress() + ", " + place.latLng);
                        Log.d("TAG", "search result click!")

                        var lat = place.latLng.toString().split(": ")[1].split(",")[0].substring(1)
                        var lng = place.latLng.toString().split(": ")[1].split(",")[1].split(")")[0]

                        var city = place.getAddress().toString().split(", ")[0]
                        var state = place.getAddress().toString().split(", ")[1]


                        val intent = Intent(this, SearchResultActivity::class.java)
                        //                                intent.putExtra("json_data", json_data)
                        intent.putExtra("city", city)
                        intent.putExtra("state", state)
                        intent.putExtra("lat", lat)
                        intent.putExtra("lng", lng)

//                                startActivity(intent)
                        startActivityForResult(intent, ADD_TAB_REQUEST_CODE)
//                                finish()

                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i("TAG", status.statusMessage.toString())
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        } else if(requestCode == ADD_TAB_REQUEST_CODE) {
            // add page
            addPage()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int {
            var favoriteInfoStr = preference.getString("favoriteInfo", "")
            NUM_PAGES = kotlin.math.max(1, favoriteInfoStr!!.split("@").size)
            return NUM_PAGES
        }

        override fun getItem(position: Int): Fragment {
            val fragmentFirst = ScreenSlidePageFragment()
            val args = Bundle()
            args.putInt("pageNum", position)
            fragmentFirst.setArguments(args)
            return fragmentFirst
        }

        override fun getItemPosition(@NonNull Object: Any): Int {
            return POSITION_NONE
        }

    }
}