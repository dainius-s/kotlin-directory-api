package com.h5templates.directory.controller

import com.h5templates.directory.model.User
import com.h5templates.directory.requests.user.UserCreateRequest
import com.h5templates.directory.service.UserService
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@RestController
@Validated
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
    fun getUsers(
        @RequestParam(defaultValue = "0") @Min(0) page: Int,
        @RequestParam(defaultValue = "25", name="per_page") @Min(25) size: Int,
    ): Iterable<User> = userService.getUsers(PageRequest.of(page, size))

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Int) = userService.getUser(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody @Valid user: User): User = userService.createUser(user)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateUser(@PathVariable @Min(1) id: Int, @RequestBody @Valid user: User): User = userService.updateUser(id, user)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable @Min(1) id: Int): Unit = userService.deleteUser(id)

}