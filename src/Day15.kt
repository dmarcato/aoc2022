import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class PlanePos(val x: Int, val y: Int)

fun PlanePos.distance(x: Int, y: Int) = abs(this.x - x) + abs(this.y - y)
fun PlanePos.distance(pos: PlanePos) = distance(pos.x, pos.y)

enum class PlaneItem { Sensor, Beacon, NoBeacon }

fun PlaneItem?.symbol() = when (this) {
    PlaneItem.Sensor -> "S"
    PlaneItem.Beacon -> "B"
    PlaneItem.NoBeacon -> "#"
    else -> "."
}

class Plane {
    private val grid = mutableMapOf<PlanePos, PlaneItem>()
    private val distances = mutableMapOf<PlanePos, Int>()
    private var rangeX: IntRange? = null
    private var rangeY: IntRange? = null

    fun addItems(input: List<String>) {
        input.forEach { row ->
            val (sensor, beacon) = row.split(":").map { item ->
                "x=(-?\\d+), y=(-?\\d+)".toRegex().find(item)!!.groupValues.let { (_, x, y) ->
                    PlanePos(x.toInt(), y.toInt())
                }
            }
            val distance = sensor.distance(beacon)
            distances[sensor] = distance
            listOf(sensor, beacon).forEach { pos ->
                grid[pos] = when (pos) {
                    sensor -> PlaneItem.Sensor
                    else -> PlaneItem.Beacon
                }
                rangeX = min(pos.x - distance, rangeX?.first ?: Int.MAX_VALUE) .. max(pos.x + distance, rangeX?.last ?: Int.MIN_VALUE)
                rangeY = min(pos.y - distance, rangeY?.first ?: Int.MAX_VALUE) .. max(pos.y + distance, rangeY?.last ?: Int.MIN_VALUE)
            }
        }
    }

    fun noBeaconPosition(rowNum: Int): Int {
        return rangeX!!.count { x ->
            val item = PlanePos(x, rowNum)
            distances.any { (pos, distance) ->
                grid[item] == null && pos.distance(item) <= distance
            }
        }
    }

    fun tuningFrequency(area: IntRange): Long {
        val size = area.last - area.first
        (0 .. size).forEach { y ->
            var x = 0
            while (x <= size) {
                val sensorDistance = distances.firstNotNullOfOrNull { (pos, distance) ->
                    val itemDistance = pos.distance(x, y)
                    if (itemDistance <= distance) {
                        distance - itemDistance
                    } else {
                        null
                    }
                } ?: return x * 4000000L + y
                x += sensorDistance + 1
            }
        }
        throw RuntimeException()
    }

    @Suppress("unused")
    fun print() {
        buildString {
            rangeY!!.forEach { y ->
                rangeX!!.forEach { x ->
                    if (x == 500 && y == 0) append("+")
                    else append(grid[PlanePos(x, y)].symbol())
                }
                append("\n")
            }
        }.println()
    }

}

fun main() {

    fun part1(input: List<String>, rowToCheck: Int): Int {
        val plane = Plane()
        plane.addItems(input)
        return plane.noBeaconPosition(rowToCheck)
    }
    fun part2(input: List<String>, areaToCheck: Int): Long {
        val plane = Plane()
        plane.addItems(input)
        return plane.tuningFrequency(0 .. areaToCheck)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput, 10) == 26) { "part1 check failed" }
    check(part2(testInput, 20) == 56000011L) { "part2 check failed" }

    val input = readInput("Day15")
    part1(input, 2000000).println()
    part2(input, 4000000).println()
}
