package com.example.weather

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log

class SplashScreenActivity: Activity() {
    private val duration:Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("TAG", "splash screen started!")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()

        }, duration)
    }

    override fun onBackPressed() {
        // We don't want the splash screen to be interrupted
    }
}