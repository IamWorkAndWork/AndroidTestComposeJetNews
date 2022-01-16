package com.example.testcomposejetnews.ui.interests

import com.example.testcomposejetnews.model.InterestSection

data class InterestsUiState(
    val topics: List<InterestSection> = emptyList(),
    val people: List<String> = emptyList(),
    val publications: List<String> = emptyList(),
    val loading: Boolean = false
)