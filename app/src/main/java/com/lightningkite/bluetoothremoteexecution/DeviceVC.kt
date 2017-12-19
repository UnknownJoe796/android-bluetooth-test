package com.lightningkite.bluetoothremoteexecution

import android.bluetooth.BluetoothDevice
import android.view.View
import com.lightningkite.kotlin.anko.lifecycle
import com.lightningkite.kotlin.anko.observable.adapter.listAdapter
import com.lightningkite.kotlin.anko.selectableItemBackgroundResource
import com.lightningkite.kotlin.anko.verticalRecyclerView
import com.lightningkite.kotlin.anko.viewcontrollers.AnkoViewController
import com.lightningkite.kotlin.anko.viewcontrollers.VCContext
import com.lightningkite.kotlin.anko.viewcontrollers.containers.VCStack
import com.lightningkite.kotlin.observable.property.bind
import org.jetbrains.anko.*

/**
 * Created by joseph on 12/19/17.
 */
class DeviceVC(val stack: VCStack, val device: BluetoothDevice) : AnkoViewController() {

    val deviceManager = CommandDeviceManager(device)

    override fun createView(ui: AnkoContext<VCContext>): View = ui.verticalLayout {

        deviceManager.stayOnForLifecycle(lifecycle)

        textView {
            text = resources.getString(R.string.select_a_command)
        }.lparams(matchParent, wrapContent) { margin = dip(8) }

        verticalRecyclerView {
            adapter = listAdapter(deviceManager.methods) { itemObs ->
                textView {
                    padding = dip(8)
                    backgroundResource = selectableItemBackgroundResource
                    lifecycle.bind(itemObs) { text = it.name }
                    setOnClickListener {
                        stack.push(MethodVC(stack, deviceManager, itemObs.value))
                    }
                }.lparams(matchParent, wrapContent)
            }
        }
    }
}

