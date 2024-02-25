package com.h5templates.directory.feature.user.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.h5templates.directory.user.entity.User
import com.h5templates.directory.user.repository.UserRepository
import com.h5templates.directory.user.model.CreateUserDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.justRun
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
internal class UserControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {
    val baseUrl = "/api/users"

    @MockkBean
    lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        clearMocks(userRepository)
    }

    @Nested
    @DisplayName("GET /api/users")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetUsers {

        @BeforeEach
        fun setUp() {
            val users = listOf(
                User(
                    1,
                    "John Doe",
                    "john.doe@example.com",
                    "07722000001",
                    "labas789",
                    true,
                    true,
                )
            )
            val pageOfUsers = PageImpl(users)
            every { userRepository.findAll(any<Pageable>()) } returns pageOfUsers
        }

        @Test
        fun `should return all users`() {
            // when
            val getUsers = mockMvc.get(baseUrl)

            // then
            getUsers.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.content[0].name") { value("John Doe") }
            }
        }
    }

    @Nested
    @DisplayName("GET /api/users/{id}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetUser {
        @BeforeEach
        fun setUp() {
            every { userRepository.findById(1) } returns Optional.of(
                User(
                    1,
                    "John Doe",
                    "john.doe@example.com",
                    "07722000001",
                    "labas789",
                    true,
                    true
                )
            )
            every { userRepository.findById(0) } returns Optional.empty()
        }

        @Test
        fun `should return the user with given id`() {
            // given
            val id = 1

            // when
            val getUser = mockMvc.get("$baseUrl/$id")

            // then
            getUser.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(id) }
            }
        }

        @Test
        fun `should return Not Found if the user with id does not exist`() {
            // given
            val id = 0

            // when
            val updateNotExistingUser = mockMvc.get("$baseUrl/$id")

            // then
            updateNotExistingUser.andExpect {
                status { isNotFound() }
            }
        }
    }

    @Nested
    @DisplayName("POST /api/users")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class CreateUser {
        @BeforeEach
        fun setUp() {
            every { userRepository.save(any()) } answers { firstArg() }
            every { userRepository.existsByEmail("joe.biederman@example.com") } returns false
            every { userRepository.existsByEmail("john.smith@example.com") } returns true
        }

        @Test
        fun `should create the new user`() {
            // given
            val payload = CreateUserDTO(
                "Joe Biederman",
                "joe.biederman@example.com",
                "07722000001",
                "labas789",
                "labas789",
                false,
                true,
            )

            // when
            val createUser = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(payload)
            }

            // then
            createUser.andExpect {
                status { isCreated() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.name") { value("Joe Biederman") }
                jsonPath("$.email") { value("joe.biederman@example.com") }
                jsonPath("$.verified") { value(false) }
                jsonPath("$.active") { value(true) }
            }

        }

        @Test
        fun `should return Validation Error if user with given email already exist`() {
            // given
            val payload = CreateUserDTO(
                "John Smith",
                "john.smith@example.com",
                "07722000001",
                "labas789",
                "labas789",
                false,
                true,
            )

            // when
            val createDuplicateEmailUser = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(payload)
            }

            // then
            createDuplicateEmailUser.andExpect {
                status { isUnprocessableEntity() }
            }
        }
    }

    @Nested
    @DisplayName("PUT /api/users/{id}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class UpdateUser {
        @BeforeEach
        fun setUp() {
            val existingUser = User(
                1,
                "Existing User",
                "existing@example.com",
                "07722000001",
                "labas789",
                true,
                true,
            )
            every { userRepository.findById(1) } returns Optional.of(existingUser)
            every { userRepository.findById(2) } returns Optional.empty() // Update to use ID=2 for non-existing user
            every { userRepository.existsByEmailAndIdNot("updated@example.com", 1) } returns false
            every { userRepository.existsByEmailAndIdNot("nonexisting@example.com", 2) } returns false
            every { userRepository.save(any()) } answers { firstArg() }
        }

        @Test
        fun `should update user with data provided`() {
            // given
            val id = 1
            val updatedUser = User(
                id,
                "Updated User",
                "updated@example.com",
                "07722000001",
                "labas789",
                false,
                true,

                )

            // when
            val updateUser = mockMvc.put("$baseUrl/$id") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updatedUser)
            }

            // then
            updateUser.andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(updatedUser))
                }
            }
        }

        @Test
        fun `should return Not Found if the user with id does not exist`() {
            // given
            val id = 2
            val nonExistingUser = User(
                id,
                "Non-Existing User",
                "nonexisting@example.com",
                "07722000001",
                "labas789",
                false,
                true,
            )

            // when
            val updateNotExistingUser = mockMvc.put("$baseUrl/$id") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(nonExistingUser)
            }

            // when
            updateNotExistingUser.andExpect {
                status { isNotFound() }
            }
        }
    }

    @Nested
    @DisplayName("DELETE /api/users/{id}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteUser {
        @BeforeEach
        fun setUp() {
            val existingUser = User(
                1,
                "Existing User",
                "existing@example.com",
                "07722000001",
                "labas789",
                true,
                true,
            )
            every { userRepository.findById(1) } returns Optional.of(existingUser)
            every { userRepository.findById(2) } returns Optional.empty()
            justRun { userRepository.delete(any<User>()) }
        }

        @Test
        fun `should delete user with id provided`() {
            // given
            val id = 1

            // when
            val deleteUser = mockMvc.delete("$baseUrl/$id")

            // then
            deleteUser.andExpect {
                status { isNoContent() }
            }
        }

        @Test
        fun `should return Not Found if the user with id does not exist`() {
            // given
            val id = 2

            // when
            val deleteNotExistingUser = mockMvc.delete("$baseUrl/$id")

            // then
            deleteNotExistingUser.andExpect {
                status { isNotFound() }
            }
        }
    }
}
