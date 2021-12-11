package com.adventofcode

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
            initializeBasinProcessing(point, point, mutableSetOf(point), pointsMap)
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
// So we recur until a value of 8 is reached, then we stop.

fun initializeBasinProcessing(
    initPoint: Point,
    point: Point,
    basin: Set<Point>,
    pointsMap: MutableMap<Point, Int>
) {
    if (basin.size > globalSizeMap[initPoint]!!) {
        globalSizeMap[initPoint] = basin.size
    }

    val x = point.x
    val y = point.y
    val value = pointsMap[point]
    val pointNorth = Point(x, y - 1)
    val pointSouth = Point(x, y + 1)
    val pointWest = Point(x - 1, y)
    val pointEast = Point(x + 1, y)

    // no point in flowing from a point of value 8, since the next point would be 9 and can't be part of basin.
    if (value == 8) {
        return
    }

    // track new points
    val newPoints: MutableSet<Point> = mutableSetOf()

    // Try to flow in all directions, only if the adjacent exists, is unvisited and is 1 higher
    for (newPoint in listOf(pointNorth, pointSouth, pointWest, pointEast)){
        if (pointsMap[newPoint] != null && !basin.contains(newPoint) && pointsMap[newPoint] == pointsMap[point]?.plus(1)) {
            newPoints.add(newPoint)
        }
    }

    for (np in newPoints) {
        globalVisitedSet.add(np)
        initializeBasinProcessing(initPoint, np, basin.plus(newPoints), pointsMap)
    }

    if (globalVisitedSet.size > globalSizeMap[initPoint]!!) {
        globalSizeMap[initPoint] = globalVisitedSet.size
    }
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