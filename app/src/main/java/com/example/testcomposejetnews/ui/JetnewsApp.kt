package com.example.testcomposejetnews.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.testcomposejetnews.data.AppContainer
import com.example.testcomposejetnews.ui.components.AppNavRail
import com.example.testcomposejetnews.ui.theme.TestComposeJetNewsTheme
import com.example.testcomposejetnews.utils.WindowSize
import com.google.accompanist.insets.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterialApi
@Composable
fun JetnewsApp(
    appContainer: AppContainer,
    windowSize: WindowSize
) {
    TestComposeJetNewsTheme() {
        ProvideWindowInsets {
            val systemUiController = rememberSystemUiController()
            val darkIcons = MaterialTheme.colors.isLight
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = darkIcons)
            }

            val navController = rememberNavController()
            val navigationActions = remember(navController) {
                JetnewsNavigationActions(navController)
            }

            val coroutineScope = rememberCoroutineScope()

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute =
                navBackStackEntry?.destination?.route ?: JetnewsDestinations.HOME_ROUTE

            val isExpandedScreen = windowSize == WindowSize.Expanded
            val sizeAwareDrawerState = rememberSizeAwareDrawerState(isExpandedScreen)

            ModalDrawer(
                drawerContent = {
                    AppDrawer(
                        currentRoute = currentRoute,
                        navigateToHome = navigationActions.navigationToHome,
                        navigateToInterests = navigationActions.navigateToInterests,
                        closeDrawer = {
                            coroutineScope.launch {
                                sizeAwareDrawerState.close()
                            }
                        },
                        modifier = Modifier
                            .statusBarsPadding()
                            .navigationBarsPadding()
                    )
                },
                drawerState = sizeAwareDrawerState,
                gesturesEnabled = !isExpandedScreen
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding(bottom = false)
                ) {
                    if (isExpandedScreen) {
                        AppNavRail(
                            currentRoute = currentRoute,
                            navigateToHome = navigationActions.navigationToHome,
                            navigateToInterests = navigationActions.navigateToInterests
                        )
                    }
                    JetnewsNavGraph(
                        appContainer = appContainer,
                        isExpandedScreen = isExpandedScreen,
                        navController = navController,
                        openDrawer = {
                            coroutineScope.launch {
                                sizeAwareDrawerState.open()
                            }
                        }
                    )
                }

            }

        }
    }
}

@Composable
private fun rememberSizeAwareDrawerState(isExpandedScreen: Boolean): DrawerState {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    return if (!isExpandedScreen) {
        drawerState
    } else {
        DrawerState(DrawerValue.Closed)
    }
}

@Composable
fun rememberContentPaddingForScreen(additionalTop: Dp = 0.dp): PaddingValues {
    return rememberInsetsPaddingValues(
        insets = LocalWindowInsets.current.systemBars,
        applyTop = false,
        applyEnd = false,
        applyStart = false,
        additionalTop = additionalTop
    )
}
