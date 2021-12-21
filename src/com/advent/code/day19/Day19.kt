package com.advent.code.day19

import cartesianProduct
import readInput
import toBinary
import readInput
import kotlin.math.abs
import kotlin.math.ceil

fun main() {
    val testInput = readInput("Day19_test")
    check(part1(testInput) == 79)
    check(part2(testInput) == 3621)

    val input = readInput("Day19")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    return input
        .parseInput()
        .mergeScanners()
        .first[0].size
}

private fun part2(input: List<String>): Int {
    return input
        .parseInput()
        .mergeScanners()
        .maxDistance()
}

private data class Point(val x: Int, val y: Int, val z: Int)

private fun List<String>.parseInput(): List<MutableSet<Point>> {
    return joinToString("\n")
        .split("\n\n")
        .map { it.split("\n") }
        .map {
            it.subList(1, it.size)
                .map { row ->
                    val coordinates = row.split(",")
                    Point(coordinates[0].toInt(), coordinates[1].toInt(), coordinates[2].toInt())
                }.toMutableSet()
        }.toList()
}

private fun List<MutableSet<Point>>.mergeScanners(): Pair<List<MutableSet<Point>>, MutableMap<Int, Point>> {
    val translations = mutableMapOf(0 to Point(0, 0, 0))
    val mergedScanners = mutableSetOf<Int>()

    fun merge() {
        forEachIndexed { index, scanner ->
            if (index != 0 && !mergedScanners.contains(index)) {
                getRotationsList().forEach { rotation ->
                    val scannerRotated = scanner.map(rotation)
                    val firstScanner = this.first()

                    firstScanner.forEach { scannerPoint ->
                        scannerRotated.forEach { scannerRotatedPoint ->
                            val scannerTranslated =
                                scannerRotated.map { it.translatedPoint(scannerPoint, scannerRotatedPoint) }
                            if (scannerTranslated.intersect(firstScanner).size >= 12) {
                                translations[index] = scannerPoint - scannerRotatedPoint
                                mergedScanners.add(index)
                                firstScanner.addAll(scannerTranslated)
                                return
                            }
                        }
                    }

                }
            }
        }
    }
    while (mergedScanners.size < this.size - 1) {
        merge()
    }

    return Pair(this, translations)
}

private fun getRotationsList() = listOf(
    { (x, y, z): Point -> Point(x, y, z) },
    { (x, y, z): Point -> Point(x, -y, -z) },
    { (x, y, z): Point -> Point(-x, y, -z) },
    { (x, y, z): Point -> Point(-x, -y, z) },
    { (x, y, z): Point -> Point(x, z, -y) },
    { (x, y, z): Point -> Point(x, -z, y) },
    { (x, y, z): Point -> Point(-x, z, y) },
    { (x, y, z): Point -> Point(-x, -z, -y) },
    { (x, y, z): Point -> Point(y, x, -z) },
    { (x, y, z): Point -> Point(y, -x, z) },
    { (x, y, z): Point -> Point(-y, x, z) },
    { (x, y, z): Point -> Point(-y, -x, -z) },
    { (x, y, z): Point -> Point(y, z, x) },
    { (x, y, z): Point -> Point(y, -z, -x) },
    { (x, y, z): Point -> Point(-y, z, -x) },
    { (x, y, z): Point -> Point(-y, -z, x) },
    { (x, y, z): Point -> Point(z, y, -x) },
    { (x, y, z): Point -> Point(z, -y, x) },
    { (x, y, z): Point -> Point(-z, y, x) },
    { (x, y, z): Point -> Point(-z, -y, -x) },
    { (x, y, z): Point -> Point(z, x, y) },
    { (x, y, z): Point -> Point(z, -x, -y) },
    { (x, y, z): Point -> Point(-z, x, -y) },
    { (x, y, z): Point -> Point(-z, -x, y) },
)

private fun Point.distance(point: Point) = abs(x - point.x) + abs(y - point.y) + abs(z - point.z)

private fun Point.translatedPoint(scannerPoint: Point, scannerRotatedPoint: Point) = Point(
    x + (scannerPoint.x - scannerRotatedPoint.x),
    y + (scannerPoint.y - scannerRotatedPoint.y),
    z + (scannerPoint.z - scannerRotatedPoint.z),
)

private operator fun Point.minus(point: Point) = Point(x - point.x, y - point.y, z - point.z)

private fun Pair<List<MutableSet<Point>>, MutableMap<Int, Point>>.maxDistance(): Int {
    val (scanners, translations) = this
    var maxDistance = Int.MIN_VALUE
    scanners.indices.forEach { i ->
        scanners.indices.forEach { j ->
            maxDistance = maxOf(translations[i]!!.distance(translations[j]!!), maxDistance)
        }
    }
    return maxDistance
}