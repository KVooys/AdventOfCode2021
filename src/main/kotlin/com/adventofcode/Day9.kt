package com.adventofcode

data class Point(val x: Int, val y: Int)


// Ugly workaround for part 2 counting...
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

    // Part 2: if a point is a low point, try to flow from it in all directions, expanding the basin.
    // then every point can flow in any direction again.
    for ((point, value) in pointsMap.entries) {
        if (isPointALowPoint(point, pointsMap)) {
            println("Initializing basin from $point")
            globalSizeMap[point] = 1
            initializeBasinProcessing(point, mutableMapOf(point to false), pointsMap)

            println(globalSizeMap.values.sorted().reversed())
        }
    }
    println(risk)
    println(globalSizeMap.values.sorted().reversed())
    return pointsMap
}

// A basin can expand from its low point in every direction, as long as the next value is not nine.
// Then from every direction it can also expand in every direction.
// So we recur until a value is 8, then we stop.

// Track local maximum.
fun initializeBasinProcessing(initPoint: Point, basin: MutableMap<Point, Boolean>, pointsMap: MutableMap<Point, Int>) {

    for ((point, visited) in basin.entries) {
        // No need to flow from a point we already used to flow from.
        if (visited) {
            continue
        } else {
            var goAgain = false
            var newBasin = basin.toMutableMap()
            newBasin[point] = true
            val x = point.x
            val y = point.y
            val value = pointsMap[point]
            val pointNorth = Point(x, y - 1)
            val pointSouth = Point(x, y + 1)
            val pointWest = Point(x - 1, y)
            val pointEast = Point(x + 1, y)
            // no point in flowing from a point of 8, since the next point would be 9.
            if (value == 8) {
                break
            }

            // Try to flow in all directions, only if the adjacent exists, is unvisited and is 1 higher
            if (pointsMap[pointNorth] != null && newBasin[pointNorth] == null && pointsMap[pointNorth] == pointsMap[point]?.plus(
                    1
                )
            ) {
//                println("North point is available to flow to from $point")
                newBasin[pointNorth] = false
                goAgain = true
            }
            if (pointsMap[pointSouth] != null && newBasin[pointSouth] == null && pointsMap[pointSouth] == pointsMap[point]?.plus(
                    1
                )
            ) {
//                println("South point is available to flow to from $point")
                newBasin[pointSouth] = false
                goAgain = true
            }

            if (pointsMap[pointWest] != null && newBasin[pointWest] == null && pointsMap[pointWest] == pointsMap[point]?.plus(
                    1
                )
            ) {
//                println("West point is available to flow to from $point")
                newBasin[pointWest] = false
                goAgain = true
            }

            if (pointsMap[pointEast] != null && newBasin[pointEast] == null && pointsMap[pointEast] == pointsMap[point]?.plus(
                    1
                )
            ) {
//                println("East point is available to flow to from $point")
                newBasin[pointEast] = false
                goAgain = true
            }

            if (goAgain) {
                initializeBasinProcessing(initPoint, newBasin, pointsMap)
                if (newBasin.size > globalSizeMap[initPoint]!!) {
                    globalSizeMap[initPoint] = newBasin.size
                }
            }
        }
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
    parsePoints(testInput)
}