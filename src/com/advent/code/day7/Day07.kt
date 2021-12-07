package com.advent.code.day7

import parseIntCsv
import readInput
import triangular
import java.math.BigInteger
import kotlin.math.abs

fun main() {

    val testInput = readInput( "Day07_test").first()
    val input = readInput( "Day07").first()

    check(part1(testInput) == 37L)
    println(part1(input))

    check(part2(testInput) == 168L)
    println(part2(input))
}

private fun part1(input: String): Long {
    val crabPositions: List<Int> = parseIntCsv(input)
    val fuelPerPosition: Map<Int, Long> = calculateFuelPosition(crabPositions) { start, end ->
        abs(start - end).toLong()
    }
    return fuelPerPosition.minOf { it.value }
}

private fun part2(input: String): Long {
    val crabPositions: List<Int> = parseIntCsv(input)
    val fuelPerPosition: Map<Int, Long> = calculateFuelPosition(crabPositions) { start, end ->
        abs(start - end).toLong().triangular()
    }
    return fuelPerPosition.minOf { it.value }
}

/**
 * Returns the total fuel consumption per position in [startingPositions] to move each entity to each of those.
 */
private fun calculateFuelPosition(
    startingPositions: List<Int>,
    fuelEquation: (Int, Int) -> Long,
): Map<Int, Long> {
    val minPosition: Int = startingPositions.minOf { it }
    val maxPosition: Int = startingPositions.maxOf { it }
    return (minPosition..maxPosition).associateWith { end ->
        startingPositions.sumOf { start ->
            fuelEquation(start, end)
        }
    }
}

