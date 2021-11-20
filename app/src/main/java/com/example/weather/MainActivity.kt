package com.example.weather

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import org.json.JSONTokener


class MainActivity : AppCompatActivity() {
    val posts: MutableList<DailyTempPost> = mutableListOf()
    var lat = ""
    var lng = ""
    var json_data = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // get IP info API
        val queue = Volley.newRequestQueue(this)
        val ipUrl = "https://ipinfo.io/json?token=ecddd4a7e21254"

        val ipInfoRequest = JsonObjectRequest(
            Request.Method.GET, ipUrl, null,
            { response ->

                Log.d("TAG", response.getString("loc"))
                lat = response.getString("loc").split(",")[0]
                lng = response.getString("loc").split(",")[1]

                addressTextView.text = response.getString("city") + ", " + response.getString("region")

                var weatherUrl = "http://10.26.30.167:8080/weather?lat=${lat}&lng=${lng}"

                //        var weatherUrl = "http://127.0.0.1:8080/weather"
                val weatherRequest = JsonObjectRequest(
                    Request.Method.GET, weatherUrl, null,
                    { response ->
                        val jsonObject = JSONTokener(response.toString()).nextValue() as JSONObject
                        json_data = response.toString()

                        var current_data = jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals").getJSONObject(0).getJSONObject("values")

                        mainWeatherIcon.setImageResource(Utils.weatherCodeMap.get(current_data.getString("weatherCode").toString())!!.first)
                        mainWeatherTextView.text = Utils.weatherCodeMap.get(current_data.getString("weatherCode").toString())!!.second


                        temperatureTextView.text = current_data.getString("temperature").toString()
                        humidityTextView.text = current_data.getString("humidity").toString()
                        windTextView.text = current_data.getString("windSpeed").toString()
                        visibilityTextView.text = current_data.getString("visibility").toString()
                        pressureTextView.text = current_data.getString("pressureSeaLevel").toString()

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

            startActivity(intent)

        }


        val layoutManager = LinearLayoutManager(applicationContext)

        recyclerView.layoutManager = layoutManager
        recyclerView.layoutManager?.scrollToPosition(0)

        recyclerView.adapter = DailyTempPostAdapter(this@MainActivity, posts)

        var myWeatherList: MutableList<String> = mutableListOf()

        myWeatherList.add("2021-10-27,46,60")

        for(i in 0 until myWeatherList.size) {
            var post = DailyTempPost(myWeatherList[i].split(",")[0], "", myWeatherList[i].split(",")[1], myWeatherList[i].split(",")[2]);
//            Log.d("TAG", post.date + " " + post.highTemp)
            posts.add(post)
            Log.d("TAG", "post added")
            recyclerView.adapter?.notifyItemInserted(posts.size - 1)

//            Log.d("TAG", "recyclerView notified")
//            Log.d("TAG", posts.size.toString())
        }
    }

    // Called before the activity is destroyed
    public override fun onDestroy() {
        recyclerView.layoutManager = null
        recyclerView.adapter = null
        super.onDestroy()
    }

    // create an action bar button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        menuInflater.inflate(R.menu.mymenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // handle button activities
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        if (id == R.id.mybutton) {
            Log.d("TAG", "search button clicked")

//            val intent = Intent(this, SearchableActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//            startActivity(intent)
//            finish()
            // do something here

            // Create the text message with a string.
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEARCH
//                putExtra(Intent.EXTRA_TEXT, textMessage)
//                type = "text/plain"
            }

            // Try to invoke the intent.
            try {
                Log.d("TAG", "start activity1")
                startActivity(sendIntent)
                Log.d("TAG", "search button clicked2")
            } catch (e: ActivityNotFoundException) {
                Log.d("TAG", "Error occured!")
                // Define what your app should do if no activity can handle the intent.
            }
        }
        return super.onOptionsItemSelected(item)
    }
}