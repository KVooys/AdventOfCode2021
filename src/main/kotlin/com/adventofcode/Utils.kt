package com.adventofcode

import java.io.File

fun readInput(path: String): List<String> {
    var rootPath = "C:\\Users\\kvooy\\Documents\\Code Projects\\AdventOfCode2021\\src\\main\\resources\\"

    return File("$rootPath$path".plus("_input.txt")).useLines { it.toList() }
}