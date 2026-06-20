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

    @Test
    fun `invoke returns empty string for empty input`() {
        assertEquals("", useCase(""))
    }

    @Test
    fun `invoke converts vowels in a sentence and leaves other characters untouched`() {
        assertEquals("H3ll0, W0rld!", useCase("Hello, World!"))
    }

    @Test
    fun `invoke leaves non-ASCII characters unchanged`() {
        // Only ASCII a, e, i, o are converted; everything else passes through.
        assertEquals("こんにちは", useCase("こんにちは"))
    }

    @Test
    fun `invoke leaves emoji and surrogate pairs intact`() {
        // An emoji is a surrogate pair (two Chars in UTF-16). Neither half is a
        // vowel, so it must pass through untouched while surrounding ASCII vowels
        // are still converted.
        assertEquals("4🎉3", useCase("a🎉e"))
    }
}
