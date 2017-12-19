package com.lightningkite.bluetoothremoteexecution

import android.view.View
import com.lightningkite.kotlin.anko.FullInputType
import com.lightningkite.kotlin.anko.lifecycle
import com.lightningkite.kotlin.anko.observable.TextWatcherAdapter
import com.lightningkite.kotlin.anko.textInputEditText
import com.lightningkite.kotlin.anko.viewcontrollers.AnkoViewController
import com.lightningkite.kotlin.anko.viewcontrollers.VCContext
import com.lightningkite.kotlin.anko.viewcontrollers.containers.VCStack
import com.lightningkite.kotlin.async.doUiThread
import com.lightningkite.kotlin.networking.gsonFrom
import com.lightningkite.kotlin.observable.property.StandardObservableProperty
import com.lightningkite.kotlin.observable.property.bind
import org.jetbrains.anko.*
import org.jetbrains.anko.design.textInputLayout

class MethodVC(
        val stack: VCStack,
        val deviceManager: CommandDeviceManager,
        val method: Method
) : AnkoViewController() {

    val inputs = HashMap<String, String>()
    val responseObs = StandardObservableProperty<Any?>(null)

    override fun createView(ui: AnkoContext<VCContext>): View = ui.scrollView {
        deviceManager.stayOnForLifecycle(lifecycle)
        verticalLayout {

            textView {
                text = method.name
            }.lparams(matchParent, wrapContent) { margin = dip(8) }

            textView {
                text = method.description
            }.lparams(matchParent, wrapContent) { margin = dip(8) }

            textView {
                text = resources.getString(R.string.input_instructions)
            }.lparams(matchParent, wrapContent) { margin = dip(8) }

            for (argument in method.arguments) {
                textInputLayout {
                    hint = argument.name
                    textInputEditText {
                        inputType = FullInputType.CODE
                        addTextChangedListener(object : TextWatcherAdapter() {
                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                inputs[argument.name] = s?.toString() ?: ""
                            }
                        })
                    }
                }.lparams(matchParent, wrapContent) { margin = dip(8) }
            }

            button {
                textResource = R.string.submit
                setOnClickListener {
                    val request = Request(
                            id = 0,
                            method = method.name,
                            params = inputs.mapValues { it.value.gsonFrom<Any>() }
                    )
                    doAsync {
                        val response = deviceManager.request(request)
                        doUiThread {
                            responseObs.value = response?.result
                        }
                    }
                }
            }.lparams(matchParent, wrapContent) { margin = dip(8) }

            textView {
                lifecycle.bind(responseObs) {
                    text = resources.getString(R.string.response_x, it.toString())
                }
            }.lparams(matchParent, wrapContent) { margin = dip(8) }
        }
    }
}