package org.ergoplatform.mosaik.model.ui.text

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Label showing an address, providing share/copy and address book functionality
 * (in case application supports it).
 */
@Serializable
@SerialName("ErgoAddressLabel")
class ErgoAddressLabel : StyleableTextLabel<String?>(), ExpandableElement {
    override var expandOnClick: Boolean = true
    override var text: String? = null
}