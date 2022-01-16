package com.example.testcomposejetnews.ui.components

import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun JetnewsSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    snackBar: @Composable (SnackbarData) -> Unit = {
        Snackbar(it)
    }
) {

    SnackbarHost(
        hostState = hostState, modifier = modifier
            .systemBarsPadding()
            .wrapContentWidth(align = Alignment.Start)
            .widthIn(max = 550.dp),
        snackbar = snackBar
    )

}