package org.ergoplatform.mosaik.model.ui.text

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.layout.HAlignment

@Serializable
@SerialName("Button")
class Button : ViewElement(), TextLabel<String?> {
    override var text: String? = null
    override var maxLines = 0
    override var truncationType = TruncationType.END
    override var textAlignment = HAlignment.CENTER
    var style = ButtonStyle.PRIMARY
    var isEnabled = true

    enum class ButtonStyle {
        PRIMARY, SECONDARY, TEXT
    }
}