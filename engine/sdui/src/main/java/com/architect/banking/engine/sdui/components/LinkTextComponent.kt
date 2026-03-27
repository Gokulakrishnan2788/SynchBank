package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.architect.banking.core.ui.theme.ArchitectColors
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

/** Props for the LINK_TEXT SDUI component. */
@Serializable
data class LinkTextComponentProps(
    val prefix: String = "",
    val linkText: String = "",
    val suffix: String = "",
    val linkColor: String = "NavyPrimary",
)

private const val LINK_ANNOTATION_TAG = "LINK_ACTION"

/**
 * Renders a LINK_TEXT SDUI component with a tappable hyperlink segment.
 *
 * @param props Raw JSON props decoded into [LinkTextComponentProps].
 * @param onAction Called when the link text is tapped.
 */
@Composable
fun LinkTextComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json.decodeFromJsonElement<LinkTextComponentProps>(props)
    }.getOrDefault(LinkTextComponentProps())

    val resolvedLinkColor = decoded.linkColor.toArchitectColor()

    val annotatedString = buildAnnotatedString {
        append(decoded.prefix)
        pushStringAnnotation(tag = LINK_ANNOTATION_TAG, annotation = "link")
        withStyle(
            style = SpanStyle(
                color = resolvedLinkColor,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
            ),
        ) {
            append(decoded.linkText)
        }
        pop()
        append(decoded.suffix)
    }

    ClickableText(

        text = annotatedString,
        style = MaterialTheme.typography.bodyMedium.copy(
            color = ArchitectColors.MediumGray,
        ),
        onClick = { offset ->
            annotatedString
                .getStringAnnotations(LINK_ANNOTATION_TAG, offset, offset)
                .firstOrNull()
                ?.let { onAction("") }
        },
    )
}
