import kotlin.math.min

sealed interface Node

data class Dir(
    val name: String,
    val parent: Dir? = null,
    var children: MutableList<Node> = mutableListOf()
) : Node

data class File(
    val name: String,
    val size: Int
) : Node

fun Node.totalSize(): Int = when (this) {
    is File -> size
    is Dir -> children.sumOf { it.totalSize() }
}

@Suppress("unused")
fun Node.print(prefix: String = ""): String {
    return when (this) {
        is Dir -> {
            "$prefix- $name (dir)" + children.joinToString(separator = "\n", prefix = "\n") { it.print("$prefix  ") }
        }
        is File -> {
            "$prefix- $name (file, size=$size)"
        }
    }
}

fun Node.sumIfAtMost(atMost: Int): Int {
    return when (this) {
        is Dir -> {
            val totalSize = totalSize()
            if (totalSize <= atMost) {
                totalSize
            } else {
                0
            } + children.sumOf { it.sumIfAtMost(atMost) }
        }
        else -> 0
    }
}

fun Dir.findSmallest(minSize: Int): Int {
    val totalSize = totalSize()
    val subDirs = children.filterIsInstance<Dir>()
    return if (subDirs.isEmpty()) {
        totalSize
    } else {
        val childrenMin = subDirs.minOf { node ->
            val size = node.findSmallest(minSize)
            if (size >= minSize) size else Integer.MAX_VALUE
        }
        if (totalSize >= minSize) min(totalSize, childrenMin) else childrenMin
    }
}

fun main() {

    fun createFS(input: List<String>): Dir {
        val root = Dir("/")
        var pointer = root
        input.forEach { row ->
            when {
                row.startsWith("$") -> {
                    val pieces = row.trim('$', ' ').split(" ")
                    when (pieces[0]) {
                        "cd" -> {
                            when (val arg = pieces[1]) {
                                "/" -> pointer = root
                                ".." -> pointer.parent?.let { pointer = it }
                                else -> pointer = pointer.children.first { (it as? Dir)?.name == arg } as Dir
                            }
                        }
                    }
                }
                else -> {
                    val (typeOrSize, name) = row.split(" ")
                    when (typeOrSize) {
                        "dir" -> pointer.children.add(Dir(name, pointer))
                        else -> pointer.children.add(File(name, typeOrSize.toInt()))
                    }
                }
            }
        }
        return root
    }

    fun part1(input: List<String>): Int {
        val root = createFS(input)
        return root.sumIfAtMost(100000)
    }

    fun part2(input: List<String>): Int {
        val root = createFS(input)
        val freeSpace = 70000000 - root.totalSize()
        val minDelete = 30000000 - freeSpace
        return root.findSmallest(minDelete)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437) { "part1 check failed" }
    check(part2(testInput) == 24933642) { "part2 check failed" }

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
