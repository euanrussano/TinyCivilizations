package com.sophia.tinycivilizations.renderer

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.sophia.tinycivilizations.map.Terrain
import com.sophia.tinycivilizations.map.Tile
import com.sophia.tinycivilizations.world.CivWorld
import ktx.graphics.center
import space.earlygrey.shapedrawer.ShapeDrawer
import space.earlygrey.shapedrawer.ShapeUtils.SQRT3
import kotlin.random.Random

class CivWorldRenderer(
    var civWorld: CivWorld,
    val viewportWidth: Float = 50f,
    val viewportHeight: Float = 30f,
) {
    val viewport = ExtendViewport(viewportWidth, viewportHeight).apply {
        camera.center(viewportWidth, viewportHeight, -1f, 2f)
        camera.update()
    }
    val batch = SpriteBatch()
    val drawer = ShapeDrawer(batch).apply {
        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.WHITE)
        pixmap.fill()
        setTextureRegion(TextureRegion(Texture(pixmap)))
        pixmap.dispose()
    }

    val empireColors = mutableListOf<Color>()

    init {
        for (empire in civWorld.empires) {
            val color = Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f)
            empireColors.add(color)
        }
    }

    fun render() {
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        drawMap()
        drawEmpires()
        batch.end()

    }

    private fun drawEmpires() {
        if (empireColors.size < civWorld.empires.size) {
            while(empireColors.size < civWorld.empires.size){
                val color = Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f)
                empireColors.add(color)
            }
        }
        for ((idx, empire) in civWorld.empires.withIndex()) {
            drawer.setColor(empireColors[idx])
            for (pair in empire.territories) {
                val (cx, cy) = hexToPixel(pair.first, pair.second)
                drawer.filledPolygon(cx, cy, 6, 0.5f)
            }

        }
    }

    private fun drawMap() {
        val tiles = civWorld.civMap.tiles
        for (col in tiles.indices){
            for (row in tiles[0].indices){
                val tile = tiles[col][row]
                drawer.setColor(Color.BLUE)
                if (tile.terrain == Terrain.LAND) {
                    drawer.setColor(Color.GREEN)
                }
                val (cx, cy) = hexToPixel(col, row)
                drawer.filledPolygon(cx, cy, 6, 0.5f)

                drawer.setColor(Color.BLACK)
                drawer.polygon(cx, cy, 6, 0.5f, 0.5f, 0.1f, 0.05f)
            }
        }
    }

    private fun hexToPixel(col: Int, row: Int): Pair<Float, Float> {
        val size = 0.5f
        val x = col * (1.5f * size)
        val term2 = if (col % 2 == 1) SQRT3/2 * size else 0f
        val y = row * (SQRT3 * size) + term2
        return x to y
    }

    fun onResize(width: Int, height: Int) {
        viewport.update(width, height)
    }

}
