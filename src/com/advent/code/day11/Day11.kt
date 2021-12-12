package com.advent.code.day11

import readInput
import java.util.*


fun main() {

    val testInput = readInput( "Day11_test")
    val input = readInput( "Day11")

    check(part1(testInput) == 1656)
    println(part1(input))

    check(part2(testInput) == 195)
    println(part2(input))
}

fun List<String>.parse() = Array(size) { rowIndex ->
    IntArray(this[rowIndex].length) { columnIndex ->
        this[rowIndex][columnIndex].digitToInt()
    }
}

fun Array<IntArray>.iterate() {
    val height = size
    val width = first().size

    val flashes = LinkedList<Pair<Int, Int>>()

    indices.forEach { rowIndex ->
        first().indices.forEach { columnIndex ->
            this[rowIndex][columnIndex]++
            if (this[rowIndex][columnIndex] >= 10) {
                this[rowIndex][columnIndex] = 0
                flashes.add(Pair(rowIndex, columnIndex))
            }
        }
    }

    while (flashes.isNotEmpty()) {
        val position = flashes.removeFirst()

        val wave = listOf(
            Pair(position.first + 1, position.second),
            Pair(position.first + 1, position.second + 1),
            Pair(position.first + 1, position.second - 1),
            Pair(position.first, position.second + 1),
            Pair(position.first, position.second - 1),
            Pair(position.first - 1, position.second),
            Pair(position.first - 1, position.second + 1),
            Pair(position.first - 1, position.second - 1),
        )
            .filter { it.first in 0 until height && it.second in 0 until width }
            .filterNot { this[it.first][it.second] == 0 }

        wave.forEach { (rowIndex, columnIndex) ->
            this[rowIndex][columnIndex]++
            if (this[rowIndex][columnIndex] >= 10) {
                this[rowIndex][columnIndex] = 0
                flashes.add(Pair(rowIndex, columnIndex))
            }
        }
    }
}

fun Array<IntArray>.flashCount() = sumOf { row -> row.count { it == 0 } }

private fun part1(
    input: List<String>
) = input.parse().let { array ->
    (1..100).fold(0) { acc, _ ->
        array.iterate()
        return@fold acc + array.flashCount()
    }
}

private fun part2(
    input: List<String>
) = input.parse().let { array ->
    var step = 0
    do {
        step++
        array.iterate()
    } while (array.size * array.first().size != array.flashCount())
    return@let step
}
