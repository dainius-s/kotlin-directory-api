package com.h5templates.directory;

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class UsersController {

    @GetMapping
    fun index() = listOf<String>(
        "denis!",
        "menace",
    )
}

