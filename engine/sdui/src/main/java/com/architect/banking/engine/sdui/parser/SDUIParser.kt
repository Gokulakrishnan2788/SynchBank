package com.architect.banking.engine.sdui.parser

import com.architect.banking.core.domain.Result
import com.architect.banking.engine.sdui.model.ScreenModel
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Parses SDUI screen JSON into a [ScreenModel].
 *
 * Uses Kotlinx Serialization with lenient settings to handle missing/unknown fields
 * gracefully — new component types introduced on the server won't crash older clients.
 */
@Singleton
class SDUIParser @Inject constructor(private val json: Json) {

    /**
     * Parses [jsonString] into a [ScreenModel].
     *
     * @return [Result.Success] with the parsed model, or [Result.Error] on parse failure.
     */
    fun parse(jsonString: String): Result<ScreenModel> {
        return try {
            val model = json.decodeFromString<ScreenModel>(jsonString)
            Result.Success(model)
        } catch (e: Exception) {
            Result.Error(
                code = "PARSE_ERROR",
                message = "Failed to parse screen: ${e.message}",
            )
        }
    }
}
