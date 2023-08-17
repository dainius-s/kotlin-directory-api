package com.h5templates.directory.controller

import com.h5templates.directory.model.User
import com.h5templates.directory.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

    @GetMapping
    fun getUsers(): Collection<User> = userService.getUsers()

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Int) = userService.getUser(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody user: User): User = userService.createUser(user)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateUser(@PathVariable id: Int, @RequestBody user: User): User = userService.updateUser(id, user)

}