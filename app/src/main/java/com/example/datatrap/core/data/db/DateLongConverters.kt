package com.example.datatrap.core.data.db

import androidx.room.TypeConverter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DateLongConverters {
    @TypeConverter
    fun fromDateToLong(date: ZonedDateTime?): String?{
        return date?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    @TypeConverter
    fun fromLongToDate(date: String?): ZonedDateTime?{
        return if (date == null) null else ZonedDateTime.parse(date)
    }
}