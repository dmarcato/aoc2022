import java.util.*
import kotlin.time.ExperimentalTime

data class StepPos(val x: Int, val y: Int)

fun StepPos.linearPos(input: List<String>): Int = y * input.first().length + x

data class StepAndCost(val step: StepPos, val cost: Long) : Comparable<StepAndCost> {
    override fun compareTo(other: StepAndCost): Int {
        return cost.compareTo(other.cost)
    }
}

fun StepPos.createStepPosFor(x: Int, y:  Int, input: List<String>, visited: Array<Boolean>): StepPos? {
    val rowSize = input[0].length
    val realX = x.coerceIn(0, rowSize - 1)
    val realY = y.coerceIn(0, input.size - 1)
    val thisValue = input.valueOf(this.x, this.y)
    val value = input.valueOf(realX, realY)
    val linearPos = realY * input.first().length + realX
    return if ((this.x != realX || this.y != realY) && !visited[linearPos] && value.code <= thisValue.code + 1) {
        StepPos(realX, realY)
    } else {
        null
    }
}

fun StepPos.availableDirections(input: List<String>, visited: Array<Boolean>, currentCost: Long): List<StepAndCost> {
    return buildList {
        createStepPosFor(x - 1, y, input, visited)?.let {
            add(StepAndCost(it, currentCost + 1))
        }
        createStepPosFor(x, y - 1, input, visited)?.let {
            add(StepAndCost(it, currentCost + 1))
        }
        createStepPosFor(x + 1, y, input, visited)?.let {
            add(StepAndCost(it, currentCost + 1))
        }
        createStepPosFor(x, y + 1, input, visited)?.let {
            add(StepAndCost(it, currentCost + 1))
        }
    }
}

fun findStarts(input: List<String>, chars: List<Char>): List<StepPos> {
    return input.flatMapIndexed { y: Int, row: String ->
        row.withIndex().filter { it.value in chars }.map { StepPos(it.index, y) }
    }
}

fun List<String>.valueOf(x: Int, y: Int, mapStartEnd: Boolean = true): Char {
    val value = this[y][x]
    return if (mapStartEnd) {
        when (value) {
            'S' -> 'a'
            'E' -> 'z'
            else -> value
        }
    } else {
        value
    }
}

class OrderedLinkedList<T : Comparable<T>> : LinkedList<T>() {
    override fun add(element: T): Boolean {
        val itr = listIterator()
        while (true) {
            if (!itr.hasNext()) {
                itr.add(element)
                return true
            }
            if (itr.next() > element) {
                itr.previous()
                itr.add(element)
                return true
            }
        }
    }

    override fun addAll(elements: Collection<T>): Boolean {
        elements.forEach { add(it) }
        return true
    }
}

fun stepsToEnd(input: List<String>, pos: StepPos): Long {
    val linearSize = input.size * input.first().length
    val paths = OrderedLinkedList<StepAndCost>()
    val visited = Array(linearSize) { false }
    val shortest = mutableMapOf<StepPos, StepAndCost>()
    paths.add(StepAndCost(pos, 0))
    shortest[pos] = StepAndCost(pos, 0)
    while (true) {
        val current = paths.poll() ?: return Long.MAX_VALUE
        val value = input.valueOf(current.step.x, current.step.y, false)
        if (value == 'E') {
            return current.cost
        } else {
            current.step.availableDirections(input, visited, current.cost).forEach {
                if (it.cost < (shortest[it.step]?.cost ?: Long.MAX_VALUE)) {
                    shortest[it.step] = it
                    paths.add(it)
                }
            }
        }
        visited[current.step.linearPos(input)] = true
    }
}

@OptIn(ExperimentalTime::class)
fun main() {

    fun part1(input: List<String>): Long {
        return findStarts(input, listOf('S')).minOf { stepsToEnd(input, it) }
    }

    fun part2(input: List<String>): Long {
        return findStarts(input, listOf('S', 'a')).minOf { stepsToEnd(input, it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31L) { "part1 check failed" }
    check(part2(testInput) == 29L) { "part2 check failed" }

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
