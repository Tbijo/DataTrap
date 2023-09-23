package com.example.datatrap.core.util

enum class EnumMouseAge(val myName: String) {
    ADULT("Adult"),
    JUVENILE("Juvenile"),
    SUBADULT("Subadult")
}

fun toEnumMouseAge(age: String?): EnumMouseAge? {
    return when(age) {
        null -> null
        EnumMouseAge.JUVENILE.myName -> EnumMouseAge.JUVENILE
        EnumMouseAge.SUBADULT.myName -> EnumMouseAge.SUBADULT
        EnumMouseAge.ADULT.myName -> EnumMouseAge.ADULT
        else -> null
    }
}