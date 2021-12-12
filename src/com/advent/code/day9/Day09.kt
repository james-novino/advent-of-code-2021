package com.advent.code.day9

import readInput

fun main() {

    val testInput = readInput( "Day09_test")
    val input = readInput( "Day09")

    check(part1(testInput) == 15)
    println(part1(input))

    check(part2(testInput) == 1134)
    println(part2(input))
}

private fun part1(input: List<String>) : Int {
    val map = SmokeFlows(input.map { line -> line.map { it.digitToInt() } })
    return map.indices.asSequence()
        .filter { p -> map.neighbours(of = p).all { map[it] > map[p] } }
        .sumOf { map[it] + 1 }
}

private fun part2(input: List<String>) : Int {
    val map = SmokeFlows(input.map { line -> line.map { it.digitToInt() } })
    return map.indices.asSequence()
        .map { p -> map.search(from = p) { from, to -> map[from] < map[to] && map[to] != 9 } }
        .distinct().map { it.size }
        .sortedDescending().take(3)
        .fold(1, Int::times)
}

private data class Location(val x: Int, val y: Int)

private data class SmokeFlows<V>(val heights: List<List<V>>) {

    val indices = heights.flatMapIndexed { y, row -> row.indices.map { Location(it, y) } }

    operator fun get(p: Location): V = with(p) { heights[y][x] }

    fun neighbours(of: Location) = with(of) {
        sequenceOf(
            Location(x + 1, y), Location(x - 1, y),
            Location(x, y + 1), Location(x, y - 1)
        ).filter { it.isValid() }
    }

    enum class SearchType { DFS, BFS }

    fun search(
        from: Location,
        type: SearchType = SearchType.DFS,
        action: (Location) -> Unit = {},
        edge: (Location, Location) -> Boolean = { _, _ -> true }
    ): Set<Location> {
        val visited = mutableSetOf<Location>()
        val queue = ArrayDeque<Location>()
        tailrec fun search(curr: Location) {
            visited += curr.also(action)
            neighbours(curr).filter { edge(curr, it) && it !in visited }.forEach { queue += it }
            when (type) {
                SearchType.DFS -> search(queue.removeLastOrNull() ?: return)
                SearchType.BFS -> search(queue.removeFirstOrNull() ?: return)
            }
        }
        return visited.also { search(from) }
    }

    private fun Location.isValid() = y in heights.indices && x in heights[y].indices
}