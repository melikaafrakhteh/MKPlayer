package com.afrakhteh.musicplayer.views.interfaces

interface PermissionController {

    fun requestPermission()
    fun setOnPermissionRequestCallBack(callBack: (Boolean) -> Unit)

}