package com.adventofcode

private var scores = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137
)

private var openingCharacters = charArrayOf('(', '[', '{', '<')
private var closingCharacters = charArrayOf(')', ']', '}', '>')

private fun scoreSyntaxErrors(input: List<String>) {
    var sum = 0
    var scores = mutableListOf<Long>()
    for (line in input) {
        // part 1: sum corrupted  line values
        sum += scoreIfCorrupted(line)

        // part 2: ignore corrupted lines and calculate the score for autocompleting incomplete lines.
        if (scoreIfCorrupted(line) == 0) {
            scores.add(calculateAutoCompletionScore(findCharsToAutoComplete(line)))
        }
    }
    println(sum)
    println(scores.sorted()[scores.size / 2])
}

/**
 * () [] {} <>
A corrupted line is one where a chunk closes with the wrong character -
that is, where the characters it opens and closes with do not form one of the four legal pairs listed above.
 */
private fun scoreIfCorrupted(line: String): Int {
    // Track parsed opening chars
    var lastOpeners: ArrayDeque<Char> = ArrayDeque()

    // track the most recent opening character, and see if it matches the closed character.
    for (char in line) {
        if (char in openingCharacters) {
            lastOpeners.add(char)
        } else {
            if (openingCharacters.indexOf(lastOpeners.last()) != closingCharacters.indexOf(char)) {
                return scores[char]!!
            } else {
                lastOpeners.removeLast()
            }
        }
    }
    return 0
}

private fun findCharsToAutoComplete(line: String): List<Char> {
    // Track parsed opening chars
    var lastOpeners: ArrayDeque<Char> = ArrayDeque()
    var charsNeededToComplete: MutableList<Char> = mutableListOf()

    // track the most recent opening character, and see if it matches the closed character.
    for (char in line) {
        if (char in openingCharacters) {
            lastOpeners.add(char)
        } else {
            // it doesn't match, so we add the needed closing character to our autocomplete sequence.
            if (openingCharacters.indexOf(lastOpeners.last()) != closingCharacters.indexOf(char)) {
                println("Found a corruption, exit anyway.")
                break
            } else {
                lastOpeners.removeLast()
            }
        }
    }
    for (char in lastOpeners.reversed()) {
        charsNeededToComplete.add(closingCharacters[openingCharacters.indexOf(char)])
    }
    println(charsNeededToComplete)
    return charsNeededToComplete
}

fun calculateAutoCompletionScore(findCharsToAutoComplete: List<Char>): Long {
    var autoCompleteScores = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4
    )
    var total: Long = 0

    for (char in findCharsToAutoComplete) {
        total *= 5
        total += autoCompleteScores[char]!!
    }
    println(total)
    return total
}

fun day10Results() {
    var testInput = readInput("day10_test")
    var input = readInput("day10")
    scoreSyntaxErrors(input)
}

