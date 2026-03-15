package com.sophia.tinycivilizations.empire

import com.sophia.tinycivilizations.world.CivWorld
import kotlin.math.floor
import kotlin.math.pow

class Empire(val name: String, x: Int, y: Int) {

    var economy: Float = 100f
    val capital = x to y
    val territories = mutableSetOf<Pair<Int, Int>>(capital)

    val warTargets = mutableSetOf<Empire>() // TODO
    fun update(world: CivWorld) {

        val n = territories.size
        val bonus =
            if (n < 20)
                250.0f
            else
                250.0f - 0.55f * ((n - 20).toFloat().pow(1.3f))
        val deltaE = floor((2.5f * n + bonus) / 3.0f).toInt() / (warTargets.size + 1)
        economy += deltaE
        economy = economy.coerceAtMost(750f)

        val hasExpanded = world.tryExpand(this)
    }

}
