package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.architect.banking.engine.sdui.model.ComponentModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class ImageComponentProps(
    val url: String? = null,
    val localAsset: String? = null,
    val contentScale: String = "FIT",
    val aspectRatio: Float? = null,
    val width: Int? = null,
    val height: Int? = null,
)

@Composable
fun ImageComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }.decodeFromJsonElement<ImageComponentProps>(props)
    }.getOrDefault(ImageComponentProps())

    val model = decoded.url ?: decoded.localAsset ?: return

    val modifier = when {
        decoded.width != null && decoded.height != null ->
            Modifier.size(decoded.width.dp, decoded.height.dp)

        decoded.width != null ->
            Modifier.size(decoded.width.dp)

        decoded.height != null ->
            Modifier.size(decoded.height.dp)

        decoded.aspectRatio != null ->
            Modifier.wrapContentSize()

        else ->
            Modifier.wrapContentSize()
    }

    AsyncImage(
        model = model,
        contentDescription = null,
        contentScale = decoded.contentScale.toContentScale(),
        modifier = modifier,
    )
}

private fun String.toContentScale(): ContentScale = when (this.uppercase()) {
    "CROP" -> ContentScale.Crop
    "FILL_BOUNDS" -> ContentScale.FillBounds
    "FILL_WIDTH" -> ContentScale.FillWidth
    "FILL_HEIGHT" -> ContentScale.FillHeight
    "INSIDE" -> ContentScale.Inside
    "NONE" -> ContentScale.None
    else -> ContentScale.Fit
}







//package com.architect.banking.engine.sdui.components
//
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.layout.ContentScale
//import coil.compose.AsyncImage
//import kotlinx.serialization.Serializable
//import kotlinx.serialization.json.Json
//import kotlinx.serialization.json.JsonObject
//import kotlinx.serialization.json.decodeFromJsonElement
//
///** Props for the IMAGE SDUI component. */
//@Serializable
//data class ImageComponentProps(
//    val url: String? = null,
//    val localAsset: String? = null,
//    val contentScale: String = "FIT",
//    val aspectRatio: Float = 1.0f,
//)
//
///**
// * Renders an IMAGE SDUI component using Coil for async loading.
// *
// * @param props Raw JSON props decoded into [ImageComponentProps].
// * @param onAction Unused — provided for uniform composable signature.
// */
//@Composable
//fun ImageComponent(props: JsonObject, onAction: (String) -> Unit) {
//    val decoded = runCatching {
//        Json.decodeFromJsonElement<ImageComponentProps>(props)
//    }.getOrDefault(ImageComponentProps())
//
//    val model = decoded.url ?: decoded.localAsset ?: return
//
//    AsyncImage(
//        model = model,
//        contentDescription = null,
//        contentScale = decoded.contentScale.toContentScale(),
//        modifier = Modifier
//            .fillMaxWidth()
//            .aspectRatio(decoded.aspectRatio),
//    )
//}
//
//private fun String.toContentScale(): ContentScale = when (this.uppercase()) {
//    "CROP" -> ContentScale.Crop
//    "FILL" -> ContentScale.FillBounds
//    else -> ContentScale.Fit
//}
