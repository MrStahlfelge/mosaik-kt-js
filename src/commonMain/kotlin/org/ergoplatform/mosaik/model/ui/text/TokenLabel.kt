package org.ergoplatform.mosaik.model.ui.text

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ergoplatform.mosaik.model.ui.ForegroundColor
import org.ergoplatform.mosaik.model.ui.ViewElement

/**
 * Label showing a token name and balance formatted. The label is clickable with a default
 * TokenInformationAction if no other action is set
 */
@Serializable
@SerialName("TokenLabel")
class TokenLabel : ViewElement(), StyleableLabel {
    var tokenId: String = ""
    var tokenName: String? = null
    var decimals = 0
    var amount: Long? = null
    override var style: LabelStyle = LabelStyle.BODY1
    override var textColor: ForegroundColor = ForegroundColor.DEFAULT
}