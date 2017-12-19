package com.lightningkite.bluetoothremoteexecution

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import com.lightningkite.kotlin.anko.async.AndroidAsync
import com.lightningkite.kotlin.async.doUiThread
import com.lightningkite.kotlin.lifecycle.LifecycleConnectable
import com.lightningkite.kotlin.lifecycle.LifecycleListener
import com.lightningkite.kotlin.networking.gsonFrom
import com.lightningkite.kotlin.networking.gsonToString
import com.lightningkite.kotlin.observable.list.ObservableListWrapper
import org.jetbrains.anko.doAsync
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class CommandDeviceManager(val device: BluetoothDevice) {

    val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    var socket: BluetoothSocket? = null
    val methods = ObservableListWrapper<Method>()

    val openCount = AtomicInteger(0)

    fun open() {
        doAsync {
            if (openCount.getAndIncrement() == 0) {
                println("Opening...")
                socket = device.createRfcommSocketToServiceRecord(uuid).apply {
                    connect()
                    println("Connected")
//                    while(true){
//                        try {
//                            println(inputStream.read())
//                        } catch(e:Exception){
//                            e.printStackTrace()
//                            break
//                        }
//                    }
                    val newMethods = inputStream.bufferedReader().readLine().gsonFrom<List<Method>>() ?: listOf()
                    println("Received new methods, $newMethods")
                    doUiThread {
                        methods.replace(newMethods)
                    }
                }
            }
        }

    }

    fun close() {
        doAsync {
            if (openCount.decrementAndGet() == 0) {
                println("Closing...")
                socket?.close()
                socket = null
            }
        }
    }

    fun closeAfterSecond() {
        AndroidAsync.uiHandler.postDelayed({
            close()
        }, 1000L)
    }

    fun stayOnForLifecycle(lifecycleConnectable: LifecycleConnectable) {
        lifecycleConnectable.connect(object : LifecycleListener {
            override fun onStart() {
                open()
            }

            override fun onStop() {
                closeAfterSecond()
            }
        })
    }

    fun BluetoothSocket.request(request: Request): Response {
        println("Sending request, $request")
        outputStream.bufferedWriter().apply {
            appendln(request.gsonToString())
            flush()
        }
        return inputStream.bufferedReader().readLine().gsonFrom<Response>()!!
    }

    fun request(request: Request): Response? = socket?.request(request)
}