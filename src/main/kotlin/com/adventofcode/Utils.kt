package com.adventofcode

import java.io.File



fun readInput(path: String): List<String> {
    var rootPath = "C:\\Users\\kvooy\\Documents\\Code Projects\\AdventOfCode2021\\src\\main\\resources\\"

    return File("$rootPath$path".plus("_input.txt")).useLines { it.toList() }
}


/**
 *  2D Grid utils: a simple point class
 */
data class Point(val x: Int, val y: Int)

/**
 *  Transform input to a map of Points and their values
 */
fun mapPoints(input: List<String>): Map<Point, Int> {
    val pointsMap: MutableMap<Point, Int> = mutableMapOf()

    for ((y, line) in input.withIndex()) {
        line.forEachIndexed { x, v ->
            pointsMap[Point(x, y)] = Character.getNumericValue(v)
        }
    }
    return pointsMap
}

/**
 * Return adjacent Points from a Point
 */
fun getNeighbours(point: Point): List<Point> {
    val x = point.x
    val y = point.y
    return listOf(
        Point(x + 1, y),
        Point(x - 1, y),
        Point(x, y + 1),
        Point(x, y - 1),
    )
}

/**
 * Return adjacent and diagonally adjacent Points from a Point
 */
fun getNeighboursDiagonally(point: Point): List<Point> {
    val x = point.x
    val y = point.y
    return listOf(
        Point(x + 1, y),
        Point(x - 1, y),
        Point(x, y + 1),
        Point(x, y - 1),
        Point(x - 1, y - 1),
        Point(x - 1, y + 1),
        Point(x + 1, y - 1),
        Point(x + 1, y + 1),
    )
}

/**
 * Pretty print a 2D map of Points
 */

fun prettyPrintPointsMap(points: Map<Point, Int>) {
    // find Y dimension
    val maxY: Int = (points.keys.map{ k -> k.y }.maxOrNull())!!
    // print every row
    for(y in 0 .. maxY) {
        println(points.filter { (k, v) -> k.y == y }.values)
    }
}