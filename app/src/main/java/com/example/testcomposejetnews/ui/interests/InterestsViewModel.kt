package com.example.testcomposejetnews.ui.interests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.testcomposejetnews.data.interests.InterestsRepository
import com.example.testcomposejetnews.data.successOr
import com.example.testcomposejetnews.model.TopicSelection
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class InterestsViewModel(
    private val interestsRepository: InterestsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        InterestsUiState(loading = true)
    )
    val uiState: StateFlow<InterestsUiState> = _uiState.asStateFlow()

    val selectedTopics = interestsRepository.observeTopicsSelected().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptySet()
    )

    val selectedPeople = interestsRepository.observePeopleSelected().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptySet()
    )

    val selectedPublications = interestsRepository.observePublicationSelected().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptySet()
    )

    init {
        refreshAll()
    }

    fun toggleTopicSelection(topic: TopicSelection) {
        viewModelScope.launch {
            interestsRepository.toggleTopicsSelection(topic)
        }
    }

    fun togglePersonSelected(person: String) {
        viewModelScope.launch {
            interestsRepository.togglePersonSelected(person)
        }
    }

    fun togglePublicationSelected(publication: String) {
        viewModelScope.launch {
            interestsRepository.togglePublicationSelected(publication)
        }
    }

    private fun refreshAll() {
        _uiState.update {
            it.copy(loading = true)
        }

        viewModelScope.launch {
            val topicDefered = async { interestsRepository.getTopics() }
            val peopleDeferred = async { interestsRepository.getPeople() }
            val publicationDeferred = async { interestsRepository.getPublications() }

            val topics = topicDefered.await().successOr(emptyList())
            val people = peopleDeferred.await().successOr(emptyList())
            val publications = publicationDeferred.await().successOr(emptyList())

            _uiState.update {
                it.copy(
                    loading = false,
                    topics = topics,
                    people = people,
                    publications = publications
                )
            }
        }
    }

    companion object {
        fun provideFactory(
            interestsRepository: InterestsRepository
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return InterestsViewModel(interestsRepository) as T
                }

            }
        }
    }

}