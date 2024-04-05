package com.h5templates.directory.feature.auth.controller

import com.h5templates.directory.feature.auth.model.LoginRequest
import com.h5templates.directory.feature.auth.model.RegisterRequest
import com.h5templates.directory.feature.auth.model.TokenResponse
import com.h5templates.directory.feature.email.service.EmailService
import com.h5templates.directory.feature.role.model.RoleType
import com.h5templates.directory.shared.auth.JwtTokenProvider
import com.h5templates.directory.user.entity.User
import com.h5templates.directory.user.model.CreateUserDTO
import com.h5templates.directory.user.model.BaseEntityDTO
import com.h5templates.directory.user.service.UserService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.thymeleaf.context.Context

@RestController
@Validated
@RequestMapping("/api/auth")
class AuthController @Autowired constructor(
    private val userService: UserService,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val emailService: EmailService,
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
            role = BaseEntityDTO(RoleType.SUPER_ADMIN.id),
            verified = false,
            active = true,
        )

        val context = Context().apply {
            setVariable("logoUrl", "http://example.com/logo.png")
            setVariable("title", "Welcome!")
            setVariable("content", "Thank you for registering.")
            setVariable("actionUrl", "http://example.com/verify")
            setVariable("actionText", "Verify Your Account")
        }
        emailService.sendHtmlMessage("user@example.com", "Welcome to Our Service", "verify-email", context)


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
