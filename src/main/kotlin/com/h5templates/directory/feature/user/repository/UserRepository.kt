package com.h5templates.directory.feature.user.repository

import com.h5templates.directory.feature.user.entity.User
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository

interface UserRepository: CrudRepository<User, Int>, PagingAndSortingRepository<User, Int> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
    fun existsByEmailAndIdNot(email: String, id: Int): Boolean

    fun existsByPhone(phone: String): Boolean
    fun existsByPhoneAndIdNot(phone: String, id: Int): Boolean

}