package com.adventofcode

import java.util.*

data class Point(val x: Int, val y: Int)


// Ugly workaround for part 2...
var globalVisitedSet: MutableSet<Point> = mutableSetOf()
var globalSizeMap: MutableMap<Point, Int> = mutableMapOf()

// throw input in a point to value map
private fun parsePoints(input: List<String>): Map<Point, Int> {
    val pointsMap: MutableMap<Point, Int> = mutableMapOf()
    var risk = 0

    for ((y, line) in input.withIndex()) {
        line.forEachIndexed { x, v ->
            pointsMap[Point(x, y)] = Character.getNumericValue(v)
        }
    }

    // Part 1: low points have a risk of their height + 1. Sum them to the total risk.
    for ((point, value) in pointsMap.entries) {
        if (isPointALowPoint(point, pointsMap)) {
            risk += value + 1
        }
    }
    // Part 1 result
    println(risk)

    // Part 2: if a point is a low point, try to flow from it in all directions, expanding the basin.
    // then every point can flow in any direction again.
    for ((point, value) in pointsMap.entries) {
        if (isPointALowPoint(point, pointsMap)) {
            println("Initializing basin from $point")
            globalVisitedSet = mutableSetOf(point)
            globalSizeMap[point] = 1
            basinBFS(point, pointsMap)
        }
    }

    // Part 2 result
    println(
        globalSizeMap.values.sorted().reversed()[0] * globalSizeMap.values.sorted()
            .reversed()[1] * globalSizeMap.values.sorted().reversed()[2]
    )
    return pointsMap
}

// A basin can expand from a point in every direction, as long as the next value is not nine.
// Then from that point it can also expand in every direction.
// So a small BFS is implemented.
fun basinBFS(
    initPoint: Point,
    pointsMap: MutableMap<Point, Int>
) {
    val x = initPoint.x
    val y = initPoint.y
    val value = pointsMap[initPoint]
    val pointNorth = Point(x, y - 1)
    val pointSouth = Point(x, y + 1)
    val pointWest = Point(x - 1, y)
    val pointEast = Point(x + 1, y)
    val basin = mutableSetOf(initPoint)

    // BFS logic
    var queue: ArrayDeque<Point> = ArrayDeque<Point>()
    val visited: MutableSet<Point> = mutableSetOf()


    visited.add(initPoint)
    queue.add(initPoint)

    while (queue.size != 0) {
        var point = queue.poll()
        var neighbours = getNeighbours(point)

        for (newPoint in neighbours) {
            if (pointsMap[newPoint] != null && pointsMap[newPoint] != 9 && !basin.contains(newPoint)) {
                basin.add(newPoint)
                visited.add(newPoint)
                queue.add(newPoint)
            }
        }
    }
    println(basin.size)
    globalSizeMap[initPoint] = basin.size
}

private fun getNeighbours(point: Point): List<Point> {
    val x = point.x
    val y = point.y
    return listOf(
        Point(x + 1, y),
        Point(x - 1, y),
        Point(x, y + 1),
        Point(x, y - 1),
    )
}

// A point is a low point if it's lower than all its surrounding points
private fun isPointALowPoint(point: Point, pointsMap: Map<Point, Int>): Boolean {
    val x = point.x
    val y = point.y
    val pointNorth = Point(x, y - 1)
    val pointSouth = Point(x, y + 1)
    val pointWest = Point(x - 1, y)
    val pointEast = Point(x + 1, y)

    // Check all 4 adjacent points which has to either not exist, or be of higher value, to continue.
    if (pointsMap[pointNorth] == null || pointsMap[pointNorth]!! > pointsMap[point]!!) {
        if (pointsMap[pointSouth] == null || pointsMap[pointSouth]!! > pointsMap[point]!!) {
            if (pointsMap[pointWest] == null || pointsMap[pointWest]!! > pointsMap[point]!!) {
                if (pointsMap[pointEast] == null || pointsMap[pointEast]!! > pointsMap[point]!!) {
                    return true
                }
            }
        }
    }
    return false
}

fun day9Results() {
    var testInput = readInput("day9_test")
    var input = readInput("day9")
    parsePoints(input)
}