package com.adventofcode

// Naive way to pass one day and keep track of the entire array
fun passOneDay(currentState: MutableList<Int>) : MutableList<Int> {
    var newState = mutableListOf<Int>()
    var newborns = 0
    for (v in currentState){
        if (v == 0) {
            newState.add(6)
            newborns += 1
        }
        else {
            newState.add(v - 1)
        }
    }

    // add newborns at the end of the list
    for (n in 0 until newborns){
        newState.add(8)
    }
    return newState
}

// helper to initialize map of counts
fun fishCountToMap(currentState: MutableList<Int>) : MutableMap<Int, Long> {
    var map = mutableMapOf<Int, Long>()
    for (i in 0..8){
        map[i] = currentState.count { it == i }.toLong();
    }
    return map
}


// More efficiently, instead of doing operations on the entire array,
// we can just count how many of each lanternfish we have and operate on the bulk
fun passOneDayAndCount(currentCount: MutableMap<Int, Long>) : MutableMap<Int, Long> {
    var newCount = mutableMapOf<Int, Long>()
    for (i in 0..8) {
        newCount[i] = 0
    }

    // base case, just count down these by one day
    for (i in 1..8){
        newCount[i-1] = currentCount[i]!!
    }
    // add newborns
    newCount[8] = currentCount[0]!!
    // reset count for fish after they gave birth
    newCount[6] = newCount[6]?.plus(currentCount[0]!!)!!

    return newCount

}

fun mapToSum(currentCount: MutableMap<Int, Long>) : Long {
    var sum: Long = 0
    for (i in 0..8){
        sum += currentCount[i]!!
    }
    return sum
}


fun day6Results() {
    // 3,4,3,1,2
    var testInput = readInput("day6_test")
    var input = readInput("day6")
    var sequence = input[0].split(",").map { n -> n.toInt() }.toMutableList()

    // part 1
//    for(n in 0..79){
//        sequence = passOneDay(sequence)
//    }
//    println(sequence.size)

    // part 2
    var map = fishCountToMap(sequence)
    for (n in 0..255) {
        println(n)
        map = passOneDayAndCount(map)
        println(map)
    }
    println(mapToSum(map))
}