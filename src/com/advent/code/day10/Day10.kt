package com.advent.code.day10

import readInput
import java.util.ArrayDeque


private val openingChars = listOf<Char>('(', '[', '{', '<')
private val closingChars = listOf<Char>(')', ']', '}', '>')

fun main() {

    val testInput = readInput( "Day10_test")
    val input = readInput( "Day10")

    check(part1(testInput) == 26397)
    println(part1(input))

    check(part2(testInput) == 288957L)
    println(part2(input))
}

private fun part1(input: List<String>) : Int {
    val charScores = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
    return input.fold(0) { totalScore, line ->
        val chars = line.toCharArray()
        val stack = ArrayDeque<Char>()
        var offendingChar: Char? = null
        chars.forEach { char ->
            if (openingChars.contains(char)) {
                stack.push(char)
            } else if (closingChars.contains(char)) {
                if (stack.isEmpty()) {
                    // Invalid if the stack is empty
                    offendingChar = char
                    return@forEach
                } else {
                    val openingChar = stack.pop()
                    val openingCharIndex = openingChars.indexOf(openingChar)
                    val closingCharIndex = closingChars.indexOf(char)
                    if (openingCharIndex != closingCharIndex) {
                        // Invalid if the opening and closing characters don't match
                        offendingChar = char
                        return@forEach
                    }
                }
            }
        }

        if (offendingChar != null) {
            totalScore + charScores[offendingChar]!!
        } else totalScore
    }
}

private fun part2(input: List<String>): Long {
    val openingChars = listOf<Char>('(', '[', '{', '<')
    val closingChars = listOf<Char>(')', ']', '}', '>')
    val charScores = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)
    var stack = ArrayDeque<Char>()
    val totalScores = mutableListOf<Long>()

    for (line in input) {
        var lineValid = true
        val lineChars = line.toCharArray()
        for (char in lineChars) {
            if (openingChars.contains(char)) {
                // Opening character
                stack.push(char)
            } else if (closingChars.contains(char)) {
                // Closing character
                if (stack.isEmpty()) {
                    // Invalid if the stack is empty
                    lineValid = false
                    break
                } else {
                    val openingChar = stack.pop()
                    val openingCharIndex = openingChars.indexOf(openingChar)
                    val closingCharIndex = closingChars.indexOf(char)
                    if (openingCharIndex != closingCharIndex) {
                        // Invalid if the opening and closing characters don't match
                        lineValid = false
                        break
                    }
                }
            }
        }

        if (lineValid) {
            var lineScore = 0L
            for (item in stack) {
                val openingCharIndex = openingChars.indexOf(item)
                val closingChar = closingChars[openingCharIndex]

                lineScore *= 5
                lineScore += charScores[closingChar]!!
            }
            totalScores.add(lineScore)
        }

        stack.clear()
    }

    totalScores.sort()

    return totalScores[totalScores.size / 2]
}
