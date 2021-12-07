package com.advent.code.day6

import parseIntCsv
import readInput
import java.math.BigInteger
import kotlin.math.absoluteValue


fun main() {
    val testInput = readInput("Day06_test").flatMap { parseIntCsv(it) }
    val input = readInput("Day06").flatMap { parseIntCsv(it) }

    check(part1(testInput) == 5934)
    println(part1(input))

    check(part2(testInput) == BigInteger.valueOf(26984457539L))
    println(part2(input))
}

private fun part1(input: List<Int>): Int {
    val result = (0..79).fold(input) { fish, _ ->
        val next = fish.flatMap {
            if (it == 0) {
                listOf(6, 8)
            } else
                listOf(it - 1)
        }
        next
    }.count()

    return result
}

private fun part2(input: List<Int>): BigInteger {
    val fishMap = input.groupBy { it }.mapValues { it.value.count().toBigInteger() }

    val result = (0..255).fold(fishMap) { fish, _ ->
        val newMap: Map<Int, BigInteger> = fish.mapKeys {
            if(it.key == 0) 6 else it.key - 1
        }
        val sixSum= fish.getOrDefault(0, BigInteger.valueOf(0)) + fish.getOrDefault(7, BigInteger.valueOf(0))
        val newMapWithCorrectSixes = newMap + Pair(6, sixSum)
        newMapWithCorrectSixes + Pair(8, fish.getOrDefault(0, BigInteger.valueOf(0)))

    }
        .map { it.value }
        .reduce{a,b -> a+b}

    return result
}

