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
