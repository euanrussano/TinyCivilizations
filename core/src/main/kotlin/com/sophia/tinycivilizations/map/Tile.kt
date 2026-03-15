package com.sophia.tinycivilizations.map

import com.sophia.tinycivilizations.empire.Empire

class Tile(
    val x: Int,
    val y: Int,
    val terrain: Terrain
){

    val occupier: Empire? = null
    var owner: Empire? = null


}
