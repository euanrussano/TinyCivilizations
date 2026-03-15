package com.sophia.tinycivilizations.world.factory

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.noise.NoiseGenerator
import com.sophia.tinycivilizations.map.CivMap
import com.sophia.tinycivilizations.map.Terrain
import com.sophia.tinycivilizations.map.Tile
import com.sophia.tinycivilizations.world.CivWorld

class CivWorldGenerator {

    var continentRoughness = 0.9f
    var oceanWorld = 0.4f
    val seed = 999

    fun generateWorld(width: Int = 64, height:Int = 32): CivWorld {
        val grid = Grid(width, height)
        val noiseGenerator = NoiseGenerator()
        noiseGenerator.radius = 10
        noiseGenerator.modifier = continentRoughness
        noiseGenerator.seed = seed
        noiseGenerator.generate(grid)

        println("World generated")
        println("min: ${grid.array.min()}")
        println("avg: ${grid.array.sum()/grid.array.size}")
        println("max: ${grid.array.max()}")

        val tiles = Array(width) { x -> Array(height) { y -> Tile(x, y, terrain = Terrain.SEA) } }
        for (x in 0 until grid.getWidth()) {
            for (y in 0 until grid.getHeight()) {
                val cell = grid.get(x, y)
                if (cell > oceanWorld) {
                    tiles[x][y] = Tile(x, y, terrain = Terrain.LAND)
                }
            }
        }


        return CivWorld(CivMap(tiles))
    }
}
