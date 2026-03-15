package com.sophia.tinycivilizations.world

import com.sophia.tinycivilizations.empire.Empire
import com.sophia.tinycivilizations.map.CivMap
import com.sophia.tinycivilizations.map.Tile
import kotlin.math.log
import kotlin.random.Random

class CivWorld(
    val civMap: CivMap
) {
    val empires = mutableListOf<Empire>()

    val random = Random(999)
    fun spawnEmpire() {
        val nEmpire = empires.size

        val width = civMap.width
        val height = civMap.height
        while (true){
            val x = random.nextInt(width)
            val y = random.nextInt(height)
            if (civMap.isLand(x, y)) {
                val empire = Empire("Empire $nEmpire", x, y)
                empires.add(empire)
                annexTile(empire, civMap.tiles[x][y])
                break
            }
        }
    }

    fun update(delta: Float) {
        for (empire in empires) {
            empire.update(this)
        }
    }

    fun tryExpand(empire: Empire): Boolean {
        val tiles = empire.territories
        for ((x, y) in tiles) {
            val neighbors = civMap.getNeighbors(x, y)
            for ((x, y) in neighbors) {
                val tile = civMap.tiles.getOrNull(x)?.getOrNull(y)?: continue
                if (!civMap.isLand(x, y)) continue
                if (random.nextInt(10) >= log(empire.economy, 10f)-2) continue
                if (tile.owner != null) continue
                if (tile.occupier != null) continue

                annexTile(empire, tile)
                return true
            }
        }
        return false
    }

    private fun annexTile(empire: Empire, tile: Tile) {
        tile.owner = empire
        empire.territories.add(tile.x to tile.y)
    }

}
