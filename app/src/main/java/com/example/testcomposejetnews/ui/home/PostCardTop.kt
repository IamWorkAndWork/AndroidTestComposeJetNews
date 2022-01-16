package com.example.testcomposejetnews.ui.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testcomposejetnews.model.Post
import com.example.testcomposejetnews.R
import com.example.testcomposejetnews.data.posts.posts
import com.example.testcomposejetnews.ui.theme.TestComposeJetNewsTheme

@Composable
fun PostCardTop(
    post: Post,
    modifier: Modifier = Modifier
) {

    val typography = MaterialTheme.typography

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val imageModifier = Modifier
            .heightIn(min = 180.dp)
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium)

        Image(
            painter = painterResource(post.imageId),
            contentDescription = null,
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.heightIn(16.dp))

        Text(
            text = post.title,
            style = typography.h6,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = post.metadata.author.name,
            style = typography.subtitle2,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = stringResource(
                    id = R.string.home_post_min_read,
                    formatArgs = arrayOf(
                        post.metadata.date,
                        post.metadata.readTimeMinutes
                    )
                ),
                style = typography.subtitle2
            )
        }
    }

}

@Preview("Default Colors")
@Composable
fun TutorialPreview() {
    TutorialPreviewTemplate()
}

@Preview("Dark Theme", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun TutorialPreviewDark() {
    TutorialPreviewTemplate()
}

@Preview("Font Scaling 1.5", fontScale = 1.5f)
@Composable
fun TutorialPreviewFontScale() {
    TutorialPreviewTemplate()
}

@Composable
fun TutorialPreviewTemplate() {
    val post = posts.highlightedPost

    TestComposeJetNewsTheme() {
        Surface() {
            PostCardTop(post = post)
        }
    }
}
