import kotlin.math.absoluteValue
import kotlin.math.sign

data class Coords(val x: Int, val y: Int)

data class Snake(
    var head: Coords = Coords(0, 0),
    val knotsCount: Int = 1
) {

    private val knots = mutableListOf<Coords>()
    private val tailVisits = mutableSetOf<Coords>()

    init {
        repeat(knotsCount) { knots.add(head.copy()) }
        tailVisits.add(knots.last())
    }

    fun move(direction: String, amount: Int) {
        (0 until amount).forEach { _ ->
            head = when (direction) {
                "U" -> head.copy(y = head.y + 1)
                "D" -> head.copy(y = head.y - 1)
                "L" -> head.copy(x = head.x - 1)
                "R" -> head.copy(x = head.x + 1)
                else -> throw RuntimeException()
            }
            moveKnots()
        }
    }

    private fun moveKnots() {
        (0 until knotsCount).forEach { i ->
            val knot = knots[i]
            val previous = if (i > 0) knots[i - 1] else head
            val diffX = previous.x - knot.x
            val diffY = previous.y - knot.y
            if (diffX.absoluteValue > 1) {
                knots[i] = knot.copy(x = knot.x + diffX.sign, y = knot.y + diffY.sign)
            } else if (diffY.absoluteValue > 1) {
                knots[i] = knot.copy(x = knot.x + diffX.sign, y = knot.y + diffY.sign)
            }
        }
        tailVisits.add(knots.last())
    }

    fun tailVisits(): Int = tailVisits.size

}

fun main() {

    fun part1(input: List<String>): Int {
        val snake = Snake()
        input.forEach { cmd ->
            val (direction, amountString) = cmd.split(" ")
            val amount = amountString.toInt()
            snake.move(direction, amount)
        }
        return snake.tailVisits()
    }

    fun part2(input: List<String>): Int {
        val snake = Snake(knotsCount = 9)
        input.forEach { cmd ->
            val (direction, amountString) = cmd.split(" ")
            val amount = amountString.toInt()
            snake.move(direction, amount)
        }
        return snake.tailVisits()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13) { "part1 check failed" }
    check(part2(testInput) == 1) { "part2 check failed" }

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
