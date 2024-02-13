package com.h5templates.directory.shared.validation

import com.h5templates.directory.shared.spring.SpringContext
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UniqueValidator::class])
annotation class Unique(
    val message: String = "value must be unique",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val repository: KClass<*>,
    val fieldName: String,
)

class UniqueValidator : ConstraintValidator<Unique, Any?> {

    private lateinit var repository: Any
    private lateinit var fieldName: String

    override fun initialize(constraintAnnotation: Unique) {
        repository = SpringContext.getBean(constraintAnnotation.repository.java)
        fieldName = constraintAnnotation.fieldName
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) {
            return true // null values are considered valid
        }

        return try {
            val method = repository.javaClass.getMethod("existsBy$fieldName", value::class.java)
            !(method.invoke(repository, value) as Boolean)
        } catch (e: Exception) {
            // Log the exception
            false
        }
    }
}
