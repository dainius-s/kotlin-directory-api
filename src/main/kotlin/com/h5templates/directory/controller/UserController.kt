package com.h5templates.directory.controller

import com.h5templates.directory.model.User
import com.h5templates.directory.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun getUsers(): Collection<User> = userService.getUsers()

}