package com.example.datatrap.core.util

object Constants {
    const val WEATHER_URL = "https://api.openweathermap.org/data/2.5/"
//    const val OWM_API_KEY = "ba1d923dba4826c58fda121fb5e7a9de"
//    const val OWM_API_KEY_URL = "&appid=$OWM_API_KEY"
//    const val UNITS = "&units=metric"
//    const val WEATHER_CURRENT = "weather?"
//    const val WEATHER_HISTORY = "onecall/timemachine?"
//    const val QUERRY_CURRENT_WEATHER_COOR = "https://api.openweathermap.org/data/2.5/weather?"
//    const val QUERRY_HISTORY_WEATHER_COOR = "https://api.openweathermap.org/data/2.5/onecall/timemachine?"
//    const val LATITUDE_URL = "lat="
//    const val LONGITUDE_URL = "&lon="
//    const val UNIXTIME_URL = "&dt=" // len do poslednych piatich dni

    private const val MILLIS_IN_SECOND = 1000L
    private const val SECONDS_IN_MINUTE = 60
    private const val MINUTES_IN_HOUR = 60
    private const val HOURS_IN_DAY = 24
    const val SECONDS_IN_FIVE_DAYS =
        5 * HOURS_IN_DAY * MINUTES_IN_HOUR * SECONDS_IN_MINUTE //* MILLIS_IN_SECOND

    private const val DAYS_IN_YEAR = 365
    const val SECONDS_IN_2_YEAR: Long =
        2L * DAYS_IN_YEAR * HOURS_IN_DAY * MINUTES_IN_HOUR * SECONDS_IN_MINUTE //* MILLIS_IN_SECOND

    // SERVER
    const val SERVER_URL = "http://192.168.0.27:8000/"
}