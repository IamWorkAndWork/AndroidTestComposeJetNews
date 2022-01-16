package com.example.testcomposejetnews

import android.app.Application
import com.example.testcomposejetnews.data.AppContainer
import com.example.testcomposejetnews.data.AppContainerImpl

class JetnewsApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        container = AppContainerImpl(appContext = this)
    }

}