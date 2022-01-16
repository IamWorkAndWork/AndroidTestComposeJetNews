package com.example.testcomposejetnews.data.interests

import com.example.testcomposejetnews.data.Result
import com.example.testcomposejetnews.model.InterestSection
import com.example.testcomposejetnews.model.TopicSelection
import kotlinx.coroutines.flow.Flow

interface InterestsRepository {

    suspend fun getTopics(): Result<List<InterestSection>>

    suspend fun getPeople(): Result<List<String>>

    suspend fun getPublications(): Result<List<String>>

    suspend fun toggleTopicsSelection(topic: TopicSelection)

    suspend fun togglePersonSelected(person: String)

    suspend fun togglePublicationSelected(publication: String)

    fun observeTopicsSelected(): Flow<Set<TopicSelection>>

    fun observePeopleSelected(): Flow<Set<String>>

    fun observePublicationSelected(): Flow<Set<String>>

}