package com.lightningkite.bluetoothremoteexecution

data class Request(
        var id: Int = 0,
        var method: String = "",
        var params: Map<String, Any?>? = null
)