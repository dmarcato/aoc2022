data class Pos(val x: Int, val y: Int)

fun main() {

    fun List<String>.atPos(pos: Pos): Int = this[pos.y][pos.x].digitToInt()

    fun visible(input: List<String>, pos: Pos, maxX: Int, maxY: Int): Boolean {
        val tree = input.atPos(pos)
        return (pos.x - 1 downTo 0).all { tree > input.atPos(Pos(it, pos.y)) }
                || (pos.x + 1 until maxX).all { tree > input.atPos(Pos(it, pos.y)) }
                || (pos.y - 1 downTo 0).all { tree > input.atPos(Pos(pos.x, it)) }
                || (pos.y + 1 until maxY).all { tree > input.atPos(Pos(pos.x, it)) }
    }

    fun Iterable<Int>.countWhileMatchOrEnd(condition: (Int) -> Boolean): Int {
        var count = 0
        this.asSequence().forEach { i ->
            count++
            if (!condition(i)) {
                return count
            }
        }
        return count
    }

    fun scenicScore(input: List<String>, pos: Pos, maxX: Int, maxY: Int): Int {
        val tree = input.atPos(pos)
        val left = (pos.x - 1 downTo 0).countWhileMatchOrEnd { tree > input.atPos(Pos(it, pos.y)) }
        val right = (pos.x + 1 until maxX).countWhileMatchOrEnd { tree > input.atPos(Pos(it, pos.y)) }
        val top = (pos.y - 1 downTo 0).countWhileMatchOrEnd { tree > input.atPos(Pos(pos.x, it)) }
        val bottom = (pos.y + 1 until maxY).countWhileMatchOrEnd { tree > input.atPos(Pos(pos.x, it)) }
        return left * right * top * bottom
    }

    fun part1(input: List<String>): Int {
        val maxX = input.first().length
        val maxY = input.size
        val extras = (1 until maxY - 1).sumOf { y ->
            (1 until maxX - 1).count { x -> visible(input, Pos(x, y), maxX, maxY) }
        }
        return (maxX * 2 + maxY * 2 - 4) + extras
    }

    fun part2(input: List<String>): Int {
        val maxX = input.first().length
        val maxY = input.size
        val max = (1 until maxY - 1).maxOf { y ->
            (1 until maxX - 1).maxOf { x -> scenicScore(input, Pos(x, y), maxX, maxY) }
        }
        return max
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21) { "part1 check failed" }
    check(part2(testInput) == 8) { "part2 check failed" }

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
