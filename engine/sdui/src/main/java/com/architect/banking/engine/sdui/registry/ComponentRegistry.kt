package com.architect.banking.engine.sdui.registry

import androidx.compose.runtime.Composable
import com.architect.banking.engine.sdui.components.ActivityItemComponent
import com.architect.banking.engine.sdui.components.AddBeneficiaryFormComponent
import com.architect.banking.engine.sdui.components.AmountInputCardComponent
import com.architect.banking.engine.sdui.components.BeneficiaryGridComponent
import com.architect.banking.engine.sdui.components.BiometricRowComponent
import com.architect.banking.engine.sdui.components.HeaderBarComponent
import com.architect.banking.engine.sdui.components.ButtonComponent
import com.architect.banking.engine.sdui.components.CardComponent
import com.architect.banking.engine.sdui.components.ColumnComponent
import com.architect.banking.engine.sdui.components.DividerComponent
import com.architect.banking.engine.sdui.components.ImageComponent
import com.architect.banking.engine.sdui.components.IconComponent
import com.architect.banking.engine.sdui.components.LineChartComponent
import com.architect.banking.engine.sdui.components.LinkTextComponent
import com.architect.banking.engine.sdui.components.RowComponent
import com.architect.banking.engine.sdui.components.SectionHeaderRowComponent
import com.architect.banking.engine.sdui.components.SourceAccountSelectorComponent
import com.architect.banking.engine.sdui.components.SpacerComponent
import com.architect.banking.engine.sdui.components.TextComponent
import com.architect.banking.engine.sdui.components.TextFieldComponent
import com.architect.banking.engine.sdui.components.TransferLimitBannerComponent
import com.architect.banking.engine.sdui.components.TransferProgressComponent
import com.architect.banking.engine.sdui.components.VerificationCardComponent
import com.architect.banking.engine.sdui.model.ComponentModel
import com.architect.banking.engine.sdui.model.SduiComponentType
import kotlinx.serialization.json.JsonObject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Registry that maps [SduiComponentType] values to their composable renderers.
 *
 * ## Open/Closed Principle
 * To add a new component type:
 * 1. Add its value to [SduiComponentType] enum.
 * 2. Create a new `XComponent.kt` file in the `components/` package.
 * 3. Register it in [renderComponent] below.
 * Never modify existing registrations.
 */
@Singleton
class ComponentRegistry @Inject constructor() {

    /**
     * Renders the composable for [component] using its resolved [SduiComponentType].
     * Silently skips components whose type is [SduiComponentType.UNKNOWN].
     *
     * @param component The component model from the SDUI screen JSON.
     * @param onAction Callback forwarded to the composable for user interactions.
     */
    @Composable
    fun renderComponent(component: ComponentModel, onAction: (String) -> Unit) {
        val actionForComponent: (String) -> Unit = { actionKey ->
            val resolvedKey = if (actionKey.isEmpty()) component.action ?: "" else actionKey
            if (resolvedKey.isNotEmpty()) onAction(resolvedKey)
        }

        when (component.type) {
            SduiComponentType.TEXT ->
                TextComponent(component.props, actionForComponent)

            SduiComponentType.TEXT_FIELD ->
                TextFieldComponent(component.id, component.props, actionForComponent)

            SduiComponentType.BUTTON ->
                ButtonComponent(component.props, actionForComponent)

            SduiComponentType.SPACER ->
                SpacerComponent(component.props, actionForComponent)

            SduiComponentType.DIVIDER ->
                DividerComponent(component.props, actionForComponent)

            SduiComponentType.IMAGE ->
                ImageComponent(component.props, actionForComponent)

            SduiComponentType.LINK_TEXT ->
                LinkTextComponent(component.props, actionForComponent)

            SduiComponentType.BIOMETRIC_ROW ->
                BiometricRowComponent(component.props, actionForComponent)

            SduiComponentType.CARD ->
                CardComponent(
                    props = component.props,
                    onAction = actionForComponent,
                    childRenderer = { child, childAction -> renderComponent(child, childAction) },
                )

            SduiComponentType.ROW ->
                RowComponent(
                    props = component.props,
                    children = component.children,
                    onAction = actionForComponent,
                    childRenderer = { child, childAction ->
                        renderComponent(child, childAction)
                    },
                )
            SduiComponentType.COLUMN ->
                ColumnComponent(
                    props = component.props,
                    onAction = actionForComponent,
                    childRenderer = { child, childAction ->
                        renderComponent(child, childAction)
                    }
                )

            SduiComponentType.HEADER_BAR ->
                HeaderBarComponent(component.props, actionForComponent)

            SduiComponentType.LINE_CHART ->
                LineChartComponent(component.props, actionForComponent)

            SduiComponentType.ICON ->
                IconComponent(component.props, actionForComponent)

            SduiComponentType.ACTIVITY_ITEM ->
                ActivityItemComponent(component.props, actionForComponent)

            SduiComponentType.SOURCE_ACCOUNT_SELECTOR ->
                SourceAccountSelectorComponent(component.props, actionForComponent)

            SduiComponentType.AMOUNT_INPUT_CARD ->
                AmountInputCardComponent(component.props, actionForComponent)

            SduiComponentType.SECTION_HEADER_ROW ->
                SectionHeaderRowComponent(component.props, actionForComponent)

            SduiComponentType.BENEFICIARY_GRID ->
                BeneficiaryGridComponent(component.props, actionForComponent)

            SduiComponentType.TRANSFER_LIMIT_BANNER ->
                TransferLimitBannerComponent(component.props, actionForComponent)

            SduiComponentType.TRANSFER_PROGRESS ->
                TransferProgressComponent(component.props, actionForComponent)

            SduiComponentType.ADD_BENEFICIARY_FORM ->
                AddBeneficiaryFormComponent(component.props, actionForComponent)

            SduiComponentType.VERIFICATION_CARD ->
                VerificationCardComponent(component.props, actionForComponent)

            SduiComponentType.ICON_BUTTON, SduiComponentType.UNKNOWN -> { /* not yet supported */ }
        }
    }
}
