package com.example.datatrap.core.util

enum class EnumCaptureID(val myName: String) {
    CAPTURED("Captured"),
    DIED("Died"),
    ESCAPED("Escaped"),
    RELEASED("Released")
}

fun toEnumCaptureID(captureId: String?): EnumCaptureID? {
    return when(captureId) {
        null -> null
        EnumCaptureID.CAPTURED.myName -> EnumCaptureID.CAPTURED
        EnumCaptureID.DIED.myName -> EnumCaptureID.DIED
        EnumCaptureID.ESCAPED.myName -> EnumCaptureID.ESCAPED
        EnumCaptureID.RELEASED.myName -> EnumCaptureID.RELEASED
        else -> null
    }
}