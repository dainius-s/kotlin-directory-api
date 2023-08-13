package com.h5templates.directory.datasource.mock

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

internal class MockUserDataSourceTest {

    private val mockDataSource = MockUserDataSource()

    @Nested
    @DisplayName("getUsers()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetUsers {
        @Test
        fun `should provide a collection of users`() {
            // when
            val users = mockDataSource.retrieveUsers()

            // then
            Assertions.assertThat(users).isNotEmpty
        }

        @Test
        fun `should provide valid mock data`() {
            // when
            val users = mockDataSource.retrieveUsers()

            // then
            Assertions.assertThat(users).allMatch { it.id > 0 }
            Assertions.assertThat(users).allMatch { it.name.isNotBlank() }
            Assertions.assertThat(users).allMatch { it.email.isNotBlank() }
        }

    }

    @Nested
    @DisplayName("getUser()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetUser {
        @Test
        fun `should provide a user with requested id`() {
            // given
            val id = 1

            // when
            val user = mockDataSource.retrieveUser(id)


            // then
            Assertions.assertThat(user).matches { it.id == 1 }

        }

        @Test
        fun `should provide an error when user with requested id not found`() {
            // given
            val id = 0

            // when - then
            assertThrows(
                NoSuchElementException::class.java,
                { mockDataSource.retrieveUser(id) },
                "User with id: $id could not be found"
            )
        }


    }
}