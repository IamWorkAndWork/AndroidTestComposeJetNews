package com.example.testcomposejetnews.data.posts

import com.example.testcomposejetnews.data.Result
import com.example.testcomposejetnews.model.Post
import com.example.testcomposejetnews.model.PostsFeed
import kotlinx.coroutines.flow.Flow

interface PostsRepository {

    suspend fun getPost(postId: String?): Result<Post>

    suspend fun getPostsFeed(): Result<PostsFeed>

    fun observeFavorites(): Flow<Set<String>>

    suspend fun toggleFavorite(postId: String)

}