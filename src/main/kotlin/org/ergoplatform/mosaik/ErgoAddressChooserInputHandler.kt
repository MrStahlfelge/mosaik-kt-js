package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ui.input.InputElement

class ErgoAddressChooserInputHandler(
    element: InputElement,
    private val mosaikRuntime: MosaikRuntime
) : OtherInputHandler(element) {

    override fun getAsString(currentValue: Any?): String {
        val address = currentValue as String?

        return if (address != null && mosaikRuntime.isErgoAddressValid(address))
            mosaikRuntime.getErgoAddressLabel(address) ?: address
        else mosaikRuntime.formatString(StringConstant.ChooseAddress)
    }

    override fun isValueValid(value: Any?): Boolean {
        return if (value != null && !mosaikRuntime.isErgoAddressValid(value as String)) false
        else super.isValueValid(value)
    }
}