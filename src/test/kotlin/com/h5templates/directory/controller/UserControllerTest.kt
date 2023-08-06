package com.h5templates.directory.controller

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
internal class UserControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    val baseUrl = "/api/users"

    @Nested
    @DisplayName("getUsers()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetUsers {
        @Test
        fun `should return all users`() {
            // when
            val response = mockMvc.get(baseUrl)

            // then
            response.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$[0].name") { value("John Smith") }
            }

        }
    }

    @Nested
    @DisplayName("getUser()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetUser {
        @Test
        fun `should return the bank with given id`() {
            // given
            val id = 1;
            // when

            val response = mockMvc.get("$baseUrl/$id")

            // then
            response.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath(".id") { value(id) }
            }
        }

        @Test
        fun `should return Not Found if the user with id does not exist`() {
            // given
            val id = 0

            // when
            val response = mockMvc.get("$baseUrl/$id")

            // then
            response
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
    }

}