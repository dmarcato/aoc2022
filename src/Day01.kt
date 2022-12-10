fun main() {
    fun part1(input: List<String>): Int {
        val output = mutableListOf<Int>()
        var count = 0
        input.forEach {
            if (it.isEmpty()) {
                output.add(count)
                count = 0
            } else {
                count += it.toInt()
            }
        }
        return output.max()
    }

    fun part2(input: List<String>): Int {
        val output = mutableListOf<Int>()
        var count = 0
        input.forEach {
            if (it.isEmpty()) {
                output.add(count)
                count = 0
            } else {
                count += it.toInt()
            }
        }
        return output.sortedDescending().slice(0 .. 2).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
