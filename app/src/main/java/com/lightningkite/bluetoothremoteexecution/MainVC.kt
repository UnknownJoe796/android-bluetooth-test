package com.lightningkite.bluetoothremoteexecution

import android.view.View
import com.lightningkite.kotlin.anko.viewcontrollers.AnkoViewController
import com.lightningkite.kotlin.anko.viewcontrollers.VCContext
import com.lightningkite.kotlin.anko.viewcontrollers.containers.VCStack
import org.jetbrains.anko.AnkoContext

class MainVC() : AnkoViewController() {
    val stack = VCStack().apply {

    }

    override fun createView(ui: AnkoContext<VCContext>): View = ui.viewContainer(ui.owner, stack)
}