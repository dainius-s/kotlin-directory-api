package com.h5templates.directory.shared.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey


@Component
class JwtTokenProvider {
    @Value("\${security.jwt.token.secret-key:secret}")
    private lateinit var base64Secret: String

    private lateinit var key: SecretKey

    @Value("\${security.jwt.token.expire-length:3600000}") // 1 hour in milliseconds
    private val validityInMilliseconds: Long = 3600000

    @PostConstruct
    fun init() {
        val decodedKey = Base64.getDecoder().decode(base64Secret)
        key = Keys.hmacShaKeyFor(decodedKey)
    }

    // Generate a JWT token with permissions
    fun createToken(username: String?, permissions: List<String>): String {
        val claims = Jwts.claims().setSubject(username)
        claims["auth"] = permissions // Storing permissions in the auth claim
        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    // Validate the JWT Token
    fun validateToken(token: String?): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            true
        } catch (e: Exception) {
            throw RuntimeException("Expired or invalid JWT token", e)
        }
    }

    // Get Authentication from the JWT Token using permissions
    fun getAuthentication(token: String, request: HttpServletRequest): Authentication {
        val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
        val username = claims.subject
        val permissionsClaim = claims["auth"] as List<String>? // Permissions are now expected here
        val authorities = permissionsClaim?.map { SimpleGrantedAuthority(it) } ?: listOf()

        val authentication = UsernamePasswordAuthenticationToken(username, null, authorities)
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

        return authentication
    }

    // Extract the username from the JWT token
    fun getUsername(token: String?): String {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body.subject
    }
}
