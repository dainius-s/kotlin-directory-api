package com.h5templates.directory.shared.validation

import com.h5templates.directory.shared.spring.SpringContext
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UniqueValidator::class])
annotation class Unique(
    val message: String = "The value must be unique",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val repository: KClass<*>,
    val fieldName: String,
    val idFieldName: String = "id" // New parameter for specifying the ID field name
)

class UniqueValidator : ConstraintValidator<Unique, Any?> {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(UniqueValidator::class.java)
    }

    private lateinit var repository: Any
    private lateinit var fieldName: String
    private lateinit var idFieldName: String

    override fun initialize(constraintAnnotation: Unique) {
        repository = SpringContext.getBean(constraintAnnotation.repository.java)
        fieldName = constraintAnnotation.fieldName
        idFieldName = constraintAnnotation.idFieldName
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true // Consider null values as valid.
        }

        try {
            val kClass = value::class
            val fieldValue = kClass.memberProperties
                .firstOrNull { it.name.equals(fieldName, ignoreCase = true) }
                ?.getter?.call(value) as? String

            // Adjust this part to handle the ID as an Int
            val idValue = kClass.memberProperties
                .firstOrNull { it.name.equals(idFieldName, ignoreCase = true) }
                ?.getter?.call(value) as? Int

            if (fieldValue == null) {
                return false // Invalid if the field value is essential and not found.
            }

            val isUnique = if (idValue != null) {
                // Ensure the method signature matches the expected types (String and Int)
                val method = repository.javaClass.getMethod(
                    "existsByEmailAndIdNot",
                    String::class.java,
                    Int::class.java // Use javaObjectType for Int
                )
                !(method.invoke(repository, fieldValue, idValue) as Boolean)
            } else {
                val method = repository.javaClass.getMethod(
                    "existsByEmail",
                    String::class.java,
                )
                !(method.invoke(repository, fieldValue) as Boolean)
            }

            if (!isUnique) {
                context.disableDefaultConstraintViolation() // Disable the default message.
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(fieldName) // Attach the error to the specific field.
                    .addConstraintViolation()
                return false
            }

            return true

        } catch (e: NoSuchMethodException) {
            logger.error("Method not found during validation: ", e)
        } catch (e: IllegalAccessException) {
            logger.error("Illegal access during validation: ", e)
        } catch (e: InvocationTargetException) {
            logger.error("Invocation target exception during validation: ", e)
        } catch (e: Exception) {
            logger.error("Unexpected exception during validation: ", e)
        }

        // Optionally, return false or handle the validation error differently if an exception occurs
        return false
    }
}
