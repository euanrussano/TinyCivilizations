package com.sophia.tinycivilizations.empire

import com.sophia.tinycivilizations.world.CivWorld
import kotlin.math.floor
import kotlin.math.pow
import kotlin.random.Random

class Empire(val name: String, x: Int, y: Int) {

    var age: Int = 0
        private set
    var economy: Float = 100f
    val capital = x to y
    private val _territories = mutableSetOf<Pair<Int, Int>>(capital)
    val territories: Set<Pair<Int, Int>> get() = _territories

    private val _borderEmpires = mutableSetOf<Empire>()
    val borderEmpires: Set<Empire> get() = _borderEmpires

    val warTargets = mutableSetOf<Empire>() // TODO
    fun update(world: CivWorld) {
        age += 1
        val n = territories.size
        val bonus =
            if (n < 20)
                250.0f
            else
                250.0f - 0.55f * ((n - 20).toFloat().pow(1.3f))
        val deltaE = (floor((2.5f * n + bonus) / 3.0f).toInt() / (warTargets.size + 1)).coerceAtMost(750)
        economy += deltaE


        val hasExpanded = world.tryExpand(this)
        if (!hasExpanded) {
            tryDeclareWar()
        }
        if (warTargets.isNotEmpty()){
            val target = warTargets.random()
            world.resolveBattle(this, target)
        }

    }

    private fun tryDeclareWar() {
        println("Empire $name age: $age")
        println("Empire $name economy: $economy")
        if (age < 30) return
        if (economy < 1000) return
        for (otherEmpire in borderEmpires) {
            if (Random.nextInt(20*territories.size) != 0) continue
            if (otherEmpire in warTargets) continue
            warTargets.add(otherEmpire)
            println("Empire $name declared war on ${otherEmpire.name}")
            return
        }
    }

    fun addTerritory(territory: Pair<Int, Int>) {
        _territories.add(territory)

        println("Empire $name gained territory: $territory")
    }

    fun addBorderEmpire(empire: Empire) {
        if (empire == this) error("Empire $name should not add itself as a border empire")

        _borderEmpires.add(empire)
        println("Empire $name added border empire: ${empire.name}")
    }

    fun removeTerritory(pair: Pair<Int, Int>) {
        _territories.remove(pair)

        println("Empire $name lost territory: $pair")
    }

}
