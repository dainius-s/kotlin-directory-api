package com.h5templates.directory.shared.validation

import org.springframework.stereotype.Service
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Validator
@Service
class ValidationService(private val validator: Validator) {

    fun <T : Any> validate(dto: T, dtoName: String) {
        val errors = BeanPropertyBindingResult(dto, dtoName)
        validator.validate(dto, errors)

        if (errors.hasErrors()) {
            // Construct and throw a specific exception for validation failures
            throw CustomValidationException(errors)
        }
    }
}