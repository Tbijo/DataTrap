package com.example.datatrap.mouse.domain.util

import com.example.datatrap.sync.utils.Error

enum class CodeGeneratorError: Error {
    INVALID_SPECIE_CODE,
    INVALID_CAPTURE_ID,
    NUMBER_OF_FINGERS_MISSING,
    LOCALITY_ID_MISSING,
}

class CodeGeneratorException(val error: CodeGeneratorError): Exception(
    "An error occurred during generating: $error"
)