package com.example.datatrap.core.util

enum class EnumSex(val myName: String) {
    FEMALE("Female"),
    MALE("Male")
}

fun toEnumSex(sex: String?): EnumSex? {
    return when(sex) {
        null -> null
        EnumSex.FEMALE.myName -> EnumSex.FEMALE
        EnumSex.MALE.myName -> EnumSex.MALE
        else -> null
    }
}