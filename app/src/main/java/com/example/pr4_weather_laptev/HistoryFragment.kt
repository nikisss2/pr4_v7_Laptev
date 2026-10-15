package com.example.pr4_weather_laptev

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryFragment : Fragment() {

    private lateinit var tvCurrentUser: TextView
    private lateinit var tvHistoryContent: TextView
    private lateinit var btnAddRecord: AppCompatButton
    private lateinit var btnClearHistory: AppCompatButton
    private lateinit var btnLogoutHistory: AppCompatButton

    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        tvCurrentUser = view.findViewById(R.id.tvCurrentUser)
        tvHistoryContent = view.findViewById(R.id.tvHistoryContent)
        btnAddRecord = view.findViewById(R.id.btnAddRecord)
        btnClearHistory = view.findViewById(R.id.btnClearHistory)
        btnLogoutHistory = view.findViewById(R.id.btnLogoutHistory)

        loadData()

        btnAddRecord.setOnClickListener {
            (activity as? MainActivity)?.showFragment(InputFragment())
        }

        btnClearHistory.setOnClickListener {
            val prefs = requireContext().getSharedPreferences("WeatherAppPrefs", Context.MODE_PRIVATE)
            prefs.edit().remove("weather_list").apply()
            tvHistoryContent.text = getString(R.string.history_empty)
            Toast.makeText(requireContext(), getString(R.string.toast_cleared), Toast.LENGTH_SHORT).show()
        }

        btnLogoutHistory.setOnClickListener {
            val prefs = requireContext().getSharedPreferences("WeatherAppPrefs", Context.MODE_PRIVATE)
            prefs.edit().remove("username").remove("password").apply()
            (activity as? MainActivity)?.showFragment(AuthFragment())
        }

        return view
    }

    private fun loadData() {
        val prefs = requireContext().getSharedPreferences("WeatherAppPrefs", Context.MODE_PRIVATE)
        val username = prefs.getString("username", "") ?: ""
        tvCurrentUser.text = getString(R.string.label_current_user) + username

        val json = prefs.getString("weather_list", "[]")
        val type = object : TypeToken<List<WeatherEntry>>() {}.type
        val list: List<WeatherEntry>? = gson.fromJson(json, type)

        if (list.isNullOrEmpty()) {
            tvHistoryContent.text = getString(R.string.history_empty)
        } else {
            val sb = StringBuilder()
            list.forEachIndexed { index, item ->
                sb.append("${index + 1}. Дата: ${item.date}\n")
                sb.append("   Город: ${item.city}\n")
                sb.append("   Температура: ${item.temperature}°C\n")
                sb.append("   Погода: ${item.condition}\n\n")
            }
            tvHistoryContent.text = sb.toString().trimEnd()
        }
    }
}
