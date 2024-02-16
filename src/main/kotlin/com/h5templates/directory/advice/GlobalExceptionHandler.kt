package com.h5templates.directory.advice

import com.h5templates.directory.shared.validation.CustomValidationException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

data class ErrorResponse(
    val status: Int,
    val message: String,
    val errors: Map<String, String>
) {
    constructor() : this(0, "", emptyMap())
}

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [CustomValidationException::class, MethodArgumentNotValidException::class])
    fun handleEntityValidationException(exception: Exception): ResponseEntity<ErrorResponse> {
        val errors = mutableMapOf<String, String>()

        when (exception) {
            is CustomValidationException -> {
                extractErrors(exception.bindingResult, errors)
            }
            is MethodArgumentNotValidException -> {
                extractErrors(exception.bindingResult, errors)
            }
        }

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

    private fun extractErrors(bindingResult: BindingResult, errors: MutableMap<String, String>) {
        bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.getDefaultMessage()
            errors[fieldName] = errorMessage ?: "field is invalid"
        }
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message ?: "Resource not found", HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.message ?: "Bad request", HttpStatus.BAD_REQUEST)

}
