package com.example.pr4_weather_laptev

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val prefs = getSharedPreferences("WeatherAppPrefs", Context.MODE_PRIVATE)
            val username = prefs.getString("username", "")
            val password = prefs.getString("password", "")

            if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
                showFragment(AuthFragment())
            } else {
                showFragment(InputFragment())
            }
        }
    }

    fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
