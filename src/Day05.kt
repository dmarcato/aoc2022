import java.util.Stack

fun main() {

    fun stacks(input: List<String>): Map<Int, Stack<Char>> {
        val stacks = mutableMapOf<Int, Stack<Char>>()
        val stacksArea = input.takeWhile { it.isNotEmpty() }
        stacksArea.reversed().drop(1).forEach { row ->
            row.windowed(3, 4) { it[1] }.forEachIndexed { index, c ->
                if (c.isLetter()) stacks.getOrPut(index) { Stack() }.push(c)
            }
        }
        return stacks
    }

    fun execute(stacks: Map<Int, Stack<Char>>, input: List<String>): String {
        val commands = input.dropWhile { it.isNotEmpty() }.drop(1)
        commands.forEach { cmd ->
            val (quantity, from, to) = cmd.split(" ").mapNotNull { it.toIntOrNull() }
            (0 until quantity).forEach { _ ->
                stacks[to - 1]!!.push(stacks[from - 1]!!.pop())
            }
        }
        return stacks.values.map { it.pop() }.joinToString(separator = "")
    }

    fun execute9001(stacks: Map<Int, Stack<Char>>, input: List<String>): String {
        val commands = input.dropWhile { it.isNotEmpty() }.drop(1)
        commands.forEach { cmd ->
            val (quantity, from, to) = cmd.split(" ").mapNotNull { it.toIntOrNull() }
            (0 until quantity).map { stacks[from - 1]!!.pop() }.reversed().forEach {
                stacks[to - 1]!!.push(it)
            }
        }
        return stacks.values.map { it.pop() }.joinToString(separator = "")
    }

    fun part1(input: List<String>): String {
        val stacks = stacks(input)
        return execute(stacks, input)
    }

    fun part2(input: List<String>): String {
        val stacks = stacks(input)
        return execute9001(stacks, input)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ") { "part1 check failed" }
    check(part2(testInput) == "MCD") { "part2 check failed" }

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
