package com.example.weather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_today_tab.*

import org.json.JSONObject
import org.json.JSONTokener
import kotlin.math.roundToInt

class TodayTab : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_today_tab, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity: DetailsActivity? = activity as DetailsActivity?
        val json_data: String = activity!!.getJsonData()!!

        // populating today weather data
        val jsonObject = JSONTokener(json_data).nextValue() as JSONObject
        val current_data = jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals").getJSONObject(0).getJSONObject("values")

        wind_speed_textview.text =  String.format("%.02f", current_data.getString("windSpeed").toDouble()) + " mph"
        pressure_textview.text =  String.format("%.02f", current_data.getString("pressureSeaLevel").toDouble()) + " inHg"
        precipitation_textview.text =  String.format("%.02f", current_data.getString("precipitationProbability").toDouble()) + " %"
        temperature_textview.text =  current_data.getString("temperature").toDouble().roundToInt().toString() + "°F"

        humidity_textview.text =  current_data.getString("humidity").toString() + "%"
        visibility_textview.text =  String.format("%.02f", current_data.getString("visibility").toDouble()) + " mi"
        cloud_cover_textview.text =  current_data.getString("cloudCover").toString() + "%"
        ozone_textview.text =  String.format("%.02f", current_data.getString("windSpeed").toDouble())

        today_icon.setImageResource(Utils.weatherCodeMap[current_data.getString("weatherCode").toString()]!!.first)
        today_weather_textview.text = Utils.weatherCodeMap[current_data.getString("weatherCode").toString()]!!.second

    }

}