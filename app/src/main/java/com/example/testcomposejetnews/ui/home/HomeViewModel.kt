package com.example.testcomposejetnews.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.testcomposejetnews.R
import com.example.testcomposejetnews.data.Result
import com.example.testcomposejetnews.data.posts.PostsRepository
import com.example.testcomposejetnews.utils.ErrorMessage
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel(
    private val postsRepository: PostsRepository
) : ViewModel() {

    private val viewModelState = MutableStateFlow(HomeViewModelState(isLoading = true))

    val uiState = viewModelState
        .map {
            it.toUiState()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        refreshPosts()

        viewModelScope.launch {
            postsRepository.observeFavorites().collect { favorites ->
                viewModelState.update {
                    it.copy(favorites = favorites)
                }
            }
        }
    }

    fun refreshPosts() {
        viewModelState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            val result = postsRepository.getPostsFeed()
            viewModelState.update {
                when (result) {
                    is Result.Success -> it.copy(postsFeed = result.data, isLoading = false)
                    is Result.Error -> {
                        val errorMessage = it.errorMessages + ErrorMessage(
                            id = UUID.randomUUID().mostSignificantBits,
                            messageId = R.string.load_error
                        )
                        it.copy(errorMessages = errorMessage, isLoading = false)
                    }
                }
            }
        }

    }

    fun toggleFavourite(postId: String) {
        viewModelScope.launch {
            postsRepository.toggleFavorite(postId = postId)
        }
    }

    fun selectArticle(postId: String) {
        interactedWithArticleDetails(postId)
    }

    fun errorShown(errorId: Long) {
        viewModelState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }

    fun interactedWithFeed(){
        viewModelState.update {
            it.copy(isArticleOpen = false)
        }
    }

    fun interactedWithArticleDetails(postId: String) {
        viewModelState.update {
            it.copy(
                selectedPostId = postId,
                isArticleOpen = true
            )
        }
    }

    fun onSearchInputChanged(searchInput: String) {
        viewModelState.update {
            it.copy(searchInput = searchInput)
        }
    }

    companion object {

        fun provideFactory(
            postsRepository: PostsRepository,
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(postsRepository = postsRepository) as T
                }
            }
        }

    }

}