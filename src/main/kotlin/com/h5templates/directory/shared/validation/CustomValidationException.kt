package com.h5templates.directory.shared.validation

import org.springframework.validation.BindingResult

class CustomValidationException(val bindingResult: BindingResult) : RuntimeException()
