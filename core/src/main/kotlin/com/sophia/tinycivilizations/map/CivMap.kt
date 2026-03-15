package com.sophia.tinycivilizations.map

class CivMap(
    val tiles: Array<Array<Tile>>,
) {

    val width: Int = tiles.size
    val height: Int = tiles[0].size

    fun isLand(x: Int, y: Int): Boolean {
        return tiles[x][y].terrain == Terrain.LAND
    }

    fun oddqToAxial(col: Int, row: Int): Pair<Int, Int> {
        val q = col
        val r = row - (col - (col and 1)) / 2
        return q to r
    }

    fun axialToOddq(q: Int, r: Int): Pair<Int, Int> {
        val col = q
        val row = r + (q - (q and 1)) / 2
        return Pair(col, row)
    }

    val AXIAL_NEIGHBORS = listOf(
        1 to 0,
        1 to -1,
        0 to -1,
        -1 to 0,
        -1 to 1,
        0 to 1
    )

    fun getNeighbors(col: Int, row: Int): List<Pair<Int, Int>> {
        val (q, r) = oddqToAxial(col, row)

        return AXIAL_NEIGHBORS.map { (dq, dr) ->
            axialToOddq(q + dq, r + dr)
        }
    }
}
