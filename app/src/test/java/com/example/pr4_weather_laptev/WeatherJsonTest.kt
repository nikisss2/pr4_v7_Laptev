package com.example.pr4_weather_laptev

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class WeatherJsonTest {

    private val gson = Gson()

    @Test
    fun testSerialization() {
        val entry = WeatherEntry("13.10.2026", "Москва", 15.5, "Ясно")
        val json = gson.toJson(entry)
        assertNotNull(json)
    }

    @Test
    fun testDeserialization() {
        val json = """{"date":"13.10.2026","city":"Москва","temperature":15.5,"condition":"Ясно"}"""
        val entry = gson.fromJson(json, WeatherEntry::class.java)
        assertEquals("Москва", entry.city)
        assertEquals(15.5, entry.temperature, 0.01)
        assertEquals("Ясно", entry.condition)
    }

    @Test
    fun testListSerializationDeserialization() {
        val list = listOf(
            WeatherEntry("13.10.2026", "Москва", 15.5, "Ясно"),
            WeatherEntry("14.10.2026", "Санкт-Петербург", 10.0, "Дождь")
        )
        val json = gson.toJson(list)
        val type = object : TypeToken<List<WeatherEntry>>() {}.type
        val parsedList: List<WeatherEntry> = gson.fromJson(json, type)
        assertEquals(2, parsedList.size)
        assertEquals("Санкт-Петербург", parsedList[1].city)
    }
}
