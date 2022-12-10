fun main() {
    fun findPacket(input: List<String>, numOfDistinct: Int): Int {
        return input.first().asSequence().windowed(numOfDistinct, 1, true) { chunk ->
            chunk.any { c ->
                chunk.count { it == c } > 1
            }
        }.indexOfFirst { !it } + numOfDistinct
    }

    fun part1(input: List<String>): Int {
        return findPacket(input, 4)
    }

    fun part2(input: List<String>): Int {
        return findPacket(input, 14)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 7) { "part1 check failed" }
    check(part2(testInput) == 19) { "part2 check failed" }

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
