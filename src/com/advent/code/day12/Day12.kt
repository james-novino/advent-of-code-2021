package com.advent.code.day12

import readInput

fun main() {

    val testInput = readInput( "Day12_test")
    val input = readInput( "Day12")

    check(part1(testInput) == 226L)
    println(part1(input))

    check(part2(testInput) == 3509L)
    println(part2(input))
}

private fun List<String>.parseInput() : List<Pair<String, String>> {
    return this.map { l ->
        val (a, b) = l.split("-")
        Pair(a, b)
    }
}

fun part1(input: List<String>): Long {
    val cave = CaveSystem(input.parseInput())
    return cave.countPaths(mutableListOf("start"), false, false)
}

fun part2(input: List<String>): Long {
    val cave = CaveSystem(input.parseInput())
    return cave.countPaths(mutableListOf("start"), true, false)
}

private data class CaveSystem(val edges: List<Pair<String, String>>) {
    val links: Map<String, List<String>> = run {
        val h = mutableMapOf<String, MutableList<String>>()
        edges.forEach { (a, b) ->
            h.putIfAbsent(a, mutableListOf())
            h.putIfAbsent(b, mutableListOf())
            h[a]!!.add(b)
            h[b]!!.add(a)
        }
        h
    }

    fun isLower(s: String): Boolean {
        return s.lowercase() == s
    }

    fun isStartEnd(s: String): Boolean {
        return s == "start" || s == "end"
    }

    fun countPaths(sofar: MutableList<String>, twiceOk: Boolean, twiceSeen: Boolean): Long {
        val cur = sofar.last()
        if (cur == "end") {
            return 1
        }

        var found = 0L
        for (adj in links[cur]!!) {
            var twSeen = twiceSeen
            if (isLower(adj) && sofar.contains(adj)) {
                if (isStartEnd(adj) || !twiceOk || twiceSeen)
                    continue
                twSeen = true
            }

            sofar.add(adj)
            found += countPaths(sofar, twiceOk, twSeen)
            sofar.removeLast()
        }
        return found
    }
}