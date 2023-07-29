package com.example.datatrap.occasion.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.example.datatrap.mouse.data.MyWeather
import com.example.datatrap.core.util.Constants.DATE_TIME_URL
import com.example.datatrap.core.util.Constants.DLZKA_URL
import com.example.datatrap.core.util.Constants.FIVE_DAYS
import com.example.datatrap.core.util.Constants.OWM_API_KEY_URL
import com.example.datatrap.core.util.Constants.QUERRY_CURRENT_WEATHER_COOR
import com.example.datatrap.core.util.Constants.QUERRY_HISTORY_WEATHER_COOR
import com.example.datatrap.core.util.Constants.SIRKA_URL
import com.example.datatrap.core.util.Constants.UNITS
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

    val weatherCoor: MutableLiveData<MyWeather> = MutableLiveData<MyWeather>()
    val errorWeather: MutableLiveData<String> = MutableLiveData<String>()

    fun getCurrentWeatherByCoordinates(sirka: Float, dlzka: Float) {
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
                    errorWeather.value = "Error in Response."
                }
            )
            reqQue.add(jsonObjectRequest)
        }
    }

    fun getHistoricalWeatherByCoordinates(sirka: Float, dlzka: Float, unixTime: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentTime = Calendar.getInstance().time.time
            if (currentTime - unixTime >= FIVE_DAYS) {
                errorWeather.value = "Occasion is older than 5 days."
                return@launch
            }

            val url =
                QUERRY_HISTORY_WEATHER_COOR + SIRKA_URL + sirka + DLZKA_URL + dlzka + DATE_TIME_URL + (unixTime / 1000) + UNITS + OWM_API_KEY_URL

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
                    errorWeather.value = "Error in Response."
                }
            )
            reqQue.add(jsonObjectRequest)
        }
    }

}