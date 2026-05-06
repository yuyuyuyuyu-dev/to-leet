package dev.yuyuyuyuyu.toleet.domain.useCase

class ToLeetUseCase {
    operator fun invoke(string: String): String = string.map { char ->
        when (char) {
            'A', 'a' -> '4'
            'E', 'e' -> '3'
            'I', 'i' -> '1'
            'O', 'o' -> '0'
            else -> char
        }
    }.joinToString("")
}
