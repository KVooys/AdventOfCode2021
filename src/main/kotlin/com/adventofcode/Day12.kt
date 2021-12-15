package com.adventofcode

var totalRoutes = 0

fun parseRoutes(input: List<String>): MutableMap<String, MutableList<String>> {
    // Map that tracks which points can go to which point.
    val graphMap: MutableMap<String, MutableList<String>> = mutableMapOf()

    for (line in input) {
        val parts = line.split("-")
        val from = parts[0]
        val to = parts[1]
        // Logic works both ways, add from to to's and add to's to froms.
        if (!graphMap.contains(from)) {
            graphMap[from] = mutableListOf()
        }
        if (!graphMap.contains(to)) {
            graphMap[to] = mutableListOf()
        }
        graphMap[from]?.add(to)
        graphMap[to]?.add(from)

    }
    println(graphMap)
    return graphMap
}

fun getPathsFromRoutes(routes: MutableMap<String, MutableList<String>>): MutableList<List<String>> {
    var paths = mutableListOf<List<String>>()

    // Start from start node, get all its possible neighbours, then find all possible routes from those nodes.
    // Keep doing this until end nodes are reached, only adding the found path if it's unique.

    // Part 1
//    travelToNextNode("start", listOf("start"), routes)

    // Part 2
    travelToNextNodeButAllowOneSmallCaveTwice("start", listOf("start"), routes, false)

    return paths
}


// Part 1
fun travelToNextNode(node: String, path: List<String>, routes: MutableMap<String, MutableList<String>>) {
    if (node == "end") {
        totalRoutes += 1
        return
    }
    for (nextNode in routes[node]!!) {
        // Only visit a node if it's uppercase, or an unvisited lowercase node.
        if (nextNode.uppercase() == nextNode || !path.contains(nextNode)) {
            println("Going to $nextNode from $path")
            travelToNextNode(nextNode, path.plus(nextNode), routes)
        }
    }
}

// Part 2 monstrosity
fun travelToNextNodeButAllowOneSmallCaveTwice(node: String, path: List<String>, routes: MutableMap<String, MutableList<String>>, visitedSmallCaveTwice: Boolean) {
    if (node == "end") {
        println(path)
        totalRoutes += 1
        return
    }
    for (nextNode in routes[node]!!) {
        // Only visit a node if it's uppercase, or an unvisited lowercase node
        if (nextNode.uppercase() == nextNode || !path.contains(nextNode)) {
            travelToNextNodeButAllowOneSmallCaveTwice(nextNode, path.plus(nextNode), routes, visitedSmallCaveTwice)
        }
        // or it's a small cave we've visited, but we haven't visited any small cave twice yet, and it's neither start or end.
        else {
            if (nextNode.lowercase() == nextNode && path.contains(nextNode) && !visitedSmallCaveTwice
                && !nextNode.equals("start") && !nextNode.equals("end")) {
                travelToNextNodeButAllowOneSmallCaveTwice(nextNode, path.plus(nextNode), routes, true)
            }
        }
    }
}

fun day12Results() {
    var smallTestInput = readInput("day12_small_test")
    var testInput = readInput("day12_test")
    var input = readInput("day12")

    var routes = parseRoutes(input)
    getPathsFromRoutes(routes)
    println(totalRoutes)
}



