package com.h5templates.directory.feature.auth.model

import UKPhoneNumber
import com.h5templates.directory.shared.validation.FieldsValueMatch
import com.h5templates.directory.shared.validation.PasswordStrength
import com.h5templates.directory.shared.validation.Unique
import com.h5templates.directory.shared.validation.UniqueConstraints
import com.h5templates.directory.user.repository.UserRepository
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

@UniqueConstraints(
    arrayOf(
        Unique(repository = UserRepository::class, fieldName = "email"),
        Unique(repository = UserRepository::class, fieldName = "phone"),
    )
)
@FieldsValueMatch(field = "password", fieldMatch = "password_confirm", message = "Password and confirm password must match")
data class RegisterRequest(
    @field:NotEmpty
    @field:Size(max = 120)
    val name: String,

    @field:NotEmpty
    @field:Email
    @field:Size(max = 120)
    val email: String,

    @field:NotEmpty
    @field:Size(max = 13)
    @field:UKPhoneNumber
    val phone: String,

    @field:NotEmpty
    @field:Size(min = 6)
    @field:PasswordStrength
    val password: String,

    @field:NotEmpty
    val password_confirm: String,
)