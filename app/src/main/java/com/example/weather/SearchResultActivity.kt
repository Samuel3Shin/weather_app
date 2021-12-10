package com.example.weather

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.android.synthetic.main.activity_search_result.addressTextView
import kotlinx.android.synthetic.main.activity_search_result.card1
import kotlinx.android.synthetic.main.activity_search_result.dateTextView1
import kotlinx.android.synthetic.main.activity_search_result.dateTextView2
import kotlinx.android.synthetic.main.activity_search_result.dateTextView3
import kotlinx.android.synthetic.main.activity_search_result.dateTextView4
import kotlinx.android.synthetic.main.activity_search_result.dateTextView5
import kotlinx.android.synthetic.main.activity_search_result.dateTextView6
import kotlinx.android.synthetic.main.activity_search_result.dateTextView7
import kotlinx.android.synthetic.main.activity_search_result.favoriteButton
import kotlinx.android.synthetic.main.activity_search_result.highTempTextView1
import kotlinx.android.synthetic.main.activity_search_result.highTempTextView2
import kotlinx.android.synthetic.main.activity_search_result.highTempTextView3
import kotlinx.android.synthetic.main.activity_search_result.highTempTextView4
import kotlinx.android.synthetic.main.activity_search_result.highTempTextView5
import kotlinx.android.synthetic.main.activity_search_result.highTempTextView6
import kotlinx.android.synthetic.main.activity_search_result.highTempTextView7
import kotlinx.android.synthetic.main.activity_search_result.humidityTextView
import kotlinx.android.synthetic.main.activity_search_result.iconImg1
import kotlinx.android.synthetic.main.activity_search_result.iconImg2
import kotlinx.android.synthetic.main.activity_search_result.iconImg3
import kotlinx.android.synthetic.main.activity_search_result.iconImg4
import kotlinx.android.synthetic.main.activity_search_result.iconImg5
import kotlinx.android.synthetic.main.activity_search_result.iconImg6
import kotlinx.android.synthetic.main.activity_search_result.iconImg7
import kotlinx.android.synthetic.main.activity_search_result.lowTempTextView1
import kotlinx.android.synthetic.main.activity_search_result.lowTempTextView2
import kotlinx.android.synthetic.main.activity_search_result.lowTempTextView3
import kotlinx.android.synthetic.main.activity_search_result.lowTempTextView4
import kotlinx.android.synthetic.main.activity_search_result.lowTempTextView5
import kotlinx.android.synthetic.main.activity_search_result.lowTempTextView6
import kotlinx.android.synthetic.main.activity_search_result.lowTempTextView7
import kotlinx.android.synthetic.main.activity_search_result.mainWeatherIcon
import kotlinx.android.synthetic.main.activity_search_result.mainWeatherTextView
import kotlinx.android.synthetic.main.activity_search_result.pressureTextView
import kotlinx.android.synthetic.main.activity_search_result.progressbar_layout
import kotlinx.android.synthetic.main.activity_search_result.temperatureTextView
import kotlinx.android.synthetic.main.activity_search_result.visibilityTextView
import kotlinx.android.synthetic.main.activity_search_result.windTextView
import org.json.JSONObject
import org.json.JSONTokener
import kotlin.math.roundToInt

class SearchResultActivity : AppCompatActivity() {
    val preference: SharedPreferences by lazy {getSharedPreferences("mainActivity", Context.MODE_PRIVATE)}

    private lateinit var json_data: String
    private lateinit var city: String
    private lateinit var state: String
    private lateinit var lat: String
    private lateinit var lng: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        val toolbar: Toolbar = findViewById(R.id.my_toolbar) as Toolbar

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()?.setHomeButtonEnabled(true);
        }

        // get json data
        val extras = intent.extras
        if (extras != null) {
//            json_data = extras.getString("json_data").toString()

            city = extras.getString("city").toString()
            state = extras.getString("state").toString()
            lat = extras.getString("lat").toString()
            lng = extras.getString("lng").toString()

            Log.d("TAG", "city: " + city + " state: " + state + " lat: " + lat + " lng: " + lng)

        }

        val queue = Volley.newRequestQueue(this)

        var weatherUrl = "http://10.26.230.250:8080/weather?lat=${lat}&lng=${lng}"

        val weatherRequest = JsonObjectRequest(
            Request.Method.GET, weatherUrl, null,
            { response ->
                val jsonObject = JSONTokener(response.toString()).nextValue() as JSONObject
                json_data = response.toString()

                Log.d("TAG", city + ", " + state)

                // calling the action bar
                var actionBar = supportActionBar
                actionBar?.title = city + ", " + state

                // showing the back button in action bar
                actionBar?.setDisplayHomeAsUpEnabled(true)
                actionBar?.setDisplayShowHomeEnabled(true)

//                val jsonObject = JSONTokener(json_data).nextValue() as JSONObject

                var current_data = jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals").getJSONObject(0).getJSONObject("values")

                mainWeatherIcon.setImageResource(Utils.weatherCodeMap.get(current_data.getString("weatherCode").toString())!!.first)
                mainWeatherTextView.text = Utils.weatherCodeMap.get(current_data.getString("weatherCode").toString())!!.second

                addressTextView.text = city + ", " + state

                temperatureTextView.text = current_data.getString("temperature").toDouble().roundToInt().toString() + "°F"
                humidityTextView.text = current_data.getString("humidity").toString() + "%"
                windTextView.text = current_data.getString("windSpeed").toString() + "mph"
                visibilityTextView.text = current_data.getString("visibility").toString() + "mi"
                pressureTextView.text = current_data.getString("pressureSeaLevel").toString() + "inHg"

                var weekly_data = jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(2).getJSONArray("intervals")

                var dateStr = weekly_data.getJSONObject(0).getString("startTime")
                dateTextView1.text = dateStr.substring(0, dateStr.length-15)
                iconImg1.setImageResource(Utils.weatherCodeMap.get(weekly_data.getJSONObject(0).getJSONObject("values").getString("weatherCode").toString())!!.first)
                lowTempTextView1.text = weekly_data.getJSONObject(0).getJSONObject("values").getString("temperatureMin").toDouble().roundToInt().toString()
                highTempTextView1.text = weekly_data.getJSONObject(0).getJSONObject("values").getString("temperatureMax").toDouble().roundToInt().toString()

                dateStr = weekly_data.getJSONObject(1).getString("startTime")
                dateTextView2.text = dateStr.substring(0, dateStr.length-15)
                iconImg2.setImageResource(Utils.weatherCodeMap.get(weekly_data.getJSONObject(1).getJSONObject("values").getString("weatherCode").toString())!!.first)
                lowTempTextView2.text = weekly_data.getJSONObject(1).getJSONObject("values").getString("temperatureMin").toDouble().roundToInt().toString()
                highTempTextView2.text = weekly_data.getJSONObject(1).getJSONObject("values").getString("temperatureMax").toDouble().roundToInt().toString()

                dateStr = weekly_data.getJSONObject(2).getString("startTime")
                dateTextView3.text = dateStr.substring(0, dateStr.length-15)
                iconImg3.setImageResource(Utils.weatherCodeMap.get(weekly_data.getJSONObject(2).getJSONObject("values").getString("weatherCode").toString())!!.first)
                lowTempTextView3.text = weekly_data.getJSONObject(2).getJSONObject("values").getString("temperatureMin").toDouble().roundToInt().toString()
                highTempTextView3.text = weekly_data.getJSONObject(2).getJSONObject("values").getString("temperatureMax").toDouble().roundToInt().toString()

                dateStr = weekly_data.getJSONObject(3).getString("startTime")
                dateTextView4.text = dateStr.substring(0, dateStr.length-15)
                iconImg4.setImageResource(Utils.weatherCodeMap.get(weekly_data.getJSONObject(3).getJSONObject("values").getString("weatherCode").toString())!!.first)
                lowTempTextView4.text = weekly_data.getJSONObject(3).getJSONObject("values").getString("temperatureMin").toDouble().roundToInt().toString()
                highTempTextView4.text = weekly_data.getJSONObject(3).getJSONObject("values").getString("temperatureMax").toDouble().roundToInt().toString()

                dateStr = weekly_data.getJSONObject(4).getString("startTime")
                dateTextView5.text = dateStr.substring(0, dateStr.length-15)
                iconImg5.setImageResource(Utils.weatherCodeMap.get(weekly_data.getJSONObject(4).getJSONObject("values").getString("weatherCode").toString())!!.first)
                lowTempTextView5.text = weekly_data.getJSONObject(4).getJSONObject("values").getString("temperatureMin").toDouble().roundToInt().toString()
                highTempTextView5.text = weekly_data.getJSONObject(4).getJSONObject("values").getString("temperatureMax").toDouble().roundToInt().toString()

                dateStr = weekly_data.getJSONObject(5).getString("startTime")
                dateTextView6.text = dateStr.substring(0, dateStr.length-15)
                iconImg6.setImageResource(Utils.weatherCodeMap.get(weekly_data.getJSONObject(5).getJSONObject("values").getString("weatherCode").toString())!!.first)
                lowTempTextView6.text = weekly_data.getJSONObject(5).getJSONObject("values").getString("temperatureMin").toDouble().roundToInt().toString()
                highTempTextView6.text = weekly_data.getJSONObject(5).getJSONObject("values").getString("temperatureMax").toDouble().roundToInt().toString()


                dateStr = weekly_data.getJSONObject(6).getString("startTime")
                dateTextView7.text = dateStr.substring(0, dateStr.length-15)
                iconImg7.setImageResource(Utils.weatherCodeMap.get(weekly_data.getJSONObject(6).getJSONObject("values").getString("weatherCode").toString())!!.first)
                lowTempTextView7.text = weekly_data.getJSONObject(6).getJSONObject("values").getString("temperatureMin").toDouble().roundToInt().toString()
                highTempTextView7.text = weekly_data.getJSONObject(6).getJSONObject("values").getString("temperatureMax").toDouble().roundToInt().toString()

                // Card 1 click -> detail
                card1.setOnClickListener {
                    Log.d("TAG", "card1 click!")
                    val intent = Intent(this, DetailsActivity::class.java)
                    intent.putExtra("json_data", json_data)
                    intent.putExtra("city", city)
                    intent.putExtra("state", state)

                    startActivity(intent)

                }

                var favoriteInfoStr = preference.getString("favoriteInfo", "")
                var favoriteInfoStrArr = favoriteInfoStr!!.split("@")

                // check whether it already has the city and state
                var isExist = false
                for(i in 1..favoriteInfoStrArr.size-1) {
                    if(favoriteInfoStrArr[i].split(",").get(0) == city && favoriteInfoStrArr[i].split(",").get(1) == state) {
                        isExist = true
                        break
                    }
                }

                if(isExist) {
                    favoriteButton.setImageResource(R.drawable.map_marker_minus)
                } else {
                    favoriteButton.setImageResource(R.drawable.map_marker_plus)
                }

                favoriteButton.setOnClickListener{
                    Log.d("TAG", "fab clicked!")

                    var favoriteInfoStr = preference.getString("favoriteInfo", "")
                    var favoriteInfoStrArr = favoriteInfoStr!!.split("@")

                    var isExist = false
                    for(i in 1..favoriteInfoStrArr.size-1) {
                        if(favoriteInfoStrArr[i].split(",").get(0) == city && favoriteInfoStrArr[i].split(",").get(1) == state) {
                            isExist = true
                            break
                        }
                    }

                    if(isExist) {
                        // remove favorite city
                        var newFavoriteInfo = ""

                        var deletedIdx = 1
                        for(i in 1..favoriteInfoStrArr.size-1) {
                            if(!(favoriteInfoStrArr[i].split(",").get(0) == city && favoriteInfoStrArr[i].split(",").get(1) == state)) {
                                newFavoriteInfo = newFavoriteInfo + "@" + favoriteInfoStrArr[i]
                            } else {
                                deletedIdx = i
                            }
                        }
                        preference.edit().putString("favoriteInfo", newFavoriteInfo).apply()
                        favoriteButton.setImageResource(R.drawable.map_marker_plus)
                        Toast.makeText(applicationContext, "$city, $state was removed from favorites", Toast.LENGTH_SHORT).show()

                        favoriteInfoStr = preference.getString("favoriteInfo", "")
                        Log.d("TAG", favoriteInfoStr.toString())

                    } else {
                        // add favorite city
                        var curFavoriteInfo = preference.getString("favoriteInfo", "")
                        preference.edit().putString("favoriteInfo", "$curFavoriteInfo@$city,$state,$lat,$lng").apply()
                        favoriteButton.setImageResource(R.drawable.map_marker_minus)
                        Toast.makeText(applicationContext, "$city, $state was added to favorites", Toast.LENGTH_SHORT).show()

                        favoriteInfoStr = preference.getString("favoriteInfo", "")
                        Log.d("TAG", favoriteInfoStr.toString())
                    }
                }


            progressbar_layout.visibility = View.INVISIBLE
            result_layout.visibility = View.VISIBLE


            },
            { error ->
                // TODO: Handle error
                Log.d("TAG", error.toString())
            }
        )

        queue.add(weatherRequest)

        }

    // back button clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                Log.d("TAG", "back button clicked!")
                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()
//                onBackPressed()
            }
        }
        return true
    }
}