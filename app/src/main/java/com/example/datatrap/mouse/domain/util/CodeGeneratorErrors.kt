package com.example.datatrap.mouse.domain.util

enum class CodeGeneratorError {
    INVALID_SPECIE_CODE,
    INVALID_CAPTURE_ID,
    NUMBER_OF_FINGERS_MISSING,
    LOCALITY_ID_MISSING,
}

class CodeGeneratorException(val error: CodeGeneratorError): Exception(
    "An error occurred during generating: $error"
)