package com.example.weather

object Utils {

    var weatherCodeMap : HashMap<String, Pair<Int, String>> = hashMapOf(
        "1000" to Pair(R.drawable.ic_clear_day, "Clear"),
        "1001" to Pair(R.drawable.ic_cloudy, "Cloudy"),
        "1100" to Pair(R.drawable.ic_mostly_cloudy, "Mostly Cloud"),
        "1101" to Pair(R.drawable.ic_partly_cloudy_day, "Partly Cloud"),
        "1102" to Pair(R.drawable.ic_mostly_cloudy, "Mostly Cloud"),
        "2000" to Pair(R.drawable.ic_fog, "Fog"),
        "2100" to Pair(R.drawable.ic_fog_light, "Light Fog"),
        "3000" to Pair(R.drawable.ic_light_wind, "Light Wind"),
        "3001" to Pair(R.drawable.ic_wind, "Wind"),
        "3002" to Pair(R.drawable.ic_strong_wind, "Strong Wind"),
        "4000" to Pair(R.drawable.ic_drizzle, "Drizzle"),
        "4001" to Pair(R.drawable.ic_rain, "Rain"),
        "4200" to Pair(R.drawable.ic_rain_light, "Light Rain"),
        "4201" to Pair(R.drawable.ic_rain_heavy, "Heavy Rain"),
        "5000" to Pair(R.drawable.ic_snow, "Snow"),
        "5001" to Pair(R.drawable.ic_flurries, "Flurries"),
        "5100" to Pair(R.drawable.ic_snow_light, "Light Snow"),
        "5101" to Pair(R.drawable.ic_snow_heavy, "Heavy Snow"),
        "6000" to Pair(R.drawable.ic_freezing_drizzle, "Freezing Drizzle"),
        "6001" to Pair(R.drawable.ic_freezing_rain, "Freezing Rain"),
        "6200" to Pair(R.drawable.ic_freezing_rain_light, "Light Freezing Rain"),
        "6201" to Pair(R.drawable.ic_freezing_rain_heavy, "Heavy Freezing Rain"),
        "7000" to Pair(R.drawable.ic_ice_pellets, "Ice Pellets"),
        "7101" to Pair(R.drawable.ic_ice_pellets_heavy, "Heavy Ice Pellets"),
        "7102" to Pair(R.drawable.ic_ice_pellets_light, "Light Ice Pellets"),
        "8000" to Pair(R.drawable.ic_tstorm, "Thunderstorm"),
    )

}