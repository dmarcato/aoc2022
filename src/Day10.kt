class CPU {
    private val cyclesMap = mutableMapOf<IntRange, Int>()
    private var x = 1
    private var cycle = 1

    private val toCompute = listOf(20, 60, 100, 140, 180, 220)

    fun add(v: Int) {
        val startCycle = cycle
        cycle += 2
        cyclesMap[startCycle until cycle] = x
        x += v
    }

    fun noop() {
        val startCycle = cycle
        cycle += 1
        cyclesMap[startCycle until cycle] = x
    }

    fun signalStrength(): Int {
        return cyclesMap.keys.sortedBy { it.first }.sumOf { cycles ->
            toCompute.firstOrNull { cycles.contains(it) }?.let { cycle ->
                cycle * cyclesMap[cycles]!!
            } ?: 0
        }
    }

    fun draw(): String = buildString {
        (1 until 241).forEach { cycle ->
            val x = cyclesMap[cyclesMap.keys.firstOrNull { it.contains(cycle) }] ?: -1
            val toDraw = if (IntRange(x - 1, x + 1).contains((cycle - 1).mod(40))) {
                "#"
            } else {
                "."
            }
            append(toDraw)
            if (cycle.mod(40) == 0) {
                append("\n")
            }
        }
    }

}

fun main() {

    fun part1(input: List<String>): Int {
        val cpu = CPU()
        input.forEach { row ->
            val cmd = row.split(" ")
            when (cmd[0]) {
                "noop" -> cpu.noop()
                "addx" -> cpu.add(cmd[1].toInt())
            }
        }
        return cpu.signalStrength()
    }

    fun part2(input: List<String>): String {
        val cpu = CPU()
        input.forEach { row ->
            val cmd = row.split(" ")
            when (cmd[0]) {
                "noop" -> cpu.noop()
                "addx" -> cpu.add(cmd[1].toInt())
            }
        }
        return cpu.draw()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140) { "part1 check failed" }

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
