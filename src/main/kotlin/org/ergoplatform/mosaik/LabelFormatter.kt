package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ui.text.ErgAmountLabel
import org.ergoplatform.mosaik.model.ui.text.FiatAmountLabel
import org.ergoplatform.mosaik.model.ui.text.Label
import org.ergoplatform.mosaik.model.ui.text.StyleableTextLabel

object LabelFormatter {

    /**
     * returns formatted text to show to the user. If null is returned, no element should be
     * shown at all
     */
    fun getFormattedText(element: StyleableTextLabel<*>, treeElement: TreeElement): String? {
        return when (element) {

            is Label -> element.text ?: ""

            is ErgAmountLabel -> {
                val ergAmountString = ErgoAmount(element.text ?: 0).toStringRoundToDecimals(
                    element.maxDecimals.toLong(),
                    element.trimTrailingZero
                )

                return if (element.withCurrencySymbol)
                    "$ergAmountString $ergoCurrencyText"
                else
                    ergAmountString
            }

            is FiatAmountLabel -> {
                val fiatString =
                    treeElement.viewTree.mosaikRuntime.convertErgToFiat(element.text ?: 0)

                return if (fiatString == null && element.fallbackToErg)
                    ErgoAmount(element.text ?: 0).toStringRoundToDecimals(
                        4,
                        false
                    ) + " $ergoCurrencyText"
                else
                    fiatString
            }

            else -> element.text.toString()
        }
    }
}