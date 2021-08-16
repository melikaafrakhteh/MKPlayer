package com.afrakhteh.musicplayer.util

class SingleEvent<T>(private val value: T) {

    private var isHandled = false

    fun ifNotHandled(block: (input: T) -> Unit) {
        if (isHandled.not()) {
            isHandled = true
            block.invoke(value)
        }
    }
}