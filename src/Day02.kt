fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            val (a, transB) = it.split(" ")
            val b = when (transB) {
                "X" -> "A"
                "Y" -> "B"
                "Z" -> "C"
                else -> throw RuntimeException()
            }
            val myScore = when (b) {
                "A" -> 1
                "B" -> 2
                "C" -> 3
                else -> 0
            }
            val victoryScore = when {
                a == b -> 3
                a == "A" && b == "C" -> 0
                a == "B" && b == "A" -> 0
                a == "C" && b == "B" -> 0
                else -> 6
            }
            myScore + victoryScore
        }
    }

    fun String.points(): Int = when (this) {
        "A" -> 1
        "B" -> 2
        "C" -> 3
        "X" -> 0
        "Y" -> 3
        "Z" -> 6
        else -> throw RuntimeException()
    }
    val moves = listOf("A", "B", "C")
    fun String.loose(): String = moves[(moves.indexOf(this) - 1).mod(3)]
    fun String.win(): String = moves[(moves.indexOf(this) + 1).mod(3)]
    fun String.op(op: String) = when (op) {
        "X" -> loose()
        "Y" -> this
        "Z" -> win()
        else -> throw RuntimeException()
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            val (a, b) = it.split(" ")
            a.op(b).points() + b.points()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15) { "part1 check failed" }
    check(part2(testInput) == 12) { "part2 check failed" }

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
