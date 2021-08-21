package com.afrakhteh.musicplayer.di.builders

abstract class ComponentBuilder<C> {

    private var instance: C? = null

    fun getInstance(): C {
        if (instance == null) {
            instance = provideInstance()
        }
        return requireNotNull(instance)
    }

    abstract fun provideInstance(): C
}

