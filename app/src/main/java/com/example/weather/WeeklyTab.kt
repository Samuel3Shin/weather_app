package com.example.weather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.highsoft.highcharts.common.HIColor
import com.highsoft.highcharts.common.HIGradient
import com.highsoft.highcharts.common.HIStop
import com.highsoft.highcharts.common.hichartsclasses.*
import com.highsoft.highcharts.core.HIChartView
import org.json.JSONObject
import org.json.JSONTokener
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


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

        val chartView = requireView().findViewById<View>(R.id.hc) as HIChartView

        val options = HIOptions()

        val chart = HIChart()
        chart.type = "arearange"
        chart.zoomType = "x"
        options.chart = chart


//        chart.setBackgroundColor(HIColor.initWithLinearGradient(gradient, stops));

        val title = HITitle()
        title.text = "Temperature variation by day"
        options.title = title

        val xaxis = HIXAxis()
        xaxis.type = "datetime"
        xaxis.setDateTimeLabelFormats(HIDateTimeLabelFormats())
        xaxis.getDateTimeLabelFormats().setDay(HIDay())
        xaxis.getDateTimeLabelFormats().getDay().setMain("%e")
        options.xAxis = object : java.util.ArrayList<HIXAxis?>() {
            init {
                add(xaxis)
            }
        }

        val yaxis = HIYAxis()
        yaxis.title = HITitle()
        yaxis.setDateTimeLabelFormats(HIDateTimeLabelFormats())
        yaxis.getDateTimeLabelFormats().setDay(HIDay())
        yaxis.getDateTimeLabelFormats().getDay().setMain("%y-%b-%e")
        options.yAxis = object : java.util.ArrayList<HIYAxis?>() {
            init {
                add(yaxis)
            }
        }

        val tooltip = HITooltip()
        tooltip.shadow = true
        tooltip.valueSuffix = "°F"
        tooltip.xDateFormat = "%Y-%m-%d"
        tooltip.borderColor = HIColor.initWithRGB(226, 189, 150)
        options.tooltip = tooltip

        val legend = HILegend()
        legend.enabled = false
        options.legend = legend

        val series = HIArearange()
        series.name = "Temperatures"


        // gradient
        val gradient = HIGradient(0f, 0.5f, 1f, 1f)
        val stops: LinkedList<HIStop> = LinkedList()
        stops.add(HIStop(0.1f, HIColor.initWithRGB(226, 189, 150)))
//        stops.add(HIStop(0.8f, HIColor.initWithRGB(144, 165, 185)))
        stops.add(HIStop(0.8f, HIColor.initWithRGB(123, 164, 212)))
        series.fillColor = HIColor.initWithLinearGradient(gradient, stops)
        series.fillOpacity = 0.75
        series.lineColor = HIColor.initWithRGB(231, 198, 155)
        series.lineWidth = 0
        series.color = HIColor.initWithRGB(152, 152, 137)

        // marker color
        val marker = HIMarker()
        marker.lineWidth = 0
        series.marker = marker
        marker.fillColor = HIColor.initWithRGB(186, 185, 181)

        val jsonObject = JSONTokener(json_data).nextValue() as JSONObject

        var dateStr = jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(2).getJSONArray("intervals").getJSONObject(0).getString("startTime")

        Log.d("TAG", dateStr.split("T")[0])
        val weeklyData = jsonObject.getJSONObject("data").getJSONArray("timelines").getJSONObject(2).getJSONArray("intervals")

        val l = LocalDate.parse(dateStr.split("T")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val unix = l.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
        Log.d("TAG", unix.toString())

        var seriesData = arrayOf<Array<Any?>>()

        Log.d("TAG", weeklyData.length().toString())

        var cnt = 0
        for( i in 0..weeklyData.length()-1) {
            cnt++
            if(cnt == 15){
                break
            }

            var dateStr = weeklyData.getJSONObject(i).getString("startTime")
            val l = LocalDate.parse(dateStr.split("T")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            var unix = l.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
            unix *= 1000L

            var lowTemp = weeklyData.getJSONObject(i).getJSONObject("values").getString("temperatureMin")
            var highTemp = weeklyData.getJSONObject(i).getJSONObject("values").getString("temperatureMax")

            seriesData += arrayOf<Any?>(unix, lowTemp.toDouble(), highTemp.toDouble())
        }

        series.data = java.util.ArrayList(Arrays.asList<Array<Any?>>(*seriesData))
        options.series = ArrayList(Arrays.asList(series))

        chartView.options = options
    }

}