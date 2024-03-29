package com.h5templates.directory.shared.auth

import com.h5templates.directory.user.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    @Autowired private val userRepository: UserRepository,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("User not found: $username")

        val authorities = user.role.permissions.map { permission ->
            SimpleGrantedAuthority(permission.name)
        }.toSet()

        // Here, 'User' refers to 'org.springframework.security.core.userdetails.User'
        return User.withUsername(user.email)
            .password(user.password)
            .authorities(authorities)
            .build()
    }
}