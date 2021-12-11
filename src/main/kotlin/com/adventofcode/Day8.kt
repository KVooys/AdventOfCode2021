package com.adventofcode

/*
Digits in a seven segment display look like this:
  0:      1:      2:      3:      4:
 aaaa    ....    aaaa    aaaa    ....
b    c  .    c  .    c  .    c  b    c
b    c  .    c  .    c  .    c  b    c
 ....    ....    dddd    dddd    dddd
e    f  .    f  e    .  .    f  .    f
e    f  .    f  e    .  .    f  .    f
 gggg    ....    gggg    gggg    ....

  5:      6:      7:      8:      9:
 aaaa    aaaa    aaaa    aaaa    aaaa
b    .  b    .  .    c  b    c  b    c
b    .  b    .  .    c  b    c  b    c
 dddd    dddd    ....    dddd    dddd
.    f  e    f  .    f  e    f  .    f
.    f  e    f  .    f  e    f  .    f
 gggg    gggg    ....    gggg    gggg

There is an easy to spot segment frequency:

8 have a
6 have b
8 have c
7 have d
4 have e
9 have f (only 2 doesn't)
7 have g

 These are entered as 1-7 inputs in a seven segment display, but the a...g have been mixed up.
 We need to deduce which is which by doing some sort of frequency and occurrence analysis.
 0 is length 6
 1 is the only one with length 2
 2 is length 5
 3 is length 5
 4 is the only one with length 4
 5 is length 5
 6 is length 6
 7 is the only one with length 3
 8 is the only one with length 7
 9 is length 6
 */

private fun parseLines(input: List<String>): Int {

    // part 1: count the easy digits
    var count = 0

    // part 2: sum the concatenated digits
    var sum = 0

    for (line in input) {
        var parts = line.trim().split("|")
        var sequences = parts[0].trim().split(" ")
        var output = parts[1].trim().split(" ")

        //part 1: count the number of 1s, 4s, 7s and 8s in the output
        for (digit in output) {
            if (checkIfEasyDigit(digit)) {
                count++
            }
        }

        // part 2: determine which char sequence means which digit & then concatenate the outputs to one digit.
        var digitsMap = determineSequencesPerInput(sequences)

        var concatenatedDigits = ""
        for (digit in output) {
            concatenatedDigits += digitsMap.get(digit.toSet())
        }
        println(concatenatedDigits)
        sum += concatenatedDigits.toInt()
    }
    println("Sum is $sum")
    return count
}

fun determineSequencesPerInput(sequences: List<String>): Map<Set<Char>, Int> {

    // Track mapping of <true sequence -> given sequence>.
    val trueSequenceMap: MutableMap<Char, Char> = mutableMapOf()

    val allChars: Set<Char> = setOf('a', 'b', 'c', 'd', 'e', 'f', 'g')

    // Define the four easy digits as their char sets.
    val one = sequences.filter { n -> n.length == 2 }[0].toSet()
    val seven = sequences.filter { n -> n.length == 3 }[0].toSet()
    val four = sequences.filter { n -> n.length == 4 }[0].toSet()
    val eight = sequences.filter { n -> n.length == 7 }[0].toSet()

    // Find the digit with length 2 and length 3, those are one and seven. The difference is segment A.
    val A = seven.minus(one).toCharArray()[0]
    trueSequenceMap['A'] = A

    // The difference between four and one is segments B and D. B occurs 6 times in total, D occurs 7 times.
    val BandD = four.minus(one).toCharArray()
    for (i in BandD) {
        if (sequences.filter { n -> n.contains(i) }.size == 6) {
            trueSequenceMap['B'] = i
        }

        if (sequences.filter { n -> n.contains(i) }.size == 7) {
            trueSequenceMap['D'] = i
        }
    }

    for (i in allChars) {
        // E is the only letter that occurs 4 times
        if (sequences.filter { n -> n.contains(i) }.size == 4) {
            trueSequenceMap['E'] = i
        }
        // There is another letter that occurs 7 times and is not D, which is G.
        if (sequences.filter { n -> n.contains(i) }.size == 7 && !trueSequenceMap.values.contains(i)) {
            trueSequenceMap['G'] = i
        }
        // There is another letter that occurs 8 times and is not A, which is C.
        if (sequences.filter { n -> n.contains(i) }.size == 8 && !trueSequenceMap.values.contains(i)) {
            trueSequenceMap['C'] = i
        }
    }

    // The only unindexed letter has to be F now.
    for (i in allChars) {
        if (!trueSequenceMap.values.contains(i)) {
            trueSequenceMap['F'] = i
        }
    }

    // Zero has length 6 and does not have D.
    val zero = sequences.filter { n -> n.length == 6 && !n.contains(trueSequenceMap['D']!!) }[0].toSet()

    // Six has length 6 and does not have E.
    val six = sequences.filter { n -> n.length == 6 && !n.contains(trueSequenceMap['C']!!) }[0].toSet()

    // Nine has length 6 and does not have E.
    val nine = sequences.filter { n -> n.length == 6 && !n.contains(trueSequenceMap['E']!!) }[0].toSet()

    // Two has length 5 and does not have F.
    val two = sequences.filter { n -> n.length == 5 && !n.contains(trueSequenceMap['F']!!) }[0].toSet()

    // Three has length 5 and does have both C and F.
    val three =
        sequences.filter { n -> n.length == 5 && n.contains(trueSequenceMap['C']!!) && n.contains(trueSequenceMap['F']!!) }[0].toSet()

    // Five has length 5 and does have both B and F.
    val five =
        sequences.filter { n -> n.length == 5 && n.contains(trueSequenceMap['B']!!) && n.contains(trueSequenceMap['F']!!) }[0].toSet()

    val mappedDigits = mutableMapOf<Set<Char>, Int>()

    mappedDigits[zero] = 0
    mappedDigits[one] = 1
    mappedDigits[two] = 2
    mappedDigits[three] = 3
    mappedDigits[four] = 4
    mappedDigits[five] = 5
    mappedDigits[six] = 6
    mappedDigits[seven] = 7
    mappedDigits[eight] = 8
    mappedDigits[nine] = 9

    return mappedDigits
}

fun checkIfEasyDigit(digit: String): Boolean {
    return arrayOf(2, 3, 4, 7).contains(digit.length)
}

fun day8Results() {
    var smallTestInput = "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf"
    var testInput = readInput("day8_test")
    var input = readInput("day8")

    println(parseLines(input))
}