package com.example.weather

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.libraries.places.api.Places
import kotlinx.android.synthetic.main.fragment_screen_slide_page.*
import kotlinx.android.synthetic.main.fragment_screen_slide_page.card1
import kotlinx.android.synthetic.main.fragment_screen_slide_page.dateTextView1
import kotlinx.android.synthetic.main.fragment_screen_slide_page.dateTextView2
import kotlinx.android.synthetic.main.fragment_screen_slide_page.dateTextView3
import kotlinx.android.synthetic.main.fragment_screen_slide_page.dateTextView4
import kotlinx.android.synthetic.main.fragment_screen_slide_page.dateTextView5
import kotlinx.android.synthetic.main.fragment_screen_slide_page.dateTextView6
import kotlinx.android.synthetic.main.fragment_screen_slide_page.dateTextView7
import kotlinx.android.synthetic.main.fragment_screen_slide_page.favoriteButton
import kotlinx.android.synthetic.main.fragment_screen_slide_page.highTempTextView1
import kotlinx.android.synthetic.main.fragment_screen_slide_page.highTempTextView2
import kotlinx.android.synthetic.main.fragment_screen_slide_page.highTempTextView3
import kotlinx.android.synthetic.main.fragment_screen_slide_page.highTempTextView4
import kotlinx.android.synthetic.main.fragment_screen_slide_page.highTempTextView5
import kotlinx.android.synthetic.main.fragment_screen_slide_page.highTempTextView6
import kotlinx.android.synthetic.main.fragment_screen_slide_page.highTempTextView7
import kotlinx.android.synthetic.main.fragment_screen_slide_page.humidityTextView
import kotlinx.android.synthetic.main.fragment_screen_slide_page.iconImg1
import kotlinx.android.synthetic.main.fragment_screen_slide_page.iconImg2
import kotlinx.android.synthetic.main.fragment_screen_slide_page.iconImg3
import kotlinx.android.synthetic.main.fragment_screen_slide_page.iconImg4
import kotlinx.android.synthetic.main.fragment_screen_slide_page.iconImg5
import kotlinx.android.synthetic.main.fragment_screen_slide_page.iconImg6
import kotlinx.android.synthetic.main.fragment_screen_slide_page.iconImg7
import kotlinx.android.synthetic.main.fragment_screen_slide_page.lowTempTextView1
import kotlinx.android.synthetic.main.fragment_screen_slide_page.lowTempTextView2
import kotlinx.android.synthetic.main.fragment_screen_slide_page.lowTempTextView3
import kotlinx.android.synthetic.main.fragment_screen_slide_page.lowTempTextView4
import kotlinx.android.synthetic.main.fragment_screen_slide_page.lowTempTextView5
import kotlinx.android.synthetic.main.fragment_screen_slide_page.lowTempTextView6
import kotlinx.android.synthetic.main.fragment_screen_slide_page.lowTempTextView7
import kotlinx.android.synthetic.main.fragment_screen_slide_page.mainWeatherIcon
import kotlinx.android.synthetic.main.fragment_screen_slide_page.mainWeatherTextView
import kotlinx.android.synthetic.main.fragment_screen_slide_page.pressureTextView
import kotlinx.android.synthetic.main.fragment_screen_slide_page.temperatureTextView
import kotlinx.android.synthetic.main.fragment_screen_slide_page.visibilityTextView
import kotlinx.android.synthetic.main.fragment_screen_slide_page.windTextView
import org.json.JSONObject
import org.json.JSONTokener
import kotlin.math.roundToInt

class ScreenSlidePageFragment : Fragment() {
    val preference: SharedPreferences by lazy {
        requireActivity().getSharedPreferences("mainActivity", Context.MODE_PRIVATE)}

    var lat = ""
    var lng = ""
    var json_data = ""
    var city = ""
    var state = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_screen_slide_page, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var page = requireArguments().getInt("pageNum", 0)
//        pageTextView.text = page.toString();

        var favoriteInfoStr = preference.getString("favoriteInfo", "")
        var favoriteInfoStrArr = favoriteInfoStr!!.split("@")

        Log.d("TAG", favoriteInfoStr.toString())

        if(page == 0) {
            // default weather page
            // Places API initialize
            Places.initialize(getActivity()?.getApplicationContext(), getString(R.string.api_key));

            // favorite button hidden
            favoriteButton.hide()

            // get IP info API
            val queue = Volley.newRequestQueue(activity)
            val ipUrl = "https://ipinfo.io/json?token=ecddd4a7e21254"

            val ipInfoRequest = JsonObjectRequest(
                Request.Method.GET, ipUrl, null,
                { response ->

                    Log.d("TAG", response.getString("loc"))
                    lat = response.getString("loc").split(",")[0]
                    lng = response.getString("loc").split(",")[1]

                    addressTextView.text = response.getString("city") + ", " + response.getString("region")
                    city = response.getString("city")
                    state = response.getString("region")


                    var weatherUrl = "http://10.26.25.186:8080/weather?lat=${lat}&lng=${lng}"

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
                            progressbar_layout.visibility = View.INVISIBLE
                            result_layout.visibility = View.VISIBLE

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
        } else {
            // favorite pages

            lat = favoriteInfoStrArr[page].split(",")[2]
            lng = favoriteInfoStrArr[page].split(",")[3]

            city = favoriteInfoStrArr[page].split(",")[0]
            state = favoriteInfoStrArr[page].split(",")[1]

            val queue = Volley.newRequestQueue(activity)

            var weatherUrl = "http://10.26.25.186:8080/weather?lat=${lat}&lng=${lng}"

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

        // Card 1 click -> detail
        card1.setOnClickListener {

            Log.d("TAG", "card1 click!")
            val intent = Intent(activity, DetailsActivity::class.java)
            intent.putExtra("json_data", json_data)
            intent.putExtra("city", city)
            intent.putExtra("state", state)

            startActivity(intent)
        }

        favoriteInfoStr = preference.getString("favoriteInfo", "")
        favoriteInfoStrArr = favoriteInfoStr!!.split("@")

        // check whether it already has the city and state
        var isExist = false
//        Log.d("TAG", "check")
        for(i in 1..favoriteInfoStrArr.size-1) {
            if(favoriteInfoStrArr[i].split(",").get(0) == city && favoriteInfoStrArr[i].split(",").get(1) == state) {
                isExist = true
                break
            }
        }
//        Log.d("TAG", "check done")

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
                Toast.makeText(getActivity()?.getApplicationContext(), "$city, $state was removed from favorites", Toast.LENGTH_SHORT).show()

                favoriteInfoStr = preference.getString("favoriteInfo", "")
                Log.d("TAG", favoriteInfoStr.toString())
                // remove page
                val activity: MainActivity? = activity as MainActivity?
                activity!!.removePage(deletedIdx)
//
//                removePage()

            } else {
                // add favorite city
                var curFavoriteInfo = preference.getString("favoriteInfo", "")
                preference.edit().putString("favoriteInfo", "$curFavoriteInfo@$city,$state,$lat,$lng").apply()
                favoriteButton.setImageResource(R.drawable.map_marker_minus)
                Toast.makeText(getActivity()?.getApplicationContext(), "$city, $state was added to favorites", Toast.LENGTH_SHORT).show()

                favoriteInfoStr = preference.getString("favoriteInfo", "")
                Log.d("TAG", favoriteInfoStr.toString())
            }
        }

    }

}