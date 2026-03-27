package com.architect.banking.engine.sdui.renderer

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.architect.banking.core.ui.theme.ArchitectColors
import com.architect.banking.engine.sdui.model.ScreenModel
import com.architect.banking.engine.sdui.model.SduiLayoutType
import com.architect.banking.engine.sdui.registry.ComponentRegistry

/**
 * Root SDUI rendering composable.
 *
 * Iterates [screenModel.components], resolves each via [ComponentRegistry],
 * and wraps them in the layout defined by [screenModel.layout].
 *
 * States:
 * - [isLoading] == true and [screenModel] == null → centered loading indicator
 * - [screenModel] != null → full screen render
 * - [isLoading] == false and [screenModel] == null → nothing (error toast handles feedback)
 *
 * ## Rules
 * - Never add feature-specific logic here.
 * - Never hardcode component rendering outside this renderer.
 * - All actions flow through [onAction] → ViewModel intent.
 *
 * @param screenModel Parsed screen model. Null while loading.
 * @param onAction Callback invoked with action ID when a component is tapped.
 * @param modifier Layout modifier applied to the root container.
 * @param isLoading True only while the screen definition is actively being fetched/parsed.
 * @param error Optional error message — shown as a toast when non-null.
 * @param registry Component registry used to resolve type → composable. Injectable for testing.
 */
@Composable
fun SDUIRenderer(
    screenModel: ScreenModel?,
    onAction: (String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    error: String? = null,
    registry: ComponentRegistry = ComponentRegistry(),
) {
    Box(modifier = modifier) {
        when {
            screenModel != null -> {
                ScreenContent(
                    screenModel = screenModel,
                    onAction = onAction,
                    modifier = Modifier.fillMaxSize(),
                    registry = registry,
                )
            }
            isLoading -> {
                LoadingState(modifier = Modifier.fillMaxSize())
            }
        }

        ErrorToast(message = error)
    }
}
@Composable
private fun ScreenContent(
    screenModel: ScreenModel,
    onAction: (String) -> Unit,
    modifier: Modifier,
    registry: ComponentRegistry,
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Render JSON-defined header outside the padded/scrollable body
        screenModel.header?.takeIf { it.visible }?.let { header ->
            registry.renderComponent(header, onAction)
        }

        val layout = screenModel.layout
        val horizontalPadding = layout.padding.horizontal.dp
        val verticalPadding = layout.padding.vertical.dp
        val paddingValues = PaddingValues(horizontal = horizontalPadding, vertical = verticalPadding)
        val visibleComponents = screenModel.components.filter { it.visible }

        when (layout.type) {
            SduiLayoutType.LAZY_COLUMN -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = paddingValues,
                ) {
                    items(visibleComponents, key = { it.id }) { component ->
                        registry.renderComponent(component, onAction)
                    }
                }
            }
            SduiLayoutType.SCROLL -> {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(paddingValues),
                ) {
                    visibleComponents.forEach { component ->
                        registry.renderComponent(component, onAction)
                    }
                }
            }
            SduiLayoutType.COLUMN -> {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(paddingValues),
                ) {
                    visibleComponents.forEach { component ->
                        registry.renderComponent(component, onAction)
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = ArchitectColors.NavyPrimary)
    }
}

@Composable
private fun ErrorToast(message: String?) {
    val context = LocalContext.current

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
}