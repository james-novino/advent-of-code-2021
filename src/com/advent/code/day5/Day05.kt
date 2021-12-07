package com.advent.code.day5

import readInput
import kotlin.math.absoluteValue


data class Point2d(val x: Int, val y: Int) {

    infix fun sharesAxisWith(that: Point2d): Boolean =
        x == that.x || y == that.y

    infix fun lineTo(that: Point2d): List<Point2d> {
        val xDelta = lineWalkStep(x, that.x)
        val yDelta = lineWalkStep(y, that.y)
        val steps = maxOf((x - that.x).absoluteValue, (y - that.y).absoluteValue)
        return (1..steps).scan(this) { last, _ -> Point2d(last.x + xDelta, last.y + yDelta) }
    }

    private fun lineWalkStep(a: Int, b: Int): Int =
        when {
            a > b -> -1
            a < b -> 1
            else -> 0
        }
}

fun main() {

    fun part1(instructions: List<Pair<Point2d, Point2d>>): Int {
        return solve(instructions) { it.first sharesAxisWith it.second }
    }

    fun part2(instructions:  List<Pair<Point2d, Point2d>>): Int {
        return solve(instructions) { true }
    }


    val testInput = readInput( "Day05_test").map { parseRow(it) }
    val input = readInput("Day05").map { parseRow(it) }

    check(part1(testInput) == 5)
    println(part1(input))

    check(part2(testInput) == 12)
    println(part2(input))
}

fun parseRow(input: String): Pair<Point2d, Point2d> =
    Pair(
        input.substringBefore(" ").split(",").map { it.toInt() }.let { Point2d(it.first(), it.last()) },
        input.substringAfterLast(" ").split(",").map { it.toInt() }.let { Point2d(it.first(), it.last()) }
    )

fun solve(instructions: List<Pair<Point2d, Point2d>>, lineFilter: (Pair<Point2d, Point2d>) -> Boolean) =
    instructions
        .filter { lineFilter(it) }
        .map { it.first lineTo it.second }
        .flatten()
        .groupingBy { it }
        .eachCount()
        .count { it.value > 1 }