package com.h5templates.directory.shared.model

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class AbstractModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open val id: Int = 0
}