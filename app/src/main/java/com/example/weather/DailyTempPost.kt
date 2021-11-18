package com.example.weather

class DailyTempPost {
    var date = ""
    var iconPath = ""
    var lowTemp = ""
    var highTemp = ""

    constructor(date: String, iconPath: String, lowTemp: String, highTemp: String) {
        this.date = date
        this.iconPath = iconPath
        this.lowTemp = lowTemp
        this.highTemp = highTemp
    }

}