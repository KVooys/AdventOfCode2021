package com.adventofcode

fun calculateDepthAndHorizontalPosition(input: List<String>): Int {
    var depth = 0
    var horizontalPosition = 0
    for (i in input) {
        val parts = i.split(" ")
        if (parts.contains("down")) {
            depth += parts[1].toInt()
        }
        if (parts.contains("up")) {
            depth -= parts[1].toInt()
        }
        if (parts.contains("forward")) {
            horizontalPosition += parts[1].toInt()
        }
    }
    return depth * horizontalPosition

}

/**
 *

down X increases your aim by X units.
up X decreases your aim by X units.
forward X does two things:
It increases your horizontal position by X units.
It increases your depth by your aim multiplied by X.

 */
fun calculateAim(input: List<String>): Int {
    var depth = 0
    var horizontalPosition = 0
    var aim = 0
    for (i in input) {
        val parts = i.split(" ")
        if (parts.contains("down")) {
            aim += parts[1].toInt()
        }
        if (parts.contains("up")) {
            aim -= parts[1].toInt()
        }
        if (parts.contains("forward")){
            horizontalPosition += parts[1].toInt();
            depth += aim * parts[1].toInt();
        }
    }
    return horizontalPosition * depth

}

fun day2Results() {
    var testInput: List<String> = readInput("day2_test")
    println(calculateDepthAndHorizontalPosition(testInput))
    var input: List<String> = readInput("day2")
    println(calculateDepthAndHorizontalPosition(input))

    println(calculateAim(testInput))
    println(calculateAim(input))
}