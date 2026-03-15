package com.sophia.tinycivilizations

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.kotcrab.vis.ui.VisUI
import com.sophia.tinycivilizations.screen.MainGameScreen
import com.sophia.tinycivilizations.screen.WorldGenerationScreen
import com.sophia.tinycivilizations.world.CivWorld
import com.sophia.tinycivilizations.world.factory.CivWorldGenerator
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.async.KtxAsync
import ktx.graphics.use
import ktx.scene2d.Scene2DSkin
import kotlin.random.Random

class TinyCivilizations : KtxGame<KtxScreen>() {
    lateinit var civWorld: CivWorld
    val initialEmpires = 5

    override fun create() {
        KtxAsync.initiate()
        VisUI.load()
        Scene2DSkin.defaultSkin = VisUI.getSkin()

        addScreen(WorldGenerationScreen(this))
        addScreen(MainGameScreen(this))
        setScreen<WorldGenerationScreen>()
    }

    fun startNewGame() {
        repeat(initialEmpires) {
            civWorld.spawnEmpire()
        }
        setScreen<MainGameScreen>()
    }
}

