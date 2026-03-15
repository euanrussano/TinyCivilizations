@file:JvmName("HeadlessLauncher")

package com.sophia.tinycivilizations.headless

import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration
import com.sophia.tinycivilizations.TinyCivilizations

/** Launches the headless application. Can be converted into a server application or a scripting utility. */
fun main() {
    HeadlessApplication(TinyCivilizations(), HeadlessApplicationConfiguration().apply {
        // When this value is negative, TinyCivilizations#render() is never called:
        updatesPerSecond = -1
    })
}
