package com.advent.code.day18

import cartesianProduct
import readInput
import toBinary
import readInput
import kotlin.math.ceil

fun main() {
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 4140)
    check(part2(testInput) == 3993)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    return input
        .map { it.parser() }
        .reduce { acc, number -> acc + number }
        .magnitude()
}

fun part2(input: List<String>): Int {
    val numbers = input.map { it.parser() }
    var maxMagnitude = Int.MIN_VALUE
    for (firstNumber in numbers) {
        for (secondNumber in numbers) {
            if (firstNumber != secondNumber) {
                maxMagnitude = maxOf(maxMagnitude, (firstNumber + secondNumber).magnitude())
            }
        }
    }
    return maxMagnitude
}

private sealed class Number
private class PairNumber(var left: Number, var right: Number) : Number() {
    override fun toString() = "[$left,$right]"
}
private class RegularNumber(var value: Int) : Number() {
    override fun toString() = value.toString()
}

private fun String.parser(): Number {
    var index = 0

    fun parse(): Number = when (val c = this[index]) {
        '[' -> {
            index++
            val left = parse()
            require(this[index++] == ',')
            val right = parse()
            require(this[index++] == ']')
            PairNumber(left, right)
        }
        else -> {
            require(c.isDigit())
            index++
            RegularNumber(c.digitToInt())
        }
    }

    return parse()
}

private operator fun Number.plus(other: Number): Number = PairNumber(this, other).reduce()

private fun Number.reduce(): Number {
    var result = copy()
    while (true) {
        val nested = result.findNested(4)
        if (nested != null) {
            nested.addAfterExploding()
            continue
        }

        val tenOrGreater = result.findTenOrGreater()
        if (tenOrGreater != null) {
            val (pairNumber, split) = tenOrGreater
            val newPair = PairNumber(
                RegularNumber(split.value / 2),
                RegularNumber(ceil(split.value / 2f).toInt())
            )
            when {
                pairNumber == null -> result = newPair
                pairNumber.left == split -> pairNumber.left = newPair
                pairNumber.right == split -> pairNumber.right = newPair
            }
            continue
        }

        return result
    }
}

private fun Number.copy(): Number = when (this) {
    is PairNumber -> PairNumber(left.copy(), right.copy())
    is RegularNumber -> RegularNumber(value)
}

private fun PairNumber.destruct() = Pair((left as RegularNumber).value, (right as RegularNumber).value)

private fun Number.nextRegularNumber(searchLeft: Boolean = false): RegularNumber = when (this) {
    is RegularNumber -> this
    is PairNumber -> if (searchLeft) left.nextRegularNumber(true) else right.nextRegularNumber()
}

private fun Number.findNested(i: Int): List<PairNumber>? = when (this) {
    is RegularNumber -> null
    is PairNumber -> {
        if (i == 0) listOf(this)
        else (left.findNested(i - 1) ?: right.findNested(i - 1))?.let {
            listOf(this) + it
        }
    }
}

private fun List<PairNumber>.addAfterExploding() {
    val exploding = last()
    val (left, right) = exploding.destruct()

    when {
        this[3].right == exploding -> this[3].left.nextRegularNumber().value += left
        this[2].right == this[3] -> this[2].left.nextRegularNumber().value += left
        this[1].right == this[2] -> this[1].left.nextRegularNumber().value += left
        this[0].right == this[1] -> this[0].left.nextRegularNumber().value += left
    }

    when {
        this[3].left == exploding -> this[3].right.nextRegularNumber(true).value += right
        this[2].left == this[3] -> this[2].right.nextRegularNumber(true).value += right
        this[1].left == this[2] -> this[1].right.nextRegularNumber(true).value += right
        this[0].left == this[1] -> this[0].right.nextRegularNumber(true).value += right
    }

    if (this[3].left == exploding) {
        this[3].left = RegularNumber(0)
    } else {
        this[3].right = RegularNumber(0)
    }
}

private fun Number.findTenOrGreater(pairNumberContainer: PairNumber? = null): Pair<PairNumber?, RegularNumber>? =
    when (this) {
        is PairNumber -> left.findTenOrGreater(this) ?: right.findTenOrGreater(this)
        is RegularNumber -> if (value >= 10) pairNumberContainer to this else null
    }

private fun Number.magnitude(): Int = when (this) {
    is PairNumber -> 3 * left.magnitude() + 2 * right.magnitude()
    is RegularNumber -> value
}