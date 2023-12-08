import java.io.File
import kotlin.streams.toList

fun main() {
    val taskData = readTestFile()
    var count = 0
    var found = false
    var position = "AAA"

    while (!found) {
        val result = findFinalDestination(
            path = taskData.path.toCharArray(),
            crosses = taskData.crosses,
            lastPosition = position
        )
        count += result.count
        found = result.found
        position = result.lastPosition
    }
    println(count)
}

fun findFinalDestination(path: CharArray, crosses: List<Cross>, lastPosition: String): FindingResult {
    var count = 0
    var position = lastPosition
    var found = false
    path.forEach {
        val from = crosses.find { it.from == position }!!
        when (it){
            'L' -> {
                position = from.leftDestination
            }
            'R' -> {
                position = from.rightDestination
            }
        }
        count++
        if (position == "ZZZ"){
            found = true
            return@forEach
        }
    }
    return FindingResult(
        found = found,
         count = count,
        lastPosition = position
    )
}

fun readTestFile(): TaskData {
    val inputStream = File("src/main/resources/input.txt").inputStream()

    val lines = inputStream.bufferedReader().lines().filter { it.isNotBlank() }.toList()
    val path = lines[0]
    val crosses = mutableListOf<Cross>()
    lines.subList(1, lines.size - 1).forEach {
        val from = it.substringBefore('=').trim()
        val destinations = it.substringAfter('(').substringBefore(')')
        val leftDestination = destinations.substringBefore(',').trim()
        val rightDestination = destinations.substringAfter(',').trim()
        crosses.add(Cross(
            from = from,
            leftDestination = leftDestination,
            rightDestination = rightDestination
        ))
    }
    return TaskData(
        path = path,
        crosses = crosses
    )
}

data class TaskData(
    val path: String,
    val crosses: List<Cross>
)

data class Cross(
    val from: String,
    val leftDestination: String,
    val rightDestination: String
)

data class FindingResult(
    val found: Boolean,
    val count: Int,
    val lastPosition: String
)
