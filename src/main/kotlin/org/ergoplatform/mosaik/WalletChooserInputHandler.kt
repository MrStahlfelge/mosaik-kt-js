package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ui.input.OptionalInputElement

class WalletChooserInputHandler(
    private val element: OptionalInputElement,
    private val mosaikRuntime: MosaikRuntime
) : OtherInputHandler(element) {

    override fun getAsString(currentValue: Any?): String {
        val address = getFirstAddress(currentValue)

        return if (address != null && mosaikRuntime.isErgoAddressValid(address))
            mosaikRuntime.getErgoWalletLabel(address) ?: address
        else mosaikRuntime.formatString(StringConstant.ChooseWallet)
    }

    private fun getFirstAddress(value: Any?): String? {
        val addressList = value as? List<*>?
        return if (addressList?.isNotEmpty() == true) addressList.first() as? String else null
    }

    override fun isValueValid(value: Any?): Boolean {
        val firstAddress = getFirstAddress(value)
        return !element.mandatory || firstAddress != null && mosaikRuntime.isErgoAddressValid(firstAddress)
    }
}