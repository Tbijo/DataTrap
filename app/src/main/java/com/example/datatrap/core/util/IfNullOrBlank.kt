package com.example.datatrap.core.util

@Suppress("BOUNDS_NOT_ALLOWED_IF_BOUNDED_BY_TYPE_PARAMETER")
inline fun <C, R> C.ifNullOrBlank(defaultValue: () -> R): R where C : CharSequence?, C : R =
    if (this.isNullOrBlank()) defaultValue() else this