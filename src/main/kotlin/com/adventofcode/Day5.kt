package com.adventofcode

import java.io.File
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max

private fun parseVentCoords(input: List<String>) {
    var grid = createLargeGrid()

    for (line in input) {
        var parts = line.split(" -> ")
        var startParts = parts[0].split(",")
        var startXCoord = startParts[0].toInt()
        var startYCoord = startParts[1].toInt()
        var endParts = parts[1].split(",")
        var endXCoord = endParts[0].toInt()
        var endYCoord = endParts[1].toInt()

        println("Traversing $startXCoord, $startYCoord, $endXCoord, $endYCoord")
        grid = traverseGrid(grid, startXCoord, startYCoord, endXCoord, endYCoord)

    }
//    println(grid)
    println(calculateTwoOrMoreOverlappingLines(grid))
}

fun calculateTwoOrMoreOverlappingLines(grid: MutableList<MutableList<Int>>): Int {
    var total = 0
    for (y in 0..1000) {
        for (x in 0..1000) {
            if (grid[y][x] >= 2) {
                total++
            }
        }
    }
    return total
}

private fun traverseGrid(
    grid: MutableList<MutableList<Int>>,
    startXCoord: Int,
    startYCoord: Int,
    endXCoord: Int,
    endYCoord: Int
): MutableList<MutableList<Int>> {
    // Part 1: traverse only every straight-line coordinate pair
    if (startXCoord == endXCoord || startYCoord == endYCoord) {
        println("Traversing horizontally")
        for (y in min(startYCoord, endYCoord)..max(startYCoord, endYCoord)) {
            for (x in min(startXCoord, endXCoord)..max(startXCoord, endXCoord)) {
                println("Horizontal point $x $y")
                grid[y][x] += 1
            }
        }
    }

    // Part 2: traverse lines diagonally as well
    else {
        println("Traversing diagonally")
        // base case, both increasing
        if (startXCoord < endXCoord && startYCoord < endYCoord) {
            println("Both increasing")
            for (y in startYCoord..endYCoord) {
                for (x in startXCoord..endXCoord) {
                    if (x - startXCoord == y - startYCoord) {
                        println("Diagonal point $x $y")
                        grid[y][x] += 1
                    }
                }
            }
        }

        // both decreasing
        else if (endXCoord < startXCoord && endYCoord < startYCoord) {
            println("Both decreasing")
            for (y in startYCoord downTo endYCoord) {
                for (x in startXCoord downTo endXCoord) {
                    if (abs(endXCoord - x) == abs(endYCoord - y)) {
                        println("Diagonal point $x $y")
                        grid[y][x] += 1
                    }
                }
            }
        }

        // x decreasing, y increasing
        else if (startXCoord > endXCoord && startYCoord < endYCoord) {
            println("x decreasing")
            for (y in startYCoord..endYCoord) {
                for (x in startXCoord downTo endXCoord) {
                    if (startXCoord + startYCoord == x + y) {
                        println("Diagonal point $x $y")
                        grid[y][x] += 1
                    }
                }
            }
        }

        // y decreasing, x increasing
        else if (endXCoord > startXCoord && endYCoord < startYCoord) {
            println("y decreasing")
            for (y in startYCoord downTo endYCoord) {
                for (x in startXCoord..endXCoord) {
                    if (startXCoord + startYCoord == x + y) {
                        println("Diagonal point $x $y")
                        grid[y][x] += 1
                    }
                }
            }
        }
    }
    return grid
}

// create a 1000x1000 zeroes grid so the exact dimensions won't need to be done later
private fun createLargeGrid(): MutableList<MutableList<Int>> {
    var grid: MutableList<MutableList<Int>> = mutableListOf()
    for (y in 0..1000) {
        grid.add(mutableListOf())
        for (x in 0..1000) {
            grid[y].add(0)
        }
    }
    return grid
}


fun day5Results(rootPath: String) {
    var testInput: List<String> = File(rootPath + "day5_test_input.txt").useLines { it.toList() }
    var input: List<String> = File(rootPath + "day5_input.txt").useLines { it.toList() }

    parseVentCoords(input)
}