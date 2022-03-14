package com.example.datatrap.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.example.datatrap.models.MyWeather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import javax.inject.Inject

@HiltViewModel
class VolleyViewModel @Inject constructor(
    private val reqQue: RequestQueue
) : ViewModel() {

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

    val weatherCoor: MutableLiveData<MyWeather> = MutableLiveData<MyWeather>()
    val errorWeather: MutableLiveData<String> = MutableLiveData<String>()

    fun getCurrentWeatherByCoordinates(sirka : Float, dlzka : Float) {
        viewModelScope.launch(Dispatchers.IO) {
            val url =
                QUERRY_CURRENT_WEATHER_COOR + SIRKA_URL + sirka + DLZKA_URL + dlzka + UNITS + OWM_API_KEY_URL

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->

                    val mainJSON = response["main"] as JSONObject
                    val temp = mainJSON.getInt("temp")

                    val weatherArray = response.getJSONArray("weather") as JSONArray
                    val weatherJSON = weatherArray.getJSONObject(0)
                    val weather = weatherJSON.getString("main")

                    weatherCoor.value = MyWeather(temp, weather)
                },
                {
                    errorWeather.value = "Error in RESPONSE"
                }
            )
            reqQue.add(jsonObjectRequest)
        }
    }

    fun getHistoricalWeatherByCoordinates(sirka : Float, dlzka : Float, unixTime: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentTime = Calendar.getInstance().time.time
            if (currentTime - unixTime >= FIVE_DAYS){
                errorWeather.value = "Occasion is older than 5 days."
                return@launch
            }

            val url =
                QUERRY_HISTORY_WEATHER_COOR + SIRKA_URL + sirka + DLZKA_URL + dlzka + DATE_TIME_URL + (unixTime / 1000)  + UNITS + OWM_API_KEY_URL

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->

                    val mainJSON = response["current"] as JSONObject
                    val temp = mainJSON.getInt("temp")

                    val weatherArray = mainJSON.getJSONArray("weather") as JSONArray
                    val weatherJSON = weatherArray.getJSONObject(0)
                    val weather = weatherJSON.getString("main")

                    weatherCoor.value = MyWeather(temp, weather)
                },
                {
                    errorWeather.value = "Error in RESPONSE"
                }
            )
            reqQue.add(jsonObjectRequest)
        }
    }

}