package com.sophia.tinycivilizations.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.sophia.tinycivilizations.TinyCivilizations
import com.sophia.tinycivilizations.renderer.CivWorldRenderer
import com.sophia.tinycivilizations.world.factory.CivWorldGenerator
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.app.KtxScreen
import ktx.scene2d.actors
import ktx.scene2d.vis.*
import kotlin.math.round

class WorldGenerationScreen(val civilizations: TinyCivilizations): KtxScreen {

    val generator = CivWorldGenerator()
    val civWorld get() = civilizations.civWorld
    lateinit var civWorldRenderer: CivWorldRenderer

    var isGenerateOnChange = false
    lateinit var widthSlider: Slider
    lateinit var widthLabel: Label
    lateinit var heightSlider: Slider
    lateinit var heightLabel: Label
    lateinit var contRoughSlider: Slider
    lateinit var contRoughLabel: Label
    lateinit var oceanSlider: Slider
    lateinit var oceanLabel: Label
    lateinit var stage: Stage

    private fun generate() {
        generator.continentRoughness = contRoughSlider.value
        generator.oceanWorld = oceanSlider.value

        val width = widthSlider.value.toInt()
        val height = heightSlider.value.toInt()

        val newWorld = generator.generateWorld(width, height)
        civilizations.civWorld = newWorld
        civWorldRenderer.civWorld = newWorld
    }

    override fun show() {
        civilizations.civWorld = generator.generateWorld()
        civWorldRenderer = CivWorldRenderer(civilizations.civWorld, 100f, 60f)
        createUi()
        updateLabels()

        Gdx.input.inputProcessor = stage
    }

    private fun updateLabels() {
        widthLabel.setText(widthSlider.value.toInt().toString())
        heightLabel.setText(heightSlider.value.toInt().toString())
        contRoughLabel.setText("%.1f".format(contRoughSlider.value))
        oceanLabel.setText("%.1f".format(oceanSlider.value))
    }

    private fun createUi() {
        stage = Stage().apply {
            actors {
                visTable {
                    setFillParent(true)
                    visTable {
                        it.grow()
                    }
                    visTable(true) {
                        it.growY().width(Value.percentWidth(0.3f, parent))
                        background = skin.getDrawable("window-bg")
                        this.columnDefaults(2).width(Value.percentWidth(0.4f, this))
                        visCheckBox("Real-time?"){
                            isChecked = isGenerateOnChange
                            onChange {
                                isGenerateOnChange = this.isChecked
                            }
                        }
                        row()
                        visLabel("w: ")
                        widthLabel = visLabel("")
                        widthSlider = visSlider(10f, 80f, 10f) {
                            this.value = civWorld.civMap.width.toFloat()
                            onChange {
                                widthLabel.setText((round(this.value*100)/100f).toInt().toString())
                                if (isGenerateOnChange) generate()
                            }
                        }
                        row()
                        visLabel("h: ")
                        heightLabel = visLabel("")
                        heightSlider = visSlider(10f, 80f, 10f) {
                            this.value = civWorld.civMap.height.toFloat()
                            onChange {
                                heightLabel.setText((round(this.value*100f)/100f).toInt().toString())
                                if (isGenerateOnChange) generate()
                            }
                        }
                        row()
                        visLabel("cont. rough: ")
                        contRoughLabel = visLabel("")
                        contRoughSlider = visSlider(0.1f, 1f, 0.1f) {
                            this.value = generator.continentRoughness
                            onChange {
                                contRoughLabel.setText((round(this.value*100f)/100f).toString())
                                if (isGenerateOnChange) generate()
                            }
                        }
                        row()
                        visLabel("ocean: ")
                        oceanLabel = visLabel("?")
                        oceanSlider = visSlider(0.1f, 1f, 0.1f) {
                            this.value = generator.oceanWorld
                            onChange {
                                oceanLabel.setText((round(this.value*100f)/100f).toString())
                                if (isGenerateOnChange) generate()
                            }
                        }
                        row()
                        visTextButton("Generate") {
                            onClick {
                                generate()
                            }
                        }
                        row()
                        visTextButton("Start") {
                            onClick {
                                civilizations.startNewGame()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun hide() {
        Gdx.input.inputProcessor = null
    }

    override fun render(delta: Float) {
        civWorldRenderer.render()

        stage.viewport.apply()
        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        civWorldRenderer.onResize(width, height)
        stage.viewport.update(width, height)
    }

}
