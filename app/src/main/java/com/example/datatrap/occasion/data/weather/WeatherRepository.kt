package com.example.datatrap.occasion.data.weather

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.core.util.Constants
import com.example.datatrap.mouse.domain.model.MyWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar

class WeatherRepository {

    // Volley
    val weatherCoor: MutableLiveData<MyWeather> = MutableLiveData<MyWeather>()
    val errorWeather: MutableLiveData<String> = MutableLiveData<String>()

    fun getHistoricalWeatherByCoordinates(sirka: Float, dlzka: Float, unixTime: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentTime = Calendar.getInstance().time.time
            if (currentTime - unixTime >= Constants.FIVE_DAYS) {
                errorWeather.value = "Occasion is older than 5 days."
                return@launch
            }

            val url =
                Constants.QUERRY_HISTORY_WEATHER_COOR + Constants.SIRKA_URL + sirka + Constants.DLZKA_URL + dlzka + Constants.DATE_TIME_URL + (unixTime / 1000) + Constants.UNITS + Constants.OWM_API_KEY_URL

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

    fun getCurrentWeatherByCoordinates(sirka: Float, dlzka: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            val url =
                Constants.QUERRY_CURRENT_WEATHER_COOR + Constants.SIRKA_URL + sirka + Constants.DLZKA_URL + dlzka + Constants.UNITS + Constants.OWM_API_KEY_URL

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

    private fun getHistoryWeather() {
        if (isOnline(requireContext())) {
            val locality = args.locList

            if (locality.xA == null || locality.yA == null){
                Toast.makeText(requireContext(), "No coordinates in this locality.", Toast.LENGTH_LONG).show()
                return
            }
            val unixtime = currentOccasionEntity.occasionDateTimeCreated.time

            volleyViewModel.getHistoricalWeatherByCoordinates(locality.xA!!, locality.yA!!, unixtime)
        }else{
            Toast.makeText(requireContext(), "Connect to Internet.", Toast.LENGTH_LONG).show()
        }
    }

    private fun getCurrentWeather() {
        if (isOnline(requireContext())) {

            if (args.locList.xA == null || args.locList.yA == null) {
                Toast.makeText(requireContext(), "No coordinates in this locality.", Toast.LENGTH_LONG).show()
                return
            }

            volleyViewModel.getCurrentWeatherByCoordinates(args.locList.xA!!, args.locList.yA!!)
        } else {
            Toast.makeText(requireContext(), "Connect to Internet.", Toast.LENGTH_LONG).show()
        }
    }
}