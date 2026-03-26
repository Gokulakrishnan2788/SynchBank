package com.architect.banking.engine.sdui.renderer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
 * - [screenModel] == null → skeleton loading indicator
 * - [error] != null → error card
 * - Otherwise → full screen render
 *
 * ## Rules
 * - Never add feature-specific logic here.
 * - Never hardcode component rendering outside this renderer.
 * - All actions flow through [onAction] → ViewModel intent.
 *
 * @param screenModel Parsed screen model. Null while loading.
 * @param onAction Callback invoked with action ID when a component is tapped.
 * @param modifier Layout modifier applied to the root container.
 * @param error Optional error message — shown instead of components when non-null.
 * @param registry Component registry used to resolve type → composable. Injectable for testing.
 */
@Composable
fun SDUIRenderer(
    screenModel: ScreenModel?,
    onAction: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    registry: ComponentRegistry = ComponentRegistry(),
) {
    when {
        error != null -> ErrorState(message = error, modifier = modifier)
        screenModel == null -> LoadingState(modifier = modifier)
        else -> ScreenContent(screenModel = screenModel, onAction = onAction, modifier = modifier, registry = registry)
    }
}

@Composable
private fun ScreenContent(
    screenModel: ScreenModel,
    onAction: (String) -> Unit,
    modifier: Modifier,
    registry: ComponentRegistry,
) {
    val layout = screenModel.layout
    val horizontalPadding = layout.padding.horizontal.dp
    val verticalPadding = layout.padding.vertical.dp
    val paddingValues = PaddingValues(horizontal = horizontalPadding, vertical = verticalPadding)
    val visibleComponents = screenModel.components.filter { it.visible }

    when (layout.type) {
        SduiLayoutType.LAZY_COLUMN -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = paddingValues,
            ) {
                items(visibleComponents, key = { it.id }) { component ->
                    registry.renderComponent(component, onAction)
                }
            }
        }
        SduiLayoutType.SCROLL -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
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
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                visibleComponents.forEach { component ->
                    registry.renderComponent(component, onAction)
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
private fun ErrorState(message: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = ArchitectColors.Error,
        )
    }
}
