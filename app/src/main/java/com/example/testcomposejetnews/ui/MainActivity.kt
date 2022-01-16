package com.example.testcomposejetnews.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.view.WindowCompat
import com.example.testcomposejetnews.JetnewsApplication
import com.example.testcomposejetnews.utils.rememberWindowSizeClass

@ExperimentalFoundationApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val appContainer = (application as JetnewsApplication).container

        setContent {
            val windowSizeClass = rememberWindowSizeClass()
            JetnewsApp(appContainer = appContainer, windowSize = windowSizeClass)
        }
    }
}