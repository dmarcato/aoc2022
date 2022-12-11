import java.util.*

data class Monkey(
    val items: Queue<Long>,
    val operation: (Long, Long) -> Long,
    val operationValue: Long?,
    val testValue: Long,
    val ifTrueDestination: Int,
    val ifFalseDestination: Int,
    var itemsInspected: Long = 0L
)

fun makeMonkeys(input: List<String>): Pair<Int, Monkey> {
    val index = "\\s(\\d+):".toRegex().find(input[0])!!.groupValues[1].toInt()
    val items = input[1].split(": ")[1].split(", ").map { it.toLong() }
    val (_, op, valueString) = "old\\s([+*])\\s(\\d+|old)".toRegex().find(input[2])!!.groupValues
    val opValue = when (valueString) {
        "old" -> null
        else -> valueString.toLong()
    }
    val operation: (Long, Long) -> Long = when (op) {
        "+" -> Long::plus
        "*" -> Long::times
        else -> throw RuntimeException()
    }
    val testValue = input[3].split(" ").last().toLong()
    val ifTrue = input[4].split(" ").last().toInt()
    val ifFalse = input[5].split(" ").last().toInt()
    val queue = LinkedList<Long>()
    queue.addAll(items)
    return index to Monkey(queue, operation, opValue, testValue, ifTrue, ifFalse)
}

fun monkeyBusiness(monkeys: Map<Int, Monkey>, worryLevel: Long?, rounds: Int): Long {
    val monkeyOrder = monkeys.keys.sorted()
    val commonDivisor = monkeys.values.fold(1L) { acc, monkey -> acc * monkey.testValue }
    (0 until rounds).forEach { _ ->
        monkeyOrder.forEach { num ->
            val monkey = monkeys[num]!!
            monkey.items.forEach { item ->
                var newItem = monkey.operation(item, monkey.operationValue ?: item)
                newItem = worryLevel?.let {
                    newItem / it
                } ?: (newItem % commonDivisor)
                val destMonkey = if (newItem.mod(monkey.testValue) == 0L) {
                    monkey.ifTrueDestination
                } else {
                    monkey.ifFalseDestination
                }
                monkeys[destMonkey]!!.items.offer(newItem)
                monkey.itemsInspected++
            }
            monkey.items.clear()
        }
    }
    val (m1, m2) = monkeys.values.sortedByDescending { it.itemsInspected }.take(2)
    return m1.itemsInspected * m2.itemsInspected
}

fun main() {

    fun part1(input: List<String>): Long {
        val monkeys = input.windowed(6, 7, true) {
            makeMonkeys(it)
        }.toMap()
        return monkeyBusiness(monkeys, 3L, 20)
    }

    fun part2(input: List<String>): Long {
        val monkeys = input.windowed(6, 7, true) {
            makeMonkeys(it)
        }.toMap()
        return monkeyBusiness(monkeys, null, 10000)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605L) { "part1 check failed" }
    check(part2(testInput) == 2713310158L) { "part2 check failed" }

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
