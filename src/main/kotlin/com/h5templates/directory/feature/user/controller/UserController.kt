package com.h5templates.directory.user.controller

import com.h5templates.directory.user.entity.User
import com.h5templates.directory.user.model.CreateUserDTO
import com.h5templates.directory.user.model.UpdateUserDTO
import com.h5templates.directory.user.service.UserService
import com.h5templates.directory.shared.validation.ValidationService
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@RestController
@Validated
@RequestMapping("/api/users")
class UserController(
    private val validationService: ValidationService,
    private val userService: UserService
) {
    @GetMapping
    @PreAuthorize("hasAuthority('users search')")
    fun getUsers(
        @RequestParam(defaultValue = "0") @Min(0) page: Int,
        @RequestParam(defaultValue = "25", name="per_page") @Min(25) size: Int,
    ): Iterable<User> = userService.getUsers(PageRequest.of(page, size))

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('users get_any')")
    fun getUser(@PathVariable id: Int) = userService.getUser(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('users create')")
    fun createUser(@RequestBody @Valid user: CreateUserDTO): User = userService.createUser(user)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('users update')")
    fun updateUser(
        @PathVariable @Min(1) id: Int,
        @RequestBody user: UpdateUserDTO,
    ): User {
        user.id = id
        validationService.validate(user, "UpdateUserDTO")

        return userService.updateUser(id, user)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('users delete')")
    fun deleteUser(@PathVariable @Min(1) id: Int): Unit = userService.deleteUser(id)

}