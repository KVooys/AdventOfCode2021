package com.adventofcode

import kotlin.math.pow

fun toDecimal(binaryNumber : String) : Int {
    var sum = 0
    binaryNumber.reversed().forEachIndexed {
            k, v -> sum += v.toString().toInt() * 2.0.pow(k.toDouble()).toInt()
    }
    return sum
}


fun findMostAndLeastCommonBits(input: List<String>): Int {
    var mostCommonResult = ""
    var leastCommonResult = ""
    for (i in 0 until input[0].length) {
        var zeroes = 0
        var ones = 0

        for (inp in input) {
            if (inp[i] == '0'){
                zeroes += 1
            }
            else {
                ones += 1
            }
        }
        if (zeroes > ones) {
            mostCommonResult += '0'
            leastCommonResult += '1'
        }
        else {
            mostCommonResult += '1'
            leastCommonResult += '0'
        }

    }
    return toDecimal(mostCommonResult) * toDecimal(leastCommonResult)
}

fun selectSubsetOfDigits(input: List<String>, index:Int, type: String) : List<String> {
    var zeroes = 0
    var ones = 0
    for (inp in input) {
        if (inp[index] == '0'){
            zeroes += 1
        }
        else {
            ones += 1
        }
    }
    if (type == "most"){
        if (zeroes > ones) {
            return input.filter { i -> i[index] == '0' }
        }
        else {
            return input.filter { i -> i[index] == '1' }
        }
    }
    else {
        if (zeroes <= ones) {
            return input.filter { i -> i[index] == '0' }
        }
        else {
            return input.filter { i -> i[index] == '1' }
        }

    }

}


fun keepMostCommonDigitOrOnes(input: List<String>) : Int {
    var strLength = input[0].length
    var index = strLength-1
    var current_input = input;
    for (i in 0 until strLength) {
        current_input = selectSubsetOfDigits(current_input, i, "most")
        if (current_input.size == 1){
            return toDecimal(current_input[0])
        }
        index--
        println(current_input)
        println(current_input.size)
    }
    return toDecimal(current_input[0])
}

fun keepLeastCommonDigitsOrZeroes(input: List<String>) : Int {
    var strLength = input[0].length
    var index = strLength-1
    var current_input = input;
    for (i in 0 until strLength) {
        current_input = selectSubsetOfDigits(current_input, i, "least")
        if (current_input.size == 1){
            return toDecimal(current_input[0])
        }
        index--
    }
    return toDecimal(current_input[0])
}


fun day3Results() {
    var testInput: List<String> = readInput("day3_test")
    var input: List<String> = readInput("day3")
    println(findMostAndLeastCommonBits(testInput))
    println(findMostAndLeastCommonBits(input))
    println(keepMostCommonDigitOrOnes(testInput) * keepLeastCommonDigitsOrZeroes(testInput))
    println(keepMostCommonDigitOrOnes(input) * keepLeastCommonDigitsOrZeroes(input))

}