package com.adventofcode

import java.util.Collections.max
import kotlin.math.abs


fun calculateMinimumFuelCost(crabs: List<Int>) : Int {
    // initialize the max possible position, which is the highest position of crab
    val maxPosition = max(crabs);
    // track cost
    var lowestCost = Int.MAX_VALUE

    // part 1: calculate fuel cost for every position
//    for (i in 0..maxPosition){
//        val currentCost = calculateFuelCost(crabs, i);
//        if (lowestCost == 0 || currentCost < lowestCost){
//            lowestCost = currentCost
//        }
//    }

    // part 2: calculate adjusted fuel cost for every position
    for (i in 0..maxPosition){
        val currentCost = calculateIncrementalFuelCost(crabs, i);
        if (lowestCost == 0 || currentCost < lowestCost){
            lowestCost = currentCost
        }
    }

    println(lowestCost)
    return lowestCost;
}

fun calculateFuelCost(crabs: List<Int>, currentPosition: Int) : Int {
    var sum = 0;
    for (crab in crabs) {
        sum += abs(crab - currentPosition)
    }
    println("Cost of $currentPosition is $sum")
    return sum

}

fun calculateIncrementalFuelCost(crabs: List<Int>, currentPosition: Int) : Int {
    var sum = 0;
    var stepsToMove = 0
    for (crab in crabs) {
        stepsToMove = abs(crab - currentPosition)
        sum += calculateFuelCostForSteps(stepsToMove)
    }
    return sum
}

fun calculateFuelCostForSteps(steps: Int) : Int {
    var sum = 0
    var costOfStep = 0
    for (i in 0..steps){
        sum += costOfStep
        costOfStep += 1
    }
    return sum
}


fun day7Results() {
    var testInput = readInput("day7_test")
    var input = readInput("day7")
    var crabs = input[0].split(",").map { n -> n.toInt() }
    calculateMinimumFuelCost(crabs)
}