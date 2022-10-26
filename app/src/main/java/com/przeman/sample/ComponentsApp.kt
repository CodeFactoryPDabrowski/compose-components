package com.przeman.sample

import android.app.Application
import com.przeman.sample.di.AppComponent

class ComponentsApp : Application() {

    private val appComponent by lazy { AppComponent.create(this) }

    fun appComponent() = appComponent
}
