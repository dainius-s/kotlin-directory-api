package com.h5templates.directory.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.h5templates.directory.model.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
internal class UserControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
) {
    val baseUrl = "/api/users"

    @Nested
    @DisplayName("GET /api/users")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetUsers {
        @Test
        fun `should return all users`() {
            // when
            val getUsers = mockMvc.get(baseUrl)

            // then
            getUsers.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$[0].name") { value("John Smith") }
            }

        }
    }

    @Nested
    @DisplayName("GET /api/users/{id}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetUser {
        @Test
        fun `should return the bank with given id`() {
            // given
            val id = 1;
            // when

            val getUser = mockMvc.get("$baseUrl/$id")

            // then
            getUser.andExpect {
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
            val updateNotExistingUser = mockMvc.get("$baseUrl/$id")

            // then
            updateNotExistingUser
                .andExpect {
                    status { isNotFound() }
                }
        }
    }

    @Nested
    @DisplayName("POST /api/users")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class CreateUser {

        @Test
        fun `should create the new user`() {
            // given
            val payload = User(
                10,
                "Joe Biederman",
                "joe.biederman@example.com",
                false,
                true,
            )

            // when
            val createUser = mockMvc.post(baseUrl, payload) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(payload)
            }


            // then
            createUser
                .andExpect {
                    status { isCreated() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.name") { value("Joe Biederman") }
                    jsonPath("$.email") { value("joe.biederman@example.com") }
                    jsonPath("$.verified") { value(false) }
                    jsonPath("$.active") { value(true) }
                }

        }

        @Test
        fun `should return BAD_REQUEST if user with given email already exist`() {
            // given
            val payload = User(
                10,
                "John Smith",
                "john.smith@example.com",
                false,
                true,
            )

            // when
            val createDuplicateEmailUser = mockMvc.post(baseUrl, payload) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(payload)
            }

            // then
            createDuplicateEmailUser
                .andExpect {
                    status { isBadRequest() }
                }
        }

    }

    @Nested
    @DisplayName("PUT /api/users/{id}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class UpdateUser {

        @Test
        fun `should update user with data provided`() {
            // given
            val id = 1

            val payload = User(
                id,
                "Joe Biederman",
                "joe.biederman@example.com",
                false,
                true,
            )

            // when
            val updateUser = mockMvc.put("$baseUrl/$id") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(payload)
            }

            // then
            updateUser
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(payload))
                    }
                }
        }

        @Test
        fun `should return Not Found if the user with id does not exist`() {
            // given
            val id = 0
            val payload = User(
                id,
                "Joe Biederman",
                "joe.biederman@example.com",
                false,
                true,
            )

            // when
            val updateNotExistingUser = mockMvc.put("$baseUrl/$id") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(payload)
            }

            // then
            updateNotExistingUser
                .andExpect {
                    status { isNotFound() }
                }
        }

    }

    @Nested
    @DisplayName("DELETE /api/users/{id}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteUser {

        @Test
        fun `should delete user with id provided`() {
            // given
            val id = 1

            // when
            val deleteUser = mockMvc.delete("$baseUrl/$id")


            // then
            deleteUser
                .andExpect {
                    status { isNoContent() }
                }
        }

    }

    @Test
    fun `should return Not Found if the user with id does not exist`() {
        // given
        val id = 0

        // when
        val deleteNotExistingUser = mockMvc.delete("$baseUrl/$id")

        // then
        deleteNotExistingUser
            .andExpect {
                status { isNotFound() }
            }
    }

}