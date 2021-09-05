package com.afrakhteh.musicplayer.views.mainActivity.interfaces

interface PermissionController {

    fun requestPermission()
    fun setOnPermissionRequestCallBack(callBack: (Boolean) -> Unit)
    fun hasPermission(): Boolean

}