package com.sevenexp.craftit

import android.app.Application

object Locator {
    private var application: Application? = null

    private inline val requireApplication
        get() = application
            ?: error("You forgot to call Locator.initWith(application) in your Application class")

    fun initWith(application: Application) {
        this.application = application
    }
}