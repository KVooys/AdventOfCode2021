package com.adventofcode

import java.util.*
import kotlin.system.exitProcess

private var totalFlashes = 0

fun stepOctopi(points: Map<Point, Int>): MutableMap<Point, Int>{
    val newPoints = mutableMapOf<Point, Int>()

    // track octopi that have flashed already this step
    val visited: MutableList<Point> = mutableListOf()

    // Increase entire grid by 1
    for ((k, v) in points) {
        newPoints[k] = v + 1
    }
    // Then, all points with value greater than 9 "flash", increasing all their (diagonal) neighbours by 1.
    // If any of those are now greater than 9, they also flash. So do a BFS every time.
    for ((k, v) in newPoints) {
        if (v > 9 && !visited.contains(k)) {
            println("Going from $k")

            var queue: ArrayDeque<Point> = ArrayDeque<Point>()

            visited.add(k)
            queue.add(k)
            totalFlashes += 1

            // Reset a point after flashing.
            newPoints[k] = 0

            while(queue.size != 0) {
                var point = queue.poll()
                var neighbours = getNeighboursDiagonally(point)
                for (n in neighbours.filter { n -> !visited.contains(n) }) {
                    if (newPoints[n] != null) {
                        // Increment neighbours
                        newPoints[n] = newPoints[n]?.plus(1)!!
                        if (newPoints[n]!! > 9) {
                            queue.add(n)
                            visited.add(n)
                            totalFlashes += 1
                        }
                    }
                }
            }
        }
    }
    // Finally, set all flashed octopi to 0.
    for (p in visited) {
        newPoints[p] = 0
    }
    // Part 2: stop at the step where all points flash.
    if (visited.size == newPoints.size) {
        println("STOP")
        exitProcess(0)
    }

    return newPoints
}

fun day11Results() {
    var smallInput = readInput("day11_small_test")
    var testInput = readInput("day11_test")
    var input = readInput("day11")
    var points = mapPoints(input)
    for (i in 0 until 400) {
        // Step 2, keep track of the step that flashes all points
        println(i)
        prettyPrintPointsMap(points)
        points = stepOctopi(points)

    }
    println(totalFlashes)
    println(points)
}

