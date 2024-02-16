package com.h5templates.directory.requests.user

import com.h5templates.directory.repository.UserRepository
import com.h5templates.directory.shared.validation.Unique
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Unique(repository = UserRepository::class, fieldName = "email")
data class UpdateUserDTO(
    var id: Int?,

    @field:NotEmpty
    @field:Size(max = 120)
    val name: String,

    @field:NotEmpty
    @field:Email
    @field:Size(max = 120)
    val email: String,

    @field:NotNull
    val verified: Boolean = false,

    @field:NotNull
    val active: Boolean = false
)