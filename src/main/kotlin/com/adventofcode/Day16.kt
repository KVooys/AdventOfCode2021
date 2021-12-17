package com.adventofcode

private var versionSum = 0

private val hexToBinaryMap = mapOf(
    '0' to "0000",
    '1' to "0001",
    '2' to "0010",
    '3' to "0011",
    '4' to "0100",
    '5' to "0101",
    '6' to "0110",
    '7' to "0111",
    '8' to "1000",
    '9' to "1001",
    'A' to "1010",
    'B' to "1011",
    'C' to "1100",
    'D' to "1101",
    'E' to "1110",
    'F' to "1111"
)

private fun decodeHex(hexString: String): String {
    var byteString = ""
    for (char in hexString) {
        byteString += hexToBinaryMap[char]
    }
    return byteString
}


private fun parseInput(input: List<String>): String {
    val hexEncodedInput = input[0]
    val decoded = decodeHex(hexEncodedInput)
    return decoded
}

private fun checkIfLiteralPacket(packet: String): Boolean {
    return packet.substring(3, 6).toInt(2) == 4

}

private fun parseOperatorPacket(packet: String): Map<String, Int> {

    val version = packet.substring(0, 3).toInt(2)
    versionSum += version
    val typeId = packet.substring(3, 6).toInt(2)
    val lengthTypeId = packet[6]
    println("Operator packet $packet with version $version, typeId $typeId and lengthTypeId $lengthTypeId")
    if (lengthTypeId == '0') {
        val totalLength = packet.substring(8, 22).toInt(2)
        // the remaining string is to be parsed as packets again.
        parseStringOfPacketsWithKnownLength(packet.substring(22, packet.length), totalLength)
        return mapOf(
            "version" to version,
            "typeId" to typeId,
            "subPacketCount" to 0,
            "length" to 22 + totalLength
        )
    } else if (lengthTypeId == '1') {
        val numberOfNextSubPackets = packet.substring(8, 18).toInt(2)
        val subPacketLength = parseStringOfPacketsWithKnownCount(packet.substring(18, packet.length), numberOfNextSubPackets, 0)
        println("Subpacket length $subPacketLength")
        return mapOf(
            "version" to version,
            "typeId" to typeId,
            "subPacketCount" to numberOfNextSubPackets,
            "length" to 22 + subPacketLength
        )
    }
    return mapOf()
}

private fun parseStringOfPacketsWithKnownLength(packets: String, totalLength: Int) {
    println("String with known length $totalLength")

    // Next packet is a literal packet, so process it.
    if (checkIfLiteralPacket(packets)) {
        val currentPacket = parseLiteralPacket(packets)
        println("Current literal packet in string: $currentPacket")
        val packetLength = currentPacket["length"]!!
        // Keep going, there are more packets in this string.
        if (totalLength - packetLength > 0) {
            parseStringOfPacketsWithKnownLength(
                packets.substring(packetLength, packets.length),
                totalLength - packetLength
            )
        }
    }

    // Next packet is an operator packet, so process it.
    else {
        val currentPacket = parseOperatorPacket(packets)
        println("Current operator packet in string: $currentPacket")
        val packetLength = currentPacket["length"]!!
        // Keep going, there are more packets in this string.
        if (totalLength - packetLength > 0) {
            // TODO: fix this.
            parseStringOfPacketsWithKnownLength(
                packets.substring(packetLength, packets.length),
                totalLength - packetLength
            )
        }
    }
}

// A string of counted packets has to return the length of all its subpackets, so the upper layer knows where the packet after it starts.
private fun parseStringOfPacketsWithKnownCount(packets: String, count: Int, length: Int): Int {
    println("String with known count $count")

    var newLength = 0
    if (count >= 1) {
        if (checkIfLiteralPacket(packets)) {
            val currentPacket = parseLiteralPacket(packets)
            println("Current literal packet in string: $currentPacket")
            val packetLength = currentPacket["length"]!!
            newLength = length + packetLength
            parseStringOfPacketsWithKnownCount(packets.substring(packetLength, packets.length), count - 1, newLength)
        }

        // Next packet is an operator packet, so process it.
        else {
            val currentPacket = parseOperatorPacket(packets)
            println("Current operator packet in string: $currentPacket")
            val packetLength = currentPacket["length"]!!
            newLength = length + packetLength
            // TODO: fix this.
//            parseStringOfPacketsWithKnownCount(packets.substring(packetLength, packets.length), count - 1, newLength)
        }
    }
    return newLength
}

// Once a packet is known to be a literal packet, extract its contents and take note of its length.
private fun parseLiteralPacket(packet: String): Map<String, Int> {

    val version = packet.substring(0, 3).toInt(2)
    versionSum += version
    val typeId = packet.substring(3, 6).toInt(2)
    println("Literal packet $packet with version $version and typeId $typeId")
    val literalBits = packet.substring(6, packet.length)
    var end = false
    var idx = 0
    var literalString = ""
    while (!end) {
        val headerBit = literalBits[idx]
        val realBits = literalBits.substring(idx + 1, idx + 5)
        literalString += realBits
        idx += 5
        if (headerBit == '0') {
            end = true
        }
    }

    // The actual length was 6 + the idx
    val length = 6 + idx
    val value = literalString.toInt(2)

    return mapOf(
        "version" to version,
        "typeId" to typeId,
        "value" to value,
        "length" to length
    )
}

fun day16Results() {
    var testInput = readInput("day16_test")
    var input = readInput("day16")
    val decoded = parseInput(testInput)

    // bunch of logic tests
//    check(checkIfLiteralPacket("110100101111111000101000"))
//    check(parseLiteralPacket("110100101111111000101000") == mapOf("version" to 6, "typeId" to 4, "value" to 2021, "length" to 21))
//    check(parseLiteralPacket("11010001010") == mapOf("version" to 6, "typeId" to 4, "value" to 10, "length" to 11))
//    check(parseLiteralPacket("0101001000100100") == mapOf("version" to 2, "typeId" to 4, "value" to 20, "length" to 16))

//    parseOperatorPacket("00111000000000000110111101000101001010010001001000000000")
//    parseOperatorPacket("11101110000000001101010000001100100000100011000001100000")
    parseOperatorPacket(decoded)
    println("Version sum $versionSum")
    // TODO:
    // Track the global version sum.
    // Check if each packet is literal or not, recursively.
    // If it's operator, add the version to the sum and check its contents and go into their functions.
    // If it's literal, add the version to the sum.
}
