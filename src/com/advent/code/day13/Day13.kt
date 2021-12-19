package com.advent.code.day13

import readInput

fun main() {
    val testInput = readInput( "Day13_test")
    val input = readInput( "Day13")

    check(testInput.part1() == 17)
    println(input.part1())
    println(input.part2())
}


private fun List<String>.part1() = (dots() foldedOn folds().first()).size
private fun List<String>.part2(): String {
    val result = folds().fold(dots()) { dots, fold -> dots foldedOn fold }
    val xRange = result.minOf { it.first }..result.maxOf { it.first }
    val yRange = result.minOf { it.second }..result.maxOf { it.second }
    return yRange.joinToString("\n") { y ->
        xRange.joinToString("") { x ->
            if ((x to y) in result) "##" else "  "
        }
    }
}

private fun List<String>.dots(): List<Pair<Int, Int>> = takeWhile { it.isNotBlank() }
    .map { it.split(",") }
    .map { (a, b) -> a.toInt() to b.toInt() }

private fun List<String>.folds(): List<Pair<String, Int>> = takeLastWhile { it.isNotBlank() }
    .map { it.substringAfter("along ").split("=") }
    .map { (a, b) -> a to b.toInt() }

private fun List<Pair<Int, Int>>.foldX(p: Int) = map { (x, y) -> if (x < p) x to y else 2 * p - x to y }.distinct()
private fun List<Pair<Int, Int>>.foldY(p: Int) = map { (x, y) -> if (y < p) x to y else x to 2 * p - y }.distinct()

private infix fun List<Pair<Int, Int>>.foldedOn(fold: Pair<String, Int>): List<Pair<Int, Int>> {
    val (foldDir, foldPos) = fold
    return when (foldDir) {
        "x" -> foldX(foldPos)
        else -> foldY(foldPos)
    }
}

