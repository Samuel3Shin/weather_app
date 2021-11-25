package com.example.weather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.highsoft.highcharts.common.hichartsclasses.*
import com.highsoft.highcharts.core.HIChartView
import org.json.JSONObject
import org.json.JSONTokener
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.reflect.typeOf


class WeeklyTab : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weekly_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get json data
        val activity: DetailsActivity? = activity as DetailsActivity?
        val json_data: String = activity!!.getJsonData()!!

//        val chartView = findViewById<HIChartView>(R.id.hc)
        val chartView = requireView().findViewById<View>(R.id.hc) as HIChartView

        val options = HIOptions()

        val chart = HIChart()
        chart.type = "arearange"
        chart.zoomType = "x"
        options.chart = chart

        val title = HITitle()
        title.text = "Temperature variation by day"
        options.title = title

        val xaxis = HIXAxis()
        xaxis.type = "datetime"
        options.xAxis = object : java.util.ArrayList<HIXAxis?>() {
            init {
                add(xaxis)
            }
        }

        val yaxis = HIYAxis()
        yaxis.title = HITitle()
        options.yAxis = object : java.util.ArrayList<HIYAxis?>() {
            init {
                add(yaxis)
            }
        }

        val tooltip = HITooltip()
        tooltip.shadow = true
        tooltip.valueSuffix = "°C"
        options.tooltip = tooltip

        val legend = HILegend()
        legend.enabled = false
        options.legend = legend

        val series = HIArearange()
        series.name = "Temperatures"

        val jsonObject = JSONTokener(json_data).nextValue() as JSONObject

        var dateStr = jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(2).getJSONArray("intervals").getJSONObject(0).getString("startTime")
//        val date = Date(dateStr.substring(0, dateStr.length-6))
//        Log.d("TAG", jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(2).getJSONArray("intervals").getJSONObject(0).getString("startTime"))
        Log.d("TAG", dateStr.substring(0, dateStr.length-15))
        val weeklyData = jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(2).getJSONArray("intervals")

        val l = LocalDate.parse(dateStr.substring(0, dateStr.length-15), DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val unix = l.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
        Log.d("TAG", unix.toString())

//        for(var i=0; i<this.json_data["data"]["timelines"][2]["intervals"].length; ++i) {
//            let date = new Date(this.json_data["data"]["timelines"][2]["intervals"][i]["startTime"]);
//            weather_chart_data.push([Math.round(date.getTime()), this.json_data["data"]["timelines"][2]["intervals"][i]["values"]["temperatureMin"], this.json_data["data"]["timelines"][2]["intervals"][i]["values"]["temperatureMax"]]);
//        }

        var seriesData = arrayOf<Array<Any?>>()

        Log.d("TAG", weeklyData.length().toString())

        for( i in 0..weeklyData.length()-1) {
            var dateStr = weeklyData.getJSONObject(i).getString("startTime")
            val l = LocalDate.parse(dateStr.substring(0, dateStr.length-15), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            var unix = l.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
            unix *= 1000L

            var lowTemp = weeklyData.getJSONObject(i).getJSONObject("values").getString("temperatureMin")
            var highTemp = weeklyData.getJSONObject(i).getJSONObject("values").getString("temperatureMax")

//            Log.d("TAG", "$lowTemp $highTemp")
//            Log.d("TAG","${lowTemp::class.simpleName}")
//            seriesData += seriesData_tmp.get(i)
//            seriesData += arrayOf<Any?>(1388538000000L, 1.1, 4.7)
            seriesData += arrayOf<Any?>(unix, lowTemp.toDouble(), highTemp.toDouble())
        }

        series.data = java.util.ArrayList(Arrays.asList<Array<Any?>>(*seriesData))
        options.series = ArrayList(Arrays.asList(series))

        chartView.options = options
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }
}