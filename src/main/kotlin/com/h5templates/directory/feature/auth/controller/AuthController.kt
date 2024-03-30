package com.h5templates.directory.feature.auth.controller

import com.h5templates.directory.feature.auth.model.LoginRequest
import com.h5templates.directory.feature.auth.model.RegisterRequest
import com.h5templates.directory.feature.auth.model.TokenResponse
import com.h5templates.directory.feature.role.model.RoleType
import com.h5templates.directory.shared.auth.JwtTokenProvider
import com.h5templates.directory.user.entity.User
import com.h5templates.directory.user.model.CreateUserDTO
import com.h5templates.directory.user.model.EntityDTO
import com.h5templates.directory.user.service.UserService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/api/auth")
class AuthController @Autowired constructor(
    private val userService: UserService,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerUser(@RequestBody @Valid registerRequest: RegisterRequest): User {

        val newUserDTO = CreateUserDTO(
            name = registerRequest.name,
            email = registerRequest.email,
            phone = registerRequest.phone,
            password = registerRequest.password,
            password_confirm = registerRequest.password_confirm,
            role = EntityDTO(RoleType.ADMIN.id),
            verified = false,
            active = true,
        )

        return userService.createUser(newUserDTO)
    }


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    fun loginUser(@RequestBody @Valid loginRequest: LoginRequest): TokenResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
        )

        val email = authentication.name
        val user = userService.findByEmail(email)
            ?: throw UsernameNotFoundException("User not found with email: $email")

        val token = jwtTokenProvider.createToken(user.email, user.role.permissions.map { it.name })

        return TokenResponse(token)
    }
}
