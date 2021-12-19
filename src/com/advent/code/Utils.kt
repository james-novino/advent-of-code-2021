import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String): List<String> {
    val packageName = name.split("Day")[1].replace("_test", "").toInt()
    return File("src/com/advent/code/day${packageName}", "$name.txt").readLines()
}

/**
 * Convert input string into List<Int>
 */
fun parseIntCsv(input: String): List<Int> {
    return input.split(",").map { it.toInt() }
}

/**
 * Calculates the [triangular number](https://en.wikipedia.org/wiki/Triangular_number) of the given number.
 */
fun Long.triangular(): Long = ((this * (this + 1)) / 2)



/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

/**
 * Helper method for converting a string to binary for Day16
 */
fun String.toBinary(): String {
    return this.asIterable().joinToString("") { char ->
        when (char) {
            '0' -> "0000"
            '1' -> "0001"
            '2' -> "0010"
            '3' -> "0011"
            '4' -> "0100"
            '5' -> "0101"
            '6' -> "0110"
            '7' -> "0111"
            '8' -> "1000"
            '9' -> "1001"
            'A' -> "1010"
            'B' -> "1011"
            'C' -> "1100"
            'D' -> "1101"
            'E' -> "1110"
            'F' -> "1111"
            else -> error("not a valid hex digit")
        }
    }
}

// https://stackoverflow.com/a/67595807/1773713
fun <S, T> List<S>.cartesianProduct(other: List<T>) = this.flatMap { thisIt ->
    other.map { otherIt ->
        thisIt to otherIt
    }
}