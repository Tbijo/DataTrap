package com.example.datatrap.utils

object Constants {
    const val OWM_API_KEY = "ba1d923dba4826c58fda121fb5e7a9de"
    const val OWM_API_KEY_URL = "&appid=$OWM_API_KEY"
    const val UNITS = "&units=metric"

    const val QUERRY_HISTORY_WEATHER_COOR =
        "https://api.openweathermap.org/data/2.5/onecall/timemachine?"

    //latitude
    const val SIRKA_URL = "lat="

    //longitude
    const val DLZKA_URL = "&lon="

    //unixtime
    const val DATE_TIME_URL = "&dt=" // len do poslednych piatich dni

    const val QUERRY_CURRENT_WEATHER_COOR = "https://api.openweathermap.org/data/2.5/weather?"

    private const val MILLIS_IN_SECOND = 1000L
    private const val SECONDS_IN_MINUTE = 60
    private const val MINUTES_IN_HOUR = 60
    private const val HOURS_IN_DAY = 24
    const val FIVE_DAYS =
        5 * HOURS_IN_DAY * MINUTES_IN_HOUR * SECONDS_IN_MINUTE * MILLIS_IN_SECOND


    private const val DAYS_IN_YEAR = 365
    const val MILLISECONDS_IN_2_YEAR: Long =
        2 * MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY * DAYS_IN_YEAR

    // SERVER
    const val BASE_URL = "http://192.168.0.27:8000/"
}