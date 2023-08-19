package com.example.datatrap.core.presentation.util

import androidx.annotation.StringRes

data class ValidationResult(
    // validation
    val successful: Boolean,
    // message only exists if validation failed
    @StringRes val errorMessage: Int? = null
)