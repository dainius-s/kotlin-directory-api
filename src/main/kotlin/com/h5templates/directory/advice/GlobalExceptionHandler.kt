package com.h5templates.directory.advice

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.util.function.Consumer

data class ErrorResponse(
    val status: Int,
    val message: String,
    val errors: Map<String, String>
) {
    constructor() : this(0, "", emptyMap())
}

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleEntityValidationException(exception: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errors = mutableMapOf<String, String>()

        exception.bindingResult.allErrors.forEach(Consumer { error: ObjectError ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.getDefaultMessage()
            errors[fieldName] = errorMessage ?: "field is invalid"
        })

        val message = errors.entries.joinToString(", ") { "${it.key}: ${it.value}" }

        val errorResponse = ErrorResponse(
            status = HttpStatus.UNPROCESSABLE_ENTITY.value(),
            message = message,
            errors = errors
        )

        return ResponseEntity(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleControllerViolationException(exception: ConstraintViolationException): ResponseEntity<ErrorResponse> {
        val errors = mutableMapOf<String, String>()

        for (violation in exception.constraintViolations) {
            val propertyPath = violation.propertyPath.toString().substringAfter(".")
            val errorMessage = violation.message
            errors[propertyPath] = errorMessage
        }

        val message = errors.entries.joinToString(", ") { "${it.key}: ${it.value}" }

        val errorResponse = ErrorResponse(
            status = HttpStatus.UNPROCESSABLE_ENTITY.value(),
            message = message,
            errors = errors
        )

        return ResponseEntity(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY)
    }
}
