package com.lightningkite.bluetoothremoteexecution

data class Method(
        var name: String = "",
        var description: String = "",
        var arguments: List<Method.Argument> = listOf()
) {
    data class Argument(
            var name: String = "",
            var description: String = ""
    )
}