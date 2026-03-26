package com.architect.banking.engine.sdui.validator

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Types of field validations supported by the SDUI TEXT_FIELD component.
 */
enum class ValidationType { EMAIL, MIN_LENGTH, REGEX }

/**
 * Single validation rule as defined in the SDUI JSON.
 *
 * @property type The kind of validation to apply.
 * @property value The constraint value (e.g. "8" for MIN_LENGTH, regex pattern for REGEX).
 * @property message Error message shown when validation fails.
 */
data class ValidationRule(
    val type: ValidationType,
    val value: String,
    val message: String,
)

/**
 * Result of validating a single form field.
 */
sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val message: String) : ValidationResult()
}

/**
 * Validates form field values against a list of [ValidationRule]s.
 * Called by ViewModels before submitting a SUBMIT_FORM action.
 */
@Singleton
class FormValidator @Inject constructor() {

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")

    /**
     * Validates [value] against all [rules], returning the first failure found.
     *
     * @param value The current text input to validate.
     * @param rules Ordered list of validation rules to apply.
     * @return [ValidationResult.Valid] if all rules pass, [ValidationResult.Invalid] on first failure.
     */
    fun validate(value: String, rules: List<ValidationRule>): ValidationResult {
        for (rule in rules) {
            val result = applyRule(value, rule)
            if (result is ValidationResult.Invalid) return result
        }
        return ValidationResult.Valid
    }

    private fun applyRule(value: String, rule: ValidationRule): ValidationResult = when (rule.type) {
        ValidationType.EMAIL -> {
            if (value.matches(emailRegex)) ValidationResult.Valid
            else ValidationResult.Invalid(rule.message)
        }
        ValidationType.MIN_LENGTH -> {
            val minLength = rule.value.toIntOrNull() ?: 0
            if (value.length >= minLength) ValidationResult.Valid
            else ValidationResult.Invalid(rule.message)
        }
        ValidationType.REGEX -> {
            val pattern = runCatching { Regex(rule.value) }.getOrNull()
            if (pattern != null && value.matches(pattern)) ValidationResult.Valid
            else ValidationResult.Invalid(rule.message)
        }
    }
}
