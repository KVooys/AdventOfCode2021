package com.adventofcode

import java.io.File

fun countDepthIncrements(input: List<Int>): Int {
    var increments = 0
    var previous = input[0]

    input.forEach {
        if (input.indexOf(it) > 0) {
            if (it > previous) {
                increments++;
            }
            previous = it;
        }
    }
    return increments;
}

fun countSlidingDepthIncrements(input: List<Int>): Int {
    var increments = 0
    var current_sum: Int;
    var previous = 0;
    for (i in 0..input.size-3) {
        current_sum = input.get(i) + input.get(i+1) + input.get(i+2);
        if (current_sum > previous && previous != 0){
            increments++;
        }
        previous = current_sum;
    }
    return increments;
}

fun day1Results(rootPath: String){

    var testInput : List<Int> = File(rootPath + "day1_test_input.txt").useLines { it.toList() }.map {n -> n.toInt()}
    var input : List<Int> = File(rootPath + "day1_input.txt").useLines { it.toList() }.map { n -> n.toInt() }

    println(countDepthIncrements(testInput))
    println(countSlidingDepthIncrements(testInput))
    println(countDepthIncrements(input));
    print(countSlidingDepthIncrements(input))
}
