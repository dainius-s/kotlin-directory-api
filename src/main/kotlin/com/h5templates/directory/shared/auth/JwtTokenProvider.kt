package com.h5templates.directory.shared.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtTokenProvider {
    @Value("\${security.jwt.token.secret-key:secret}")
    private val secretKey: String? = null

    @Value("\${security.jwt.token.expire-length:3600000}") // 1h in milliseconds

    private val validityInMilliseconds: Long = 3600000 // 1h in milliseconds

    // Generate a JWT token
    fun createToken(username: String?, role: String): String {
        val claims = Jwts.claims().setSubject(username)
        claims["auth"] = role
        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    // Validate the JWT Token
    fun validateToken(token: String?): Boolean {
        return try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            true
        } catch (e: Exception) {
            throw RuntimeException("Expired or invalid JWT token")
        }
    }

    fun getAuthentication(token: String, request: HttpServletRequest): Authentication {
        val claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body
        val username = claims.subject
        val authoritiesClaim = claims["auth"] as List<String>? // Assuming roles are stored as a list of strings
        val authorities = authoritiesClaim?.map { SimpleGrantedAuthority(it) } ?: listOf()

        val authentication = UsernamePasswordAuthenticationToken(username, null, authorities)
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

        return authentication
    }

    // Extract the username from the JWT token
    fun getUsername(token: String?): String {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
    }
}
