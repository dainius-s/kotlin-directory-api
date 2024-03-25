package com.h5templates.directory.shared.utilities

import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.*


//object GenerateKey {
//    @JvmStatic
//    fun main(args: Array<String>) {
//        val key = Keys.secretKeyFor(SignatureAlgorithm.HS256) // Generate the key
//        val encodedKey = Base64.getEncoder().encodeToString(key.encoded) // Encode as string
//        println("Secure key for HS256: $encodedKey")
//    }
//}