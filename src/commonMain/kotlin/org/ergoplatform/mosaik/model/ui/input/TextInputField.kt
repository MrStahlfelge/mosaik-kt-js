package org.ergoplatform.mosaik.model.ui.input

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Text field to enter general text
 */
@Serializable
@SerialName("TextInputField")
class TextInputField : StringTextField()