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
        if (tile.owner != null){
            tile.owner!!.removeTerritory(tile.x to tile.y)
            updateBorderEmpires(tile.owner!!)
        }
        tile.owner = empire
        empire.addTerritory(tile.x to tile.y)
        updateBorderEmpires(empire)

    }

    private fun updateBorderEmpires(empire: Empire) {
        for (territory in empire.territories) {
            for (pair in civMap.getNeighbors(territory.first, territory.second)) {
                val tile = civMap.tiles.getOrNull(pair.first)?.getOrNull(pair.second)?: return
                if (tile.owner != null && tile.owner != empire){
                    empire.addBorderEmpire(tile.owner!!)
                }
                if (tile.occupier != null && tile.occupier != empire){
                    empire.addBorderEmpire(tile.occupier!!)
                }

            }
        }

    }

    fun resolveBattle(empire: Empire, target: Empire) {
        if (empire == target) error("Empire cannot declare war on itself")

        println("Battle between ${empire.name} and ${target.name}")
        println("Empire ${empire.name} economy: ${empire.economy}")
        println("Empire ${target.name} economy: ${target.economy}")
        val p_fair = 0.5f
        var p_final = p_fair
        if (empire.economy > target.economy*2) {
            p_final = 0.7f
        } else if (target.economy > empire.economy*2) {
            p_final = 0.3f
        }

        var winner = target
        var loser = empire
        val p = random.nextFloat()
        if (p < p_final) {
            winner = empire
            loser = target
        }
        println("Winner: ${winner.name}")
        println("Loser: ${loser.name}")

        // exchange one tile
        for (territory in loser.territories){
            for (pair in civMap.getNeighbors(territory.first, territory.second)) {
                if (pair in winner.territories){
                    annexTile(winner, civMap.tiles[territory.first][territory.second])
                    println("Empire ${winner.name} annexed ${territory.first} ${territory.second}")
                    return
                }
            }
        }

    }

}
