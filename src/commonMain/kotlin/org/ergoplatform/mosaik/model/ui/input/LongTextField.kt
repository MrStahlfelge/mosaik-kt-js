package org.ergoplatform.mosaik.model.ui.input

import kotlinx.serialization.Serializable

@Serializable
abstract class LongTextField : TextField() {
    var value: Long? = null
}