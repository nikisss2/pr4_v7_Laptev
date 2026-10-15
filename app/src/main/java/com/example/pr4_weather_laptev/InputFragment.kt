package com.example.pr4_weather_laptev

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InputFragment : Fragment() {

    private lateinit var etDate: EditText
    private lateinit var etCity: EditText
    private lateinit var etTemp: EditText
    private lateinit var spinnerCondition: Spinner
    private lateinit var btnSave: AppCompatButton
    private lateinit var btnViewHistory: AppCompatButton
    private lateinit var btnLogout: AppCompatButton

    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_input, container, false)

        etDate = view.findViewById(R.id.etDate)
        etCity = view.findViewById(R.id.etCity)
        etTemp = view.findViewById(R.id.etTemp)
        spinnerCondition = view.findViewById(R.id.spinnerCondition)
        btnSave = view.findViewById(R.id.btnSave)
        btnViewHistory = view.findViewById(R.id.btnViewHistory)
        btnLogout = view.findViewById(R.id.btnLogout)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.weather_conditions,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCondition.adapter = adapter
        }

        val currentDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
        etDate.setText(currentDate)

        btnSave.setOnClickListener {
            val date = etDate.text.toString().trim()
            val city = etCity.text.toString().trim()
            val tempStr = etTemp.text.toString().trim()
            val condition = spinnerCondition.selectedItem?.toString() ?: ""

            if (date.isEmpty() || city.isEmpty() || tempStr.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.toast_fill_all_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val temp = tempStr.toDoubleOrNull() ?: 0.0
            val entry = WeatherEntry(date, city, temp, condition)

            val prefs = requireContext().getSharedPreferences("WeatherAppPrefs", Context.MODE_PRIVATE)
            val json = prefs.getString("weather_list", "[]")
            val type = object : TypeToken<ArrayList<WeatherEntry>>() {}.type
            val list: ArrayList<WeatherEntry> = gson.fromJson(json, type) ?: ArrayList()

            list.add(entry)

            val updatedJson = gson.toJson(list)
            prefs.edit().putString("weather_list", updatedJson).apply()

            Toast.makeText(requireContext(), getString(R.string.toast_saved_success), Toast.LENGTH_SHORT).show()

            etCity.setText("")
            etTemp.setText("")
        }

        btnViewHistory.setOnClickListener {
            (activity as? MainActivity)?.showFragment(HistoryFragment())
        }

        btnLogout.setOnClickListener {
            val prefs = requireContext().getSharedPreferences("WeatherAppPrefs", Context.MODE_PRIVATE)
            prefs.edit().remove("username").remove("password").apply()
            (activity as? MainActivity)?.showFragment(AuthFragment())
        }

        return view
    }
}
