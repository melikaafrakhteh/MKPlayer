package com.afrakhteh.musicplayer.dataSources

interface Readable<T> {
    fun read(): T

    interface IO<I, T> {
        fun read(input: I): T
    }

    interface Suspendable<T> {
        suspend fun read(): T

        interface IO<I, T> {
            fun read(input: I): T
        }
    }

    interface Void {
        fun read()

        interface IO<I, T> {
            fun read(input: I): T
        }

        interface Suspendable<T> {
            suspend fun read(): T

            interface IO<I> {
                suspend fun read(input: I)
            }
        }
    }
}