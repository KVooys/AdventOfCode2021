package com.adventofcode

import java.util.*

private var maxYMap: MutableMap<List<Int>, Int> = mutableMapOf()


private fun parseTargetArea(input: List<String>) : List<Point> {
    var pointsList = mutableListOf<Point>()
    var targetRegex: Regex = Regex("target area: x=(-?\\d+)\\.\\.(-?\\d+), y=(-?\\d+)\\.\\.(-?\\d+)")
    var matches = targetRegex.find(input[0])
    if (matches != null) {
        // First match is the entire match object, so ignore it.
        var targetXStart = matches.groupValues[1].toInt()
        var targetXEnd = matches.groupValues[2].toInt()
        var targetYStart = matches.groupValues[3].toInt()
        var targetYEnd = matches.groupValues[4].toInt()
        println("$targetXStart $targetXEnd $targetYStart $targetYEnd")
        for (y in targetYStart until targetYEnd){
            for (x in targetXStart until targetXEnd){
                pointsList.add(Point(x, y))
            }
        }
        // Bruteforce some X and Y velocities and simulate their trajectory.
        // Only bruteforce with X velocity between 0 and maxX.
        for (xVelo in 0..targetXEnd){
            for (yVelo in -1000..1000) {
                projectTrajectory(xVelo, yVelo, targetXStart, targetXEnd, targetYStart, targetYEnd)
            }
        }
        println(maxYMap)
        // Part 1
        println(maxYMap.values.maxOrNull())
        // Part 2
        println(maxYMap.size)
    }
    return pointsList
}

fun projectTrajectory(xVelocity: Int, yVelocity: Int, targetXStart: Int, targetXEnd: Int, targetYStart: Int, targetYEnd: Int) {
    var currentXVelocity = xVelocity
    var currentYVelocity = yVelocity
    var maxY = Integer.MIN_VALUE
    var initPoint = Point(0, 0)

    // add steps in a BFS way. Only add steps if the next step would be within some reasonable bounds.
    var queue: ArrayDeque<Point> = ArrayDeque<Point>()
    queue.add(initPoint)
    while(!queue.isEmpty()){
        val currentPoint = queue.poll()
        if (currentPoint.y > maxY){
            maxY = currentPoint.y
        }
        else if (currentPoint.y < Integer.MIN_VALUE) {
            break
        }
//        println("Processing $currentPoint with XVelo $currentXVelocity and YVelo $currentYVelocity")

        if (pointIsInTargetArea(currentPoint, targetXStart, targetXEnd, targetYStart, targetYEnd)) {
            println("Done XVelo $xVelocity and YVelo $yVelocity with Max Y $maxY")
            maxYMap[listOf(xVelocity, yVelocity)] = maxY
            return
        }
        else {
            /**
             *
            The probe's x position increases by its x velocity.
            The probe's y position increases by its y velocity.
            Due to drag, the probe's x velocity changes by 1 toward the value 0; that is, it decreases by 1 if it is greater than 0, increases by 1 if it is less than 0, or does not change if it is already 0.
            Due to gravity, the probe's y velocity decreases by 1.

             */
            val nextPoint = Point(currentPoint.x + currentXVelocity, currentPoint.y + currentYVelocity)
            if (currentXVelocity > 0){
                currentXVelocity--
            }
            else if (currentXVelocity < 0){
                currentXVelocity++
            }
            currentYVelocity--

            // Some reasonable values to bound. If it's straight dropping and not going to hit the target anyway, kill it.

            if (nextPoint.x in -300..300
                && !(currentXVelocity == 0 && nextPoint.x !in targetXStart..targetXEnd)
            && (nextPoint.y >= -100000)) {
//                println("$nextPoint, $targetXStart, $targetXEnd")
                queue.add(nextPoint)
            }
        }
    }
}

private fun pointIsInTargetArea(point: Point, targetXStart: Int, targetXEnd: Int, targetYStart: Int, targetYEnd: Int) : Boolean {
    return point.x in targetXStart..targetXEnd &&
            point.y in targetYStart..targetYEnd
}


fun day17Results() {
    //target area: x=241..275, y=-75..-49
    var testInput = readInput("day17_test")
    var input = readInput("day17")
    parseTargetArea(input)
}


