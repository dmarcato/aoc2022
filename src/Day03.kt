fun main() {
    fun Char.priority(): Int = when {
        isUpperCase() -> code - 'A'.code + 27
        else -> code - 'a'.code + 1
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { row ->
            val (a, b) = row.chunked(row.length / 2)
            a.asSequence().first { b.contains(it) }.priority()
        }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).sumOf { rows ->
            val (a, b, c) = rows
            a.asSequence().first { b.contains(it) && c.contains(it) }.priority()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157) { "part1 check failed" }
    check(part2(testInput) == 70) { "part2 check failed" }

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
