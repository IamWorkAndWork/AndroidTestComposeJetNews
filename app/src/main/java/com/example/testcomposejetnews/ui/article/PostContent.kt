package com.example.testcomposejetnews.ui.article

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testcomposejetnews.R
import com.example.testcomposejetnews.data.posts.post3
import com.example.testcomposejetnews.model.*
import com.example.testcomposejetnews.ui.theme.TestComposeJetNewsTheme


private val defaultSpacerSize = 16.dp

@Composable
fun PostContent(
    post: Post,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = defaultSpacerSize),
        state = state
    ) {
        postContentItems(post)
    }
}

fun LazyListScope.postContentItems(post: Post) {
    item {
        Spacer(modifier = Modifier.height(defaultSpacerSize))
        PostHeaderImage(post)
    }
    item {
        Text(text = post.title, style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(8.dp))
    }
    post.subtitle?.let { subtitle ->
        item {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.body2,
                    lineHeight = 20.sp
                )
            }
            Spacer(modifier = Modifier.height(defaultSpacerSize))
        }
    }
    item {
        PostMetadata(post.metadata)
        Spacer(Modifier.height(24.dp))
    }
    items(post.paragraphs) {
        Paragraph(paragraph = it)
    }
    item {
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun Paragraph(paragraph: Paragraph) {
    val (textStyle, paragraphStyle, trailingPadding) = paragraph.type.getTextAndParagraphStyle()

    val annotatedString = paragraphTpAnnotatedString(
        paragraph,
        MaterialTheme.typography,
        MaterialTheme.colors.codeBlockBackground
    )

    Box(modifier = Modifier.padding(bottom = trailingPadding)) {
        when (paragraph.type) {
            ParagraphType.Bullet -> BulletParagraph(
                text = annotatedString,
                textStyle = textStyle,
                paragraphStyle = paragraphStyle
            )
            ParagraphType.CodeBlock -> CodeBlockParagraph(
                text = annotatedString,
                textStyle = textStyle,
                paragraphStyle = paragraphStyle
            )
            ParagraphType.Header -> {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = annotatedString,
                    style = textStyle.merge(paragraphStyle)
                )
            }
            else -> {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = annotatedString,
                    style = textStyle
                )
            }
        }
    }
}

@Composable
fun CodeBlockParagraph(
    text: AnnotatedString,
    textStyle: TextStyle,
    paragraphStyle: ParagraphStyle
) {
    Surface(
        color = MaterialTheme.colors.codeBlockBackground,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            style = textStyle.merge(paragraphStyle)
        )
    }
}

@Composable
fun BulletParagraph(text: AnnotatedString, textStyle: TextStyle, paragraphStyle: ParagraphStyle) {
    Row {
        with(LocalDensity.current) {
            Box(
                modifier = Modifier
                    .size(8.sp.toDp(), 8.sp.toDp())
                    .alignBy {
                        9.sp.roundToPx()
                    }
                    .background(LocalContentColor.current, CircleShape)
            )
        }
        Text(
            modifier = Modifier
                .weight(1f)
                .alignBy(FirstBaseline),
            text = text,
            style = textStyle.merge(paragraphStyle)
        )
    }
}

fun paragraphTpAnnotatedString(
    paragraph: Paragraph,
    typography: Typography,
    codeBlockBackground: Color
): AnnotatedString {
    val styles: List<AnnotatedString.Range<SpanStyle>> = paragraph.markups
        .map {
            it.toAnnotatedString(typography, codeBlockBackground)
        }
    return AnnotatedString(text = paragraph.text, spanStyles = styles)
}

fun Markup.toAnnotatedString(
    typography: Typography,
    codeBlockBackground: Color
): AnnotatedString.Range<SpanStyle> {
    return when (this.type) {
        MarkupType.Italic -> {
            AnnotatedString.Range(
                typography.body1.copy(fontStyle = FontStyle.Italic).toSpanStyle(),
                start,
                end
            )
        }
        MarkupType.Link -> {
            AnnotatedString.Range(
                typography.body1.copy(textDecoration = TextDecoration.Underline).toSpanStyle(),
                start,
                end
            )
        }
        MarkupType.Bold -> {
            AnnotatedString.Range(
                typography.body1.copy(fontWeight = FontWeight.Bold).toSpanStyle(),
                start,
                end
            )
        }
        MarkupType.Code -> {
            AnnotatedString.Range(
                typography.body1.copy(
                    background = codeBlockBackground,
                    fontFamily = FontFamily.Monospace
                ).toSpanStyle(),
                start,
                end
            )
        }
    }
}

@Composable
fun PostMetadata(metadata: Metadata) {
    val typography = MaterialTheme.typography
    Row(
        modifier = Modifier.semantics(mergeDescendants = true) { }
    ) {
        Image(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            colorFilter = ColorFilter.tint(LocalContentColor.current),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = metadata.author.name,
                style = typography.caption,
                modifier = Modifier.padding(top = 4.dp)
            )

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = stringResource(
                        id = R.string.article_post_min_read, formatArgs = arrayOf(
                            metadata.date,
                            metadata.readTimeMinutes
                        )
                    ),
                    style = typography.caption
                )
            }
        }
    }
}

@Composable
fun PostHeaderImage(post: Post) {
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
    Spacer(modifier = Modifier.height(defaultSpacerSize))
}

private data class ParagraphStyling(
    val textStyle: TextStyle,
    val paragraphStyle: ParagraphStyle,
    val trailingPadding: Dp
)

@Composable
private fun ParagraphType.getTextAndParagraphStyle(): ParagraphStyling {
    val typography = MaterialTheme.typography
    var textStyle: TextStyle = typography.body1
    var paragraphStyle = ParagraphStyle()
    var trailingPadding = 24.dp

    when (this) {
        ParagraphType.Caption -> textStyle = typography.body1
        ParagraphType.Title -> textStyle = typography.h4
        ParagraphType.Subhead -> {
            textStyle = typography.h6
            trailingPadding = 16.dp
        }
        ParagraphType.Text -> {
            textStyle = typography.body1.copy(lineHeight = 28.sp)
            paragraphStyle = paragraphStyle.copy(lineHeight = 28.sp)
        }
        ParagraphType.Header -> {
            textStyle = typography.h5
            trailingPadding = 16.dp
        }
        ParagraphType.CodeBlock -> textStyle = typography.body1.copy(
            fontFamily = FontFamily.Monospace
        )
        ParagraphType.Quote -> textStyle = typography.body1
        ParagraphType.Bullet -> {
            paragraphStyle = ParagraphStyle(textIndent = TextIndent(firstLine = 8.sp))
        }
    }
    return ParagraphStyling(
        textStyle = textStyle,
        paragraphStyle = paragraphStyle,
        trailingPadding = trailingPadding
    )
}

private val Colors.codeBlockBackground: Color
    get() = onSurface.copy(alpha = .15f)


@Preview("Post Content")
@Preview("Post Content (dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewPost() {
    TestComposeJetNewsTheme() {
        Surface() {
            PostContent(post = post3)
        }
    }
}