package com.h5templates.directory.repository

import com.h5templates.directory.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository

interface UserRepository: CrudRepository<User, Int>, PagingAndSortingRepository<User, Int> {
    fun findByEmail(email: String): User?
}