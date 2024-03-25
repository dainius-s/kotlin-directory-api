package com.h5templates.directory.feature.auth.model

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class LoginRequest(
    @field:NotEmpty
    @field:Email
    @field:Size(max = 120)
    val email: String,

    @field:NotEmpty
    @field:Size(min = 6)
    val password: String,
)