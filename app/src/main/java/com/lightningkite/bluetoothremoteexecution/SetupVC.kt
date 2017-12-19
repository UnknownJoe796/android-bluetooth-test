package com.lightningkite.bluetoothremoteexecution

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.view.View
import com.lightningkite.kotlin.anko.lifecycle
import com.lightningkite.kotlin.anko.observable.adapter.listAdapter
import com.lightningkite.kotlin.anko.selectableItemBackgroundResource
import com.lightningkite.kotlin.anko.verticalRecyclerView
import com.lightningkite.kotlin.anko.viewcontrollers.AnkoViewController
import com.lightningkite.kotlin.anko.viewcontrollers.VCContext
import com.lightningkite.kotlin.observable.list.ObservableListWrapper
import com.lightningkite.kotlin.observable.property.bind
import org.jetbrains.anko.*

/**
 *
 * Created by joseph on 12/19/17.
 */
class SetupVC(val onComplete: (BluetoothDevice) -> Unit) : AnkoViewController() {

    val devices = ObservableListWrapper<BluetoothDevice>()

    override fun createView(ui: AnkoContext<VCContext>): View = ui.verticalLayout {
        textView {
            padding = dip(8)
            textResource = R.string.select_your_device
        }.lparams(matchParent, wrapContent)

        try {
            devices.replace(BluetoothAdapter.getDefaultAdapter().bondedDevices.toList())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        verticalRecyclerView {
            adapter = listAdapter(devices) { itemObs ->
                textView {
                    padding = dip(8)
                    backgroundResource = selectableItemBackgroundResource
                    lifecycle.bind(itemObs) { text = it.name ?: it.address }
                    setOnClickListener {
                        onComplete.invoke(itemObs.value)
                    }
                }.lparams(matchParent, wrapContent)
            }
        }
    }
}