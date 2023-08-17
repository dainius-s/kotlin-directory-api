package com.h5templates.directory.model


data class User(
    override val id: Int,
    val name: String,
    val email: String,
    val verified: Boolean = false,
    val active: Boolean = false,
): Entity