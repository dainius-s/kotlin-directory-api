import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UKPhoneNumberValidator::class])
annotation class UKPhoneNumber(
    val message: String = "Invalid UK phone number",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)class UKPhoneNumberValidator : ConstraintValidator<UKPhoneNumber, String> {

    private val regex = """^(?:0|\+?44)(?:\d\s?){10,11}$""".toRegex()

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) return false
        return regex.matches(value)
    }
}
