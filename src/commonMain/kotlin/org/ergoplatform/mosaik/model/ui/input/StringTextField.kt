package org.ergoplatform.mosaik.model.ui.input

import kotlinx.serialization.Serializable

@Serializable
abstract class StringTextField : TextField() {
    var value: String? = null
}