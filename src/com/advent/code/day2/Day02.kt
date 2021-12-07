package com.advent.code.day2

import readInput

fun main() {
    data class Position(val horizontal: Int = 0, val depth: Int = 0, val aim: Int = 0)

    fun part1(input: List<String>): Int {
        return input.map { it.split(" ") }
            .map { Pair(it[0], it[1].toInt()) }
            .fold(Position()) { acc, (command, amount) ->
                when (command) {
                    "forward" -> Position(acc.horizontal + amount, acc.depth)
                    "down" -> Position(acc.horizontal, acc.depth + amount)
                    "up" -> Position(acc.horizontal, acc.depth - amount)
                    else -> throw Exception()
                }
            }.let { it.horizontal * it.depth }
    }

    fun part2(input: List<String>): Int {
        return input.map { it.split(" ") }
            .map { Pair(it[0], it[1].toInt()) }
            .fold(Position()) { acc, (command, amount) ->
                when (command) {
                    "forward" -> Position(
                        acc.horizontal + amount,
                        acc.depth + acc.aim * amount,
                        acc.aim
                    )
                    "down" -> Position(acc.horizontal, acc.depth, acc.aim + amount)
                    "up" -> Position(acc.horizontal, acc.depth, acc.aim - amount)
                    else -> throw Exception()
                }
            }.let { it.horizontal * it.depth }
    }

    val testInput = readInput("Day02_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
