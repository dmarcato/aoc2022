import kotlin.math.min

interface PacketEntry : Comparable<PacketEntry> {
    companion object
}

fun PacketEntry.Companion.from(content: String): PacketEntry {
    return when {
        content.isEmpty() -> PacketList(emptyList())
        content.startsWith("[") -> PacketList.from(content)
        else -> PacketNumber(content.toInt())
    }
}

@JvmInline
value class PacketNumber(val value: Int) : PacketEntry {
    override fun compareTo(other: PacketEntry): Int {
        return this.compare(other)
    }
}

@JvmInline
value class PacketList(val value: List<PacketEntry>) : PacketEntry {
    override fun compareTo(other: PacketEntry): Int {
        return this.compare(other)
    }
    companion object
}

fun PacketList.Companion.from(content: String): PacketList {
    val elements = mutableListOf<PacketEntry>()
    val stripped = content.substring(1 until content.length - 1)
    var index = 0
    val readSubContent = {
        var c = ""
        var openCount = 0
        do {
            when (stripped[index]) {
                '[' -> openCount++
                ']' -> openCount--
            }
            c += stripped[index++]
        } while (index < stripped.length && stripped[index] != ',' || openCount > 0)
        index++
        c
    }
    while (index < stripped.length) {
        elements.add(PacketEntry.from(readSubContent()))
    }
    return PacketList(elements)
}

fun PacketEntry.compare(other: PacketEntry): Int {
    return when {
        this is PacketNumber && other is PacketNumber -> {
            value.compareTo(other.value)
        }
        this is PacketList && other is PacketList -> {
            (0 until min(value.size, other.value.size)).firstOrNull {
                value[it].compare(other.value[it]) != 0
            }.let { index ->
                when {
                    index != null -> value[index].compare(other.value[index])
                    value.size == other.value.size -> 0
                    else -> value.size.compareTo(other.value.size)
                }
            }
        }
        this is PacketNumber && other is PacketList -> {
            PacketList(listOf(this)).compare(other)
        }
        this is PacketList && other is PacketNumber -> {
            this.compare(PacketList(listOf(other)))
        }
        else -> throw RuntimeException()
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        return input.windowed(2, 3, true) {
            val p1 = PacketEntry.from(it[0])
            val p2 = PacketEntry.from(it[1])
            p1.compare(p2) < 0
        }.withIndex().sumOf {
            if (it.value) it.index + 1 else 0
        }
    }
    fun part2(input: List<String>): Int {
        val divider1 = PacketEntry.from("[[2]]")
        val divider2 = PacketEntry.from("[[6]]")
        val sorted = (input.filter { it.isNotEmpty() }.map { PacketEntry.from(it) } + listOf(divider1, divider2)).sorted()
        val first = sorted.indexOfFirst { it == divider1 }
        val second = sorted.indexOfFirst { it == divider2 }
        return (first + 1) * (second + 1)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13) { "part1 check failed" }
    check(part2(testInput) == 140) { "part2 check failed" }

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
