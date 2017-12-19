package com.lightningkite.bluetoothremoteexecution

import com.lightningkite.kotlin.anko.viewcontrollers.ViewController
import com.lightningkite.kotlin.anko.viewcontrollers.implementations.VCActivity

class MainActivity : VCActivity() {
    companion object {
        val mainVC = MainVC()
    }

    override val viewController: ViewController get() = mainVC
}

