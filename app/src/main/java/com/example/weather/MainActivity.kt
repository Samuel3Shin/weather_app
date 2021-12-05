package com.example.weather

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import org.json.JSONTokener
import java.util.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {
    val AUTOCOMPLETE_REQUEST_CODE = 1
    var lat = ""
    var lng = ""
    var json_data = ""
    var city = ""
    var state = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Places API initialize
        Places.initialize(getApplicationContext(), getString(R.string.api_key));

        // get IP info API
        val queue = Volley.newRequestQueue(this)
        val ipUrl = "https://ipinfo.io/json?token=ecddd4a7e21254"

        val ipInfoRequest = JsonObjectRequest(
            Request.Method.GET, ipUrl, null,
            { response ->

                Log.d("TAG", response.getString("loc"))
                var lat = response.getString("loc").split(",")[0]
                var lng = response.getString("loc").split(",")[1]

                addressTextView.text = response.getString("city") + ", " + response.getString("region")
                city = response.getString("city")
                state = response.getString("region")

                var weatherUrl = "http://10.26.50.246:8080/weather?lat=${lat}&lng=${lng}"

                //        var weatherUrl = "http://127.0.0.1:8080/weather"
                val weatherRequest = JsonObjectRequest(
                    Request.Method.GET, weatherUrl, null,
                    { response ->
                        val jsonObject = JSONTokener(response.toString()).nextValue() as JSONObject
                        json_data = response.toString()

                        var current_data = jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals").getJSONObject(0).getJSONObject("values")

//                        Log.d("TAG", Utils.weatherCodeMap.get(current_data.getString("weatherCode").toString())!!.first.toString())
                        mainWeatherIcon.setImageResource(Utils.weatherCodeMap.get(current_data.getString("weatherCode").toString())!!.first)
                        mainWeatherTextView.text = Utils.weatherCodeMap.get(current_data.getString("weatherCode").toString())!!.second

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


//                        Log.d("TAG", jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals").getJSONObject(0).getJSONObject("values").toString())
                    },
                    { error ->
                        // TODO: Handle error
                        Log.d("TAG", error.toString())
                    }
                )
                queue.add(weatherRequest)

            },
            { error ->
                // TODO: Handle error
                Log.d("TAG", error.toString())
            }
        )

        queue.add(ipInfoRequest)

        // Card 1 click -> detail
        card1.setOnClickListener {

            Log.d("TAG", "card1 click!")
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("json_data", json_data)
            intent.putExtra("city", city)
            intent.putExtra("state", state)

            startActivity(intent)
        }

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

                        val queue = Volley.newRequestQueue(this)

                        var lat = place.latLng.toString().split(": ")[1].split(",")[0]
                        var lng = place.latLng.toString().split(": ")[1].split(",")[1]

                        var city = place.getAddress().toString().split(", ")[0]
                        var state = place.getAddress().toString().split(", ")[1]

                        var weatherUrl = "http://10.26.50.246:8080/weather?lat=${lat}&lng=${lng}"

                        val weatherRequest = JsonObjectRequest(
                            Request.Method.GET, weatherUrl, null,
                            { response ->
                                val jsonObject = JSONTokener(response.toString()).nextValue() as JSONObject
                                json_data = response.toString()

                                val intent = Intent(this, SearchResultActivity::class.java)
                                intent.putExtra("json_data", json_data)
                                intent.putExtra("city", city)
                                intent.putExtra("state", state)

                                startActivity(intent)

                            },
                            { error ->
                                // TODO: Handle error
                                Log.d("TAG", error.toString())
                            }
                        )

                        queue.add(weatherRequest)
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
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}