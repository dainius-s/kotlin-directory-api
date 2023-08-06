package com.h5templates.directory.datasource.mock

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MockUserDataSourceTest {

    private val mockDataSource = MockUserDataSource()

    @Test
    fun `should provide a collection of users`() {
        // given

        
        // when
        val banks = mockDataSource.getUsers()
        
        
        // then
        Assertions.assertThat(banks).isNotEmpty
    }

    @Test
    fun `should provide valid mock data`() {
        // given


        // when
        val banks = mockDataSource.getUsers()

        // then
        Assertions.assertThat(banks).allMatch { it.id > 0 }
        Assertions.assertThat(banks).allMatch { it.name.isNotBlank() }
        Assertions.assertThat(banks).allMatch { it.email.isNotBlank() }
    }
}