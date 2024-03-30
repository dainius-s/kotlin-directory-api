package com.h5templates.directory.configuration

import com.h5templates.directory.shared.auth.JwtTokenFilter
import com.h5templates.directory.shared.auth.JwtTokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider,
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager? {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors(Customizer.withDefaults()) // Enable CORS with default settings
            .csrf { csrf -> csrf.disable() } // Disable CSRF
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } // No session will be created or used by spring security
            .authorizeRequests { auth ->
                auth.requestMatchers(AntPathRequestMatcher("/api/auth/**")).permitAll() // Permit all requests to /api/**
                    .anyRequest().authenticated() // All other requests need to be authenticated
            }
            .addFilterBefore(JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java) // Add JWT token filter

        return http.build()
    }
}