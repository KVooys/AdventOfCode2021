package org.example.adventofcode

import java.io.File

fun main() {
    var rootPath = "C:\\Users\\kvooy\\Documents\\Code Projects\\AdventOfCode2021\\src\\main\\resources\\"

//    var input : List<Int> = File(rootPath + "day1_test_input.txt").useLines { it.toList() }.map { n -> n.toInt() }
    var input : List<Int> = File(rootPath + "day1_input.txt").useLines { it.toList() }.map { n -> n.toInt() }

    println(countDepthIncrements(input));
    print(countSlidingDepthIncrements(input))
}

