package com.ar.of_pro.util

class Sanitizer {

    fun validateOnlyLetters(input: String): Boolean {
        val pattern = Regex("^[A-Za-z]+\$")
        return input.isNotEmpty() && pattern.matches(input)
    }

    fun validateLettersNumericAndSpaces(input: String): Boolean {
        val pattern = Regex("^[A-Za-z0-9\\s]+\$")
        return input.isNotEmpty() && pattern.matches(input)
    }

    fun validateLettersAndSpaces(input: String): Boolean {
        val pattern = Regex("^[A-Za-z\\s]+\$")
        return input.isNotEmpty() && pattern.matches(input)
    }

    fun validateMail(input: String): Boolean {
        val emailPattern = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return input.isNotEmpty() && emailPattern.matches(input)
    }

    fun validateNumeric(input: String): Boolean {
        val numberPattern = Regex("^[0-9]+$")
        return input.isNotEmpty() && numberPattern.matches(input)
    }
}