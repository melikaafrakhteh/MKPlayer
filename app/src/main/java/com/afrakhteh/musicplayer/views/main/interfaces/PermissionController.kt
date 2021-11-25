package com.afrakhteh.musicplayer.views.main.interfaces

interface PermissionController {

    fun requestPermission()
    fun setOnPermissionRequestCallBack(callBack: (Boolean) -> Unit)
    fun hasPermission(): Boolean

}