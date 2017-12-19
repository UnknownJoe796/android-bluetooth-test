package com.lightningkite.bluetoothremoteexecution

data class Response(
        var id: Int = 0,
        var result: Any?,
        var error: String?
)