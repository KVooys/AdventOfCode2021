package com.adventofcode

import java.util.*
import kotlin.math.abs
import kotlin.math.sqrt


private fun parseInput(input: List<String>) {
    var pointsMap = mapPoints(input)
    var size = sqrt(pointsMap.size.toDouble()).toInt() - 1

    // part 1
    println(dijkstra(Point(0, 0), pointsMap))

    // part 2, explode the input first.
    explodeInput(input)

}

fun explodeInput(input: List<String>) {
    val rowSize = input.size
    val colSize = input[0].length
    val pointsMap = mutableMapOf<Point, Int>()

    fun Int.nextLevel(): Int = if (this > 9) abs(this - 9) else this

    for (y in 0 until rowSize){
        for (x in 0 until colSize){
            var counter = 1
            pointsMap[Point(x, y)] = input[y][x].digitToInt()

            repeat(4){
                val newCol = x + (colSize * counter)
                val newRow = y + (rowSize * counter)
                pointsMap[Point(newCol, y)] = (pointsMap[Point(x, y)]!! + it + 1).nextLevel()
                pointsMap[Point(x, newRow)] = (pointsMap[Point(x, y)]!! + it + 1).nextLevel()

                var newCounter = 1
                repeat(4){ count ->
                    pointsMap[
                            Point( x + (colSize * counter), y + (rowSize * newCounter))] =
                        (pointsMap[Point(newCol, y)]!! + count + 1).nextLevel()
                    ++newCounter
                }

                ++counter
            }
        }
    }
    println(dijkstra(Point(0, 0), pointsMap))
}

private fun isLocationValid(point: Point, pointsMap: Map<Point, Int>): Boolean {
    return pointsMap[point] != null
}

private fun getValidNeighbours(point: Point, pointsMap: Map<Point, Int>): MutableList<Point> {
    val validNeighbours = mutableListOf<Point>()
    for (n in getNeighbours(point)) {
        if (isLocationValid(n, pointsMap)) {
            validNeighbours.add(n)
        }
    }
    return validNeighbours
}

private data class VertexDistancePair(
    val location: Point,
    val riskLevel: Int
)

private class VertexDistancePairComparator : Comparator<VertexDistancePair> {
    override fun compare(first: VertexDistancePair, second: VertexDistancePair): Int {
        return first.riskLevel.compareTo(second.riskLevel)
    }
}

private fun dijkstra(start: Point, pointsMap: Map<Point, Int>): Int {
    val priorityQueue = PriorityQueue(VertexDistancePairComparator())
    val visited = hashSetOf<Point>()
    val totalRiskLevel = hashMapOf<Point, Int>()

    totalRiskLevel[start] = 0

    // init stack operation with the first point, not counting its risk value.
    priorityQueue.add(VertexDistancePair(start, 0))

    while (priorityQueue.isNotEmpty()) {
        val (location, riskLevel) = priorityQueue.remove()
        visited.add(location)

        if (totalRiskLevel.getOrDefault(location, Int.MAX_VALUE) < riskLevel) continue

        for (adj in getValidNeighbours(location, pointsMap)) {
            if (visited.contains(adj)) continue
            val newRiskLevel = totalRiskLevel.getOrDefault(location, Int.MAX_VALUE) + pointsMap[adj]!!
            if (newRiskLevel < totalRiskLevel.getOrDefault(adj, Int.MAX_VALUE)) {
                totalRiskLevel[adj] = newRiskLevel
                priorityQueue.add(VertexDistancePair(adj, newRiskLevel))
            }
        }
    }

    // Endpoint
    return totalRiskLevel[
            Point(
                sqrt(pointsMap.size.toDouble()).toInt() - 1,
                sqrt(pointsMap.size.toDouble()).toInt() - 1
            )
    ]!!
}

fun day15Results() {
    var testInput = readInput("day15_test")
    var input = readInput("day15")
    parseInput(input)
}





