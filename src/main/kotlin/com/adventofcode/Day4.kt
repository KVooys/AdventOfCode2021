package com.adventofcode

const val BINGO_SIZE = 5
const val PLACEHOLDER_NUMBER = 111

fun parseCalledNumbers(input: List<String>): List<Int> {
    return input[0].split(",").map { i -> i.toInt() }
}

fun parseBingoCards(input: List<String>): MutableList<MutableList<MutableList<Int>>> {
    val filteredInput = input.filter { line -> line.isNotEmpty() }
    var bingoCards: MutableList<MutableList<MutableList<Int>>> = mutableListOf()

    // every 5 rows after the first (which has the called numbers), make a new bingo card
    for (inp in 1..filteredInput.size - 4 step BINGO_SIZE) {
        var bingoCard: MutableList<MutableList<Int>> = mutableListOf()
        // throw rows into card
        for (i in 0 until BINGO_SIZE) {
            val parsedRow = filteredInput[inp + i].split(" ").filter { e -> e.isNotEmpty() }.map { e -> e.toInt() }
            bingoCard.add(parsedRow.toMutableList())
        }
        bingoCards.add(bingoCard)
    }
    return bingoCards
}

fun callNumbers(calledNumbers: List<Int>, bingoCards: MutableList<MutableList<MutableList<Int>>>) {
    var winners = mutableSetOf<Int>()
    for (calledNumber in calledNumbers) {
        for (bingoCard in bingoCards) {
            scoreCard(calledNumber, bingoCard)
            if (checkVictory(bingoCard)) {
                if (!winners.contains(bingoCards.indexOf(bingoCard))){
                    println(bingoCards.indexOf(bingoCard))
                    println(calculatePoints(calledNumber, bingoCard))
                    winners.add(bingoCards.indexOf(bingoCard))
                }
            }
        }
    }
}

fun scoreCard(calledNumber: Int, bingoCard: MutableList<MutableList<Int>>): Boolean {
    // check every row for number and replace with placeholder if found
    for (i in 0 until BINGO_SIZE) {
        for (j in 0 until BINGO_SIZE) {
            if (bingoCard[i][j] == calledNumber) {
                bingoCard[i][j] = PLACEHOLDER_NUMBER
                return true
            }
        }
    }
    return false
}

fun checkVictory(bingoCard: MutableList<MutableList<Int>>): Boolean {

    for (i in 0 until BINGO_SIZE) {
        // horizontal victory
        if (bingoCard[i] == listOf(
                PLACEHOLDER_NUMBER,
                PLACEHOLDER_NUMBER,
                PLACEHOLDER_NUMBER,
                PLACEHOLDER_NUMBER,
                PLACEHOLDER_NUMBER
            )
        ) {
            return true
        }
        // vertical victory
        if (bingoCard[0][i] == PLACEHOLDER_NUMBER &&
            bingoCard[1][i] == PLACEHOLDER_NUMBER &&
            bingoCard[2][i] == PLACEHOLDER_NUMBER &&
            bingoCard[3][i] == PLACEHOLDER_NUMBER &&
            bingoCard[4][i] == PLACEHOLDER_NUMBER
        ) {
            return true
        }
    }
    return false
}

fun calculatePoints(calledNumber: Int, bingoCard: MutableList<MutableList<Int>>): Int {
    var cardTotal = 0
    println(bingoCard)
    for (i in 0 until BINGO_SIZE){
        for (j in 0 until BINGO_SIZE) {
            if (bingoCard[i][j] != PLACEHOLDER_NUMBER){
                cardTotal += bingoCard[i][j]
            }
        }
    }
    return calledNumber * cardTotal
}


fun day4Results() {
    var testInput: List<String> = readInput("day4_test")
    var input: List<String> = readInput("day4")

//    var calledNumbers = parseCalledNumbers(testInput)
//    var bingoCards = parseBingoCards(testInput)
//    callNumbers(calledNumbers, bingoCards)

    var calledNumbers = parseCalledNumbers(input)
    var bingoCards = parseBingoCards(input)
    callNumbers(calledNumbers, bingoCards)

}