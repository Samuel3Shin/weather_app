package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import org.json.JSONTokener
import kotlin.reflect.typeOf

class MainActivity : AppCompatActivity() {
    val posts: MutableList<DailyTempPost> = mutableListOf()
    var lat = ""
    var lng = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // get IP info API
        val queue = Volley.newRequestQueue(this)
        val ipUrl = "https://ipinfo.io/json?token=ecddd4a7e21254"

        val ipInfoRequest = JsonObjectRequest(
            Request.Method.GET, ipUrl, null,
            Response.Listener { response ->

                Log.d("TAG", response.getString("loc"))
                lat = response.getString("loc").split(",")[0]
                lng = response.getString("loc").split(",")[1]

                addressTextView.text = response.getString("city") + ", " + response.getString("region")

                var weatherUrl = "http://10.26.30.167:8080/weather?lat=${lat}&lng=${lng}"

                //        var weatherUrl = "http://127.0.0.1:8080/weather"
                val weatherRequest = JsonObjectRequest(
                    Request.Method.GET, weatherUrl, null,
                    Response.Listener { response ->
                        val jsonObject = JSONTokener(response.toString()).nextValue() as JSONObject

                        temperatureTextView.text = jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals").getJSONObject(0).getJSONObject("values").getString("temperature").toString()
                        humidityTextView.text = jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals").getJSONObject(0).getJSONObject("values").getString("humidity").toString()
                        windTextView.text = jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals").getJSONObject(0).getJSONObject("values").getString("windSpeed").toString()
                        visibilityTextView.text = jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals").getJSONObject(0).getJSONObject("values").getString("visibility").toString()
                        pressureTextView.text = jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals").getJSONObject(0).getJSONObject("values").getString("pressureSeaLevel").toString()

                        Log.d("TAG", jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals").getJSONObject(0).getJSONObject("values").toString())
                    },
                    Response.ErrorListener { error ->
                        // TODO: Handle error
                        Log.d("TAG", error.toString())
                    }
                )
                queue.add(weatherRequest)

            },
            Response.ErrorListener { error ->
                // TODO: Handle error
                Log.d("TAG", error.toString())
            }
        )

        queue.add(ipInfoRequest)


        val layoutManager = LinearLayoutManager(applicationContext)

        recyclerView.layoutManager = layoutManager
        recyclerView.layoutManager?.scrollToPosition(0)

        recyclerView.adapter = DailyTempPostAdapter(this@MainActivity, posts)

        var myWeatherList: MutableList<String> = mutableListOf()

        myWeatherList.add("2021-10-27,46,60")

        for(i in 0 until myWeatherList.size) {
            var post = DailyTempPost(myWeatherList[i].split(",")[0], "", myWeatherList[i].split(",")[1], myWeatherList[i].split(",")[2]);
            Log.d("TAG", post.date + " " + post.highTemp)
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
}