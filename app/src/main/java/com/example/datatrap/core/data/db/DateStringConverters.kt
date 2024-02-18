package com.example.datatrap.core.data.db

import androidx.room.TypeConverter
import java.time.ZonedDateTime

class DateStringConverters {
    @TypeConverter
    fun fromDateToString(dateTime: ZonedDateTime?): String? {
        return dateTime?.toString()
    }

    @TypeConverter
    fun fromStringToDate(dateTime: String?): ZonedDateTime? {
        return dateTime?.let {
            ZonedDateTime.parse(dateTime)
        }
    }
}