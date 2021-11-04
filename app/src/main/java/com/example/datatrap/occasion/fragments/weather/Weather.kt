package com.example.datatrap.occasion.fragments.weather

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class Weather(val context: Context) {
    private val OWM_API_KEY = "ba1d923dba4826c58fda121fb5e7a9de"
    private val OWM_API_KEY_URL = "&appid=$OWM_API_KEY"
    private val UNITS = "&units=metric"

    private val QUERRY_HISTORY_WEATHER_COOR =
        "https://api.openweathermap.org/data/2.5/onecall/timemachine?"

    //latitude
    private val SIRKA_URL = "lat="

    //longitude
    private val DLZKA_URL = "&lon="

    //unixtime
    private val DATE_TIME_URL = "&dt=" // len do poslednych piatich dni

    private val QUERRY_CURRENT_WEATHER_COOR = "https://api.openweathermap.org/data/2.5/weather?"

    private val MILLIS_IN_SECOND = 1000L
    private val SECONDS_IN_MINUTE = 60
    private val MINUTES_IN_HOUR = 60
    private val HOURS_IN_DAY = 24
    private val FIVE_DAYS = 5 * HOURS_IN_DAY * MINUTES_IN_HOUR * SECONDS_IN_MINUTE * MILLIS_IN_SECOND

    fun getCurrentWeatherByCoordinates(sirka : Float, dlzka : Float, listener: VolleyResponseListener){

        val url =
            QUERRY_CURRENT_WEATHER_COOR + SIRKA_URL + sirka + DLZKA_URL + dlzka + UNITS + OWM_API_KEY_URL

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                val mainJSON = response["main"] as JSONObject
                val temp = mainJSON.getInt("temp")

                val weatherArray = response.getJSONArray("weather") as JSONArray
                val pocasieJSON = weatherArray.getJSONObject(0)
                val pocasie = pocasieJSON.getString("main")

                listener.onResponse(temp, pocasie)

                Log.d("WEATHER" ,"Response succesful.")
            },
            { _ ->
                listener.onError("Error in response.")

                Log.d("WEATHER" ,"Error in response.")
            }
        )

        // Access the RequestQueue through your singleton class.
        SingletonVolley.getInstance(context).addToRequestQueue(jsonObjectRequest)
    }

    fun getHistoricalWeatherByCoordinates(sirka : Float, dlzka : Float, unixTime: Long, listener: VolleyResponseListener){

        val currentTime = Calendar.getInstance().time.time
        if (currentTime - unixTime >= FIVE_DAYS){
            Log.d("Weather", "$currentTime - $unixTime >= $FIVE_DAYS")
            Toast.makeText(context, "Occasion is older than 5 days.", Toast.LENGTH_LONG).show()
            return
        }

        val url =
            QUERRY_HISTORY_WEATHER_COOR + SIRKA_URL + sirka + DLZKA_URL + dlzka + DATE_TIME_URL + (unixTime / 1000)  + UNITS + OWM_API_KEY_URL

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                val mainJSON = response["current"] as JSONObject
                val temp = mainJSON.getInt("temp")

                val weatherArray = mainJSON.getJSONArray("weather") as JSONArray
                val pocasieJSON = weatherArray.getJSONObject(0)
                val pocasie = pocasieJSON.getString("main")

                listener.onResponse(temp, pocasie)

                Log.d("WEATHER" ,"History response succesful.")
            },
            { _ ->
                listener.onError("Error in history response.")
                Log.d("WEATHER" ,"Error in history response.")
            }
        )

        // Access the RequestQueue through your singleton class.
        SingletonVolley.getInstance(context).addToRequestQueue(jsonObjectRequest)
    }

    interface VolleyResponseListener{
        fun onResponse(temp: Int, weather: String)
        fun onError(message: String)
    }
}