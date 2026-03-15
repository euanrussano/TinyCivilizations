package com.sophia.tinycivilizations.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.sophia.tinycivilizations.TinyCivilizations
import com.sophia.tinycivilizations.renderer.CivWorldRenderer
import com.sophia.tinycivilizations.world.factory.CivWorldGenerator
import ktx.app.KtxScreen

class MainGameScreen(val civilizations: TinyCivilizations): KtxScreen {

    val civWorld get() = civilizations.civWorld
    lateinit var civWorldRenderer: CivWorldRenderer
    var runStepByStep = true
    var runStep = false

    override fun show() {
        civWorldRenderer = CivWorldRenderer(civilizations.civWorld)
    }

    override fun render(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)){
            runStepByStep = !runStepByStep
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)){
            runStep = true
        }

        if (runStepByStep){
            if (runStep){
                civWorld.update(delta)
                runStep = false
            }
        } else {
            civWorld.update(delta)
        }

        civWorldRenderer.render()
    }

    override fun resize(width: Int, height: Int) {
        civWorldRenderer.onResize(width, height)
    }

}
