fun main() {
    fun String.toRange(): IntRange {
        val (start, end) = split("-").map { it.toInt() }
        return IntRange(start, end)
    }

    fun IntRange.contains(other: IntRange): Boolean {
        return contains(other.first) && contains(other.last)
    }

    fun IntRange.overlap(other: IntRange): Boolean {
        return contains(other.first) || contains(other.last)
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { row ->
            val (first, second) = row.split(",").map { it.toRange() }
            @Suppress("USELESS_CAST")
            if (first.contains(second) || second.contains(first)) 1 else 0 as Int
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { row ->
            val (first, second) = row.split(",").map { it.toRange() }
            @Suppress("USELESS_CAST")
            if (first.overlap(second) || second.overlap(first)) 1 else 0 as Int
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2) { "part1 check failed" }
    check(part2(testInput) == 4) { "part2 check failed" }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
