package com.h5templates.directory.shared.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import kotlin.reflect.full.memberProperties

@Target(AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [FieldsValueMatchValidator::class])
annotation class FieldsValueMatch(
    val message: String = "Fields values don't match",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val field: String,
    val fieldMatch: String
)
class FieldsValueMatchValidator : ConstraintValidator<FieldsValueMatch, Any?> {

    private lateinit var field: String
    private lateinit var fieldMatch: String

    override fun initialize(constraintAnnotation: FieldsValueMatch) {
        field = constraintAnnotation.field
        fieldMatch = constraintAnnotation.fieldMatch
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true // Consider null values as valid to allow optional fields.
        }

        val kClass = value::class
        val fieldValue = kClass.memberProperties
            .firstOrNull { it.name.equals(field, ignoreCase = true) }
            ?.getter?.call(value)

        val fieldMatchValue = kClass.memberProperties
            .firstOrNull { it.name.equals(fieldMatch, ignoreCase = true) }
            ?.getter?.call(value)

        val isValid = fieldValue == fieldMatchValue

        if (!isValid) {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode(fieldMatch)
                .addConstraintViolation()
        }

        return isValid
    }
}
