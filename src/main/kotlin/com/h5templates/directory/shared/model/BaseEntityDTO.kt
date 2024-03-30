package com.h5templates.directory.user.model

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class BaseEntityDTO(
    @field:NotNull
    @field:Size(min = 1)
    val id: Int
)