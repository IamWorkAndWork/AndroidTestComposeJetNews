package com.example.testcomposejetnews.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.ExperimentalComposeUiApi
import com.example.testcomposejetnews.ui.article.ArticleScreen

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel,
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val uiState by homeViewModel.uiState.collectAsState()

    HomeRoute(
        uiState = uiState,
        isExpandedScreen = isExpandedScreen,
        onToggleFavorite = { homeViewModel.toggleFavourite(it) },
        onSelectPost = { homeViewModel.selectArticle(it) },
        onRefreshPosts = { homeViewModel.refreshPosts() },
        onErrorDismiss = { homeViewModel.errorShown(it) },
        onInteractWithFeed = { homeViewModel.interactedWithFeed() },
        onInteractWithArticleDetails = { homeViewModel.interactedWithArticleDetails(it) },
        onSearchInputChanged = { homeViewModel.onSearchInputChanged(it) },
        openDrawer = openDrawer,
        scaffoldState = scaffoldState,
    )

}

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun HomeRoute(
    uiState: HomeUiState,
    isExpandedScreen: Boolean,
    onToggleFavorite: (String) -> Unit,
    onSelectPost: (String) -> Unit,
    onRefreshPosts: () -> Unit,
    onErrorDismiss: (Long) -> Unit,
    onInteractWithFeed: () -> Unit,
    onInteractWithArticleDetails: (String) -> Unit,
    onSearchInputChanged: (String) -> Unit,
    openDrawer: () -> Unit,
    scaffoldState: ScaffoldState
) {

    val homeListLazyListState = rememberLazyListState()
    val articleDetailLazyListStates = when (uiState) {
        is HomeUiState.HasPosts -> {
            uiState.postsFeed.allPosts
        }
        is HomeUiState.NoPost -> {
            emptyList()
        }
    }.associate { post ->
        key(post.id) {
            post.id to rememberLazyListState()
        }
    }

    val homeScreenType = getHomeScreenType(isExpandedScreen, uiState)

    when (homeScreenType) {
        HomeScreenType.FeedWithArticleDetails -> {
            HomeFeedWithArticleDetailsScreen(
                uiState = uiState,
                showTopAppBar = !isExpandedScreen,
                onToggleFavorite = onToggleFavorite,
                onSelectPost = onSelectPost,
                onRefreshPosts = onRefreshPosts,
                onErrorDismiss = onErrorDismiss,
                onInteractWithList = onInteractWithFeed,
                onInteractWithDetail = onInteractWithArticleDetails,
                openDrawer = openDrawer,
                homeListLazyListState = homeListLazyListState,
                articleDetailLazyListStates = articleDetailLazyListStates,
                scaffoldState = scaffoldState,
                onSearchInputChanged = onSearchInputChanged,
            )
        }
        HomeScreenType.Feed -> {
            HomeFeedScreen(
                uiState = uiState,
                showTopAppBar = !isExpandedScreen,
                onToggleFavorite = onToggleFavorite,
                onSelectPost = onSelectPost,
                onRefreshPosts = onRefreshPosts,
                onErrorDismiss = onErrorDismiss,
                openDrawer = openDrawer,
                homeListLazyListState = homeListLazyListState,
                scaffoldState = scaffoldState,
                onSearchInputChanged = onSearchInputChanged,
            )
        }
        HomeScreenType.ArticleDetails -> {
            check(uiState is HomeUiState.HasPosts)

            ArticleScreen(
                post = uiState.selectedPost,
                isExpandedScreen = isExpandedScreen,
                onBack = onInteractWithFeed,
                isFavorite = uiState.favorites.contains(uiState.selectedPost.id),
                onToggleFavorite = {
                    onToggleFavorite(uiState.selectedPost.id)
                },
                lazyListState = articleDetailLazyListStates.getValue(
                    uiState.selectedPost.id
                )
            )

            BackHandler() {
                onInteractWithFeed()
            }
        }
    }
}

@Composable
private fun getHomeScreenType(isExpandedScreen: Boolean, uiState: HomeUiState): HomeScreenType {
    return when (isExpandedScreen) {
        false -> {
            when (uiState) {
                is HomeUiState.HasPosts -> {
                    if (uiState.isArticleOpen) {
                        HomeScreenType.ArticleDetails
                    } else {
                        HomeScreenType.Feed
                    }
                }
                is HomeUiState.NoPost -> {
                    HomeScreenType.Feed
                }
            }
        }
        true -> {
            HomeScreenType.FeedWithArticleDetails
        }
    }
}
