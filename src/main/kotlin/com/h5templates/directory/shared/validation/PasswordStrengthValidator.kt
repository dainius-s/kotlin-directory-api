package com.h5templates.directory.shared.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PasswordStrengthValidator::class])
annotation class PasswordStrength(
    val message: String = "Password must contain at least one number and any non-number characters",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Payload>> = []
)
class PasswordStrengthValidator : ConstraintValidator<PasswordStrength, String> {

    private val regex = Regex("^(?=.*\\d)(?=.*[a-zA-Z!@#$%^&*])(?!.*\\s).{6,}$")

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) return false // Consider null values as invalid.
        return regex.matches(value)
    }
}