package com.h5templates.directory.model

import com.h5templates.directory.shared.AbstractModel
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id


@Entity
data class User(
    @Id @GeneratedValue override val id: Int,
    val name: String,
    val email: String,
    val verified: Boolean = false,
    val active: Boolean = false,
): AbstractModel