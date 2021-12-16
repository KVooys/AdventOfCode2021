package com.adventofcode

private fun parseInput(input: List<String>) {

    var pointsSet: MutableSet<Point> = mutableSetOf()

    val foldInstructions: MutableList<List<String>> = mutableListOf()

    for (line in input) {
        var coordRegex: Regex = Regex("\\d+,\\d+")
        // first section is a list of points
        if (line.matches(coordRegex)) {
            val parts = line.split(",")
            pointsSet.add(Point(parts[0].toInt(), parts[1].toInt()))
        }
        else if (line.isNotEmpty()) {
            val instructions = line.split("fold along ")
            val parts = instructions[1].split("=")
            foldInstructions.add(parts)
        }
    }

    for(i in foldInstructions) {
        pointsSet = foldPaper(i, pointsSet)
        // part 1: Do only 1 fold.
//        break
        // part 2: Read the final code
    }
    prettyPrintPointsList(pointsSet.toList())
}

fun foldPaper(instruction: List<String>, pointsSet: MutableSet<Point>): MutableSet<Point> {
    val direction = instruction[0]
    val foldLine = instruction[1].toInt()
    val pointsToAdd = mutableSetOf<Point>()
    val pointsToRemove = mutableSetOf<Point>()

    // Horizontal fold
    if (direction == "x") {
        // The bottom part of the pointsList is flipped and then added to the top half.
        // So if X is 7, a points with X=9 (7+2) is placed at X=5 (7-2).
        // Generally, if fold.X is a, the point will move to a - diff(point.x, a).
        for (point in pointsSet) {
            if (point.x > foldLine) {
                val newX = foldLine - (point.x - foldLine)
                println("Move $point horizontally to $newX, " + point.y)
                pointsToAdd.add(Point(newX, point.y))
                pointsToRemove.add(point)
            }
        }
    }
    else if (direction == "y") {
        // The right part of the pointsList is flipped and then added to the left half.
        // So if Y is 7, a points with Y=9 (7+2) is placed at Y=5 (7-2).
        // Generally, if fold.Y is a, the point will move to a - diff(point.Y, a).

        for (point in pointsSet) {
            if (point.y > foldLine) {
                val newY = foldLine - (point.y - foldLine)
                println("Move $point vertically to $newY")
                pointsToAdd.add(Point(point.x, newY))
                pointsToRemove.add(point)
            }
        }
    }

    for (point in pointsToAdd){
        pointsSet.add(point)
    }

    for (point in pointsToRemove) {
        println("Removing point $point")
        pointsSet.remove(point)
    }
    println(pointsSet.size)
    return pointsSet

}

fun day13Results() {
    var testInput = readInput("day13_test")
    var input = readInput("day13")
    parseInput(input)
}



