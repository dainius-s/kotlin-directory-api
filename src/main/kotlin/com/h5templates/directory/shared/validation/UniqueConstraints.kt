package com.h5templates.directory.shared.validation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class UniqueConstraints(
    val value: Array<Unique>
)
