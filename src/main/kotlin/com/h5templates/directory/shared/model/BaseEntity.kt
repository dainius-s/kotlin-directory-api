package com.h5templates.directory.user.model

import jakarta.persistence.Id


open class BaseEntity(
    @Id
    open val id: Int? = null
)