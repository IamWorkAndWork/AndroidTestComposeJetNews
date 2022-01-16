package com.example.testcomposejetnews.ui.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testcomposejetnews.R
import com.example.testcomposejetnews.data.posts.post3
import com.example.testcomposejetnews.model.Post
import com.example.testcomposejetnews.ui.theme.TestComposeJetNewsTheme
import com.example.testcomposejetnews.ui.utils.BookmarkButton

@Composable
fun PostTitle(post: Post) {
    Text(post.title, style = MaterialTheme.typography.subtitle1)
}

@Composable
fun PostCardSimple(
    post: Post,
    navigateToArticle: (String) -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {
    val bookmarkAction =
        stringResource(id = if (isFavorite) R.string.unbookmark else R.string.bookmark)

    Row(
        modifier = Modifier
            .clickable(onClick = {
                navigateToArticle(post.id)
            })
            .padding(16.dp)
            .semantics {
                customActions = listOf(CustomAccessibilityAction(
                    label = bookmarkAction,
                    action = {
                        onToggleFavorite();
                        true
                    }
                ))
            },
    ) {
        PostImage(post, Modifier.padding(end = 16.dp))
        Column(modifier = Modifier.weight(1f)) {
            PostTitle(post = post)
            AuthorAndReadTime(post = post)
        }
        BookmarkButton(
            isBookmarked = isFavorite,
            onClick = onToggleFavorite,
            modifier = Modifier.clearAndSetSemantics { },
            contentAlpha = ContentAlpha.medium
        )
    }
}

@Composable
fun AuthorAndReadTime(post: Post, modifier: Modifier = Modifier) {
    Row(modifier) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = stringResource(
                    id = R.string.home_post_min_read,
                    formatArgs = arrayOf(
                        post.metadata.author.name,
                        post.metadata.readTimeMinutes
                    )
                ),
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
fun PostImage(post: Post, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(post.imageThumbId), contentDescription = null,
        modifier = modifier
            .size(40.dp, 40.dp)
            .clip(MaterialTheme.shapes.small)
    )
}

@Composable
fun PostCardHistory(
    post: Post,
    navigateToArticle: (String) -> Unit
) {

    var openDialog by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier.clickable(
            onClick = {
                navigateToArticle(post.id)
            }
        )
    ) {
        PostImage(post = post, modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp))
        Column(
            Modifier
                .weight(1f)
                .padding(top = 16.dp, bottom = 16.dp)
        ) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = stringResource(id = R.string.home_post_based_on_history),
                    style = MaterialTheme.typography.overline
                )
            }
            PostTitle(post = post)
            AuthorAndReadTime(post = post, modifier = Modifier.padding(top = 4.dp))
        }
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            IconButton(onClick = {
                openDialog = true
            }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = stringResource(id = R.string.cd_more_actions)
                )
            }
        }
    }

    if (openDialog) {
        AlertDialog(
            modifier = Modifier.padding(20.dp),
            onDismissRequest = { openDialog = false },
            title = {
                Text(
                    text = stringResource(id = R.string.fewer_stories),
                    style = MaterialTheme.typography.h6
                )
            },
            confirmButton = {
                Text(text = stringResource(id = R.string.agree),
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(15.dp)
                        .clickable {
                            openDialog = false
                        }
                )
            }
        )
    }

}


@Preview("Bookmark Button")
@Composable
fun BookmarkButtonPreview() {
    TestComposeJetNewsTheme() {
        Surface {
            BookmarkButton(isBookmarked = false, onClick = {})
        }
    }
}

@Preview("Bookmark Button Bookmarked")
@Composable
fun BookmarkButtonBookmarkedPreview() {
    TestComposeJetNewsTheme() {
        Surface {
            BookmarkButton(isBookmarked = true, onClick = {})
        }
    }
}

@Preview("Simple Post Card")
@Preview("Simple Post Card (dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SimplePostPreview() {
    TestComposeJetNewsTheme() {
        Surface() {
            PostCardSimple(post3, {}, false, {})
        }
    }
}


@Preview("Post History Card")
@Composable
fun HistoryPostPreview() {
    TestComposeJetNewsTheme() {
        Surface() {
            PostCardHistory(post = post3, navigateToArticle = {})
        }
    }
}