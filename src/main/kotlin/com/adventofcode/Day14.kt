package com.adventofcode

private fun parseInput(input: List<String>) {
    var polymer = input[0]
    val instructions: MutableMap<String, String> = mutableMapOf()

    for (line in input.subList(2, input.size)) {
        val parts = line.split(" -> ")
        instructions[parts[0]] = parts[1]
    }
    println(polymer)
//    println(instructions)

    // Part 2 monstrosity
    // initialize count map
    var instructionCount = polymerToInstructionCount(polymer, instructions)

    for (i in 1..40) {
        // Part 1
        println("Step $i")
//        polymer = stepPolymer(polymer, instructions)

        instructionCount = stepPolymerButItsFaster(instructionCount, instructions)
        println(instructionCount)
    }

    var charFrequencies = countChars(instructionCount)
    println(charFrequencies)
    println((charFrequencies.values.sortedDescending()[0] - charFrequencies.values.sorted()[0])/2)
}

fun countChars(instructionCount: MutableMap<String, Long>): MutableMap<Char, Long> {
    val charMap: MutableMap<Char, Long> = mutableMapOf()
    for ((k, v) in instructionCount) {
        if (charMap[k[0]] == null){
            charMap[k[0]] = v
        }
        else {
            charMap[k[0]] = charMap[k[0]]?.plus(v)!!
        }
        if (charMap[k[1]] == null){
            charMap[k[1]] = v
        }
        else {
            charMap[k[1]] = charMap[k[1]]?.plus(v)!!
        }
    }
    return charMap
}

fun stepPolymer(polymer: String, instructions: MutableMap<String, String>): String {
    val newPolymer: StringBuilder = StringBuilder()
    newPolymer.append(polymer[0])

    for (i in 1 until polymer.length) {
        val pair = polymer.subSequence(i - 1, i + 1)
        if (instructions[pair] != null) {
            newPolymer.append((instructions[pair]))
        }
        newPolymer.append(pair[1])
    }
//    println(newPolymer)
    return newPolymer.toString()
}

fun stepPolymerButItsFaster(
    instructionCount: MutableMap<String, Long>,
    instructions: MutableMap<String, String>
): MutableMap<String, Long> {
    // The length of the polymer is irrelevant, as long as we keep counting the number of "instructions".
    // So let's just keep track of those and their resulting transformations.
    val newInstructionCount = mutableMapOf<String, Long>()
    println(instructionCount)

    for ((k, v) in instructionCount) {
        // Add both transformations.
        for (i in transformInstructions(k, instructions)) {
            println("$i occurs $v times")
            if (newInstructionCount[i] == null) {
                newInstructionCount[i] = v
                println("Initializing $i to $v")
            } else {
                newInstructionCount[i] = newInstructionCount[i]?.plus(v)!!
                println("Adding $i plus $v")
            }
        }

    }
    println(newInstructionCount.size)
    return newInstructionCount
}

fun polymerToInstructionCount(polymer: String, instructions: MutableMap<String, String>): MutableMap<String, Long> {
    val instructionCount: MutableMap<String, Long> = mutableMapOf()
    for (k in instructions.keys) {
        instructionCount[k] = 0
    }

    for (i in 1 until polymer.length) {
        val pair = polymer.subSequence(i - 1, i + 1).toString()
        instructionCount[pair] = instructionCount[pair]?.plus(1)!!
    }
    return instructionCount

}

// Every executed instruction always results in two new "instructions".
// For example NN -> NC and CN.
fun transformInstructions(instruction: String, instructions: MutableMap<String, String>): List<String> {
    val result = instructions[instruction]
    return listOf(
        instruction[0] + result!!,
        result + instruction[1]
    )
}

fun day14Results() {
    var testInput = readInput("day14_test")
    var input = readInput("day14")
    parseInput(input)
}





