package com.example.testcomposejetnews.data

import android.content.Context
import com.example.testcomposejetnews.data.interests.FakeInterestsRepository
import com.example.testcomposejetnews.data.interests.InterestsRepository
import com.example.testcomposejetnews.data.posts.FakePostsRepository
import com.example.testcomposejetnews.data.posts.PostsRepository


interface AppContainer {
    val postRepository: PostsRepository
    val interestsRepository: InterestsRepository
}

class AppContainerImpl(
    private val appContext: Context
) : AppContainer {

    override val postRepository: PostsRepository by lazy {
        FakePostsRepository()
    }

    override val interestsRepository: InterestsRepository by lazy {
        FakeInterestsRepository()
    }

}