package dev.yuyuyuyuyu.toleet.domain.useCase

import kotlin.test.Test
import kotlin.test.assertEquals

class ToLeetUseCaseTest {
    private val useCase = ToLeetUseCase()

    @Test
    fun `invoke should replace vowels with numbers`() {
        // Arrange
        val s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        val expected = "4BCD3FGH1JKLMN0PQRSTUVWXYZ4bcd3fgh1jklmn0pqrstuvwxyz"

        // Act
        val actual = useCase(s)

        // Assert
        assertEquals(expected, actual)
    }
}
