package com.example.datatrap.sync.utils

import io.ktor.http.ContentType

fun ContentType.properText(): String {
    return "$contentType/$contentSubtype"
}