import kotlin.math.max
import kotlin.math.min

data class CavePos(val x: Int, val y: Int)

enum class CaveMaterial { ROCK, SAND }

fun CaveMaterial?.symbol() = when (this) {
    CaveMaterial.ROCK -> "#"
    CaveMaterial.SAND -> "o"
    else -> "."
}

class Cave {
    private val grid = mutableMapOf<CavePos, CaveMaterial>()
    private var rangeX = IntRange(500, 500)
    private var rangeY = IntRange(0, 0)
    private val floorY: Int
        get() = rangeY.last + 1

    fun addRocks(input: List<String>) {
        input.forEach { row ->
            val positions = row.split("->").map {
                val (x, y) = it.trim().split(",")
                val pos = CavePos(x.toInt(), y.toInt())
                if (pos.x < rangeX.first) rangeX = IntRange(pos.x, rangeX.last)
                if (pos.x > rangeX.last) rangeX = IntRange(rangeX.first, pos.x)
                if (pos.y < rangeY.first) rangeY = IntRange(pos.y, rangeY.last)
                if (pos.y > rangeY.last) rangeY = IntRange(rangeY.first, pos.y)
                pos
            }
            var i = 0
            while (i < positions.size - 1) {
                val p1 = positions[i]
                val p2 = positions[++i]
                if (p1.x == p2.x) {
                    (min(p1.y, p2.y) .. max(p1.y, p2.y)).forEach {
                        grid[CavePos(p1.x, it)] = CaveMaterial.ROCK
                    }
                } else {
                    (min(p1.x, p2.x) .. max(p1.x, p2.x)).forEach {
                        grid[CavePos(it, p1.y)] = CaveMaterial.ROCK
                    }
                }
            }
        }
    }

    fun dropSand(withFloor: Boolean = false): Int {
        var drop = 0
        while (true) {
            var pos = CavePos(500, 0)
            if (grid[pos] == CaveMaterial.SAND) {
                return drop
            }
            while (grid[pos] == null && (!withFloor || pos.y < floorY)) {
                pos = pos.copy(y = pos.y + 1)
                if (!withFloor && !rangeY.contains(pos.y)) {
                    return drop
                }
                if (grid[pos] != null) {
                    pos = pos.copy(x = pos.x - 1, y = pos.y)
                    if (grid[pos] != null) {
                        pos = pos.copy(x = pos.x + 2)
                        if (grid[pos] != null) {
                            pos = pos.copy(x = pos.x - 1, y = pos.y - 1)
                            grid[pos] = CaveMaterial.SAND
                        }
                    }
                }
            }
            if (withFloor && pos.y == floorY) {
                grid[pos] = CaveMaterial.SAND
            }
            drop++
        }
    }

    @Suppress("unused")
    fun print(withFloor: Boolean = false) {
        buildString {
            rangeY.forEach { y ->
                rangeX.forEach { x ->
                    if (x == 500 && y == 0) append("+")
                    else append(grid[CavePos(x, y)].symbol())
                }
                append("\n")
            }
            if (withFloor) {
                (rangeY.last + 1..floorY).forEach { y ->
                    rangeX.forEach { x ->
                        append(grid[CavePos(x, y)].symbol())
                    }
                    append("\n")
                }
            }
        }.println()
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val cave = Cave()
        cave.addRocks(input)
        return cave.dropSand()
    }
    fun part2(input: List<String>): Int {
        val cave = Cave()
        cave.addRocks(input)
        return cave.dropSand(withFloor = true)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24) { "part1 check failed" }
    check(part2(testInput) == 93) { "part2 check failed" }

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}
