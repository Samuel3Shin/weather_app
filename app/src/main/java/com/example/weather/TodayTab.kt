package com.example.weather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class TodayTab : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val activity: DetailsActivity? = activity as DetailsActivity?
        val myDataFromActivity: String = activity!!.getJsonData()!!
        Log.d("TAG", "fragment got data?")
        Log.d("TAG", myDataFromActivity)

        return inflater.inflate(R.layout.fragment_today_tab, container, false)
    }
}