package org.ergoplatform.mosaik.model.ui.input;

import org.ergoplatform.mosaik.model.Since;

/**
 * Text field to enter ergo addresses. Mosaik executors might show labels for known addresses
 * and provide address book functionality.
 *
 * Min value of 0 is considered that an optional address can be entered, a min value of greater
 * than 0 means that a valid address is mandatory.
 */
@Since(0)
public class ErgAddressInputField extends StringTextField {
    @Override
    public void setMaxValue(long maxValue) {
        throw new UnsupportedOperationException("Setting max value is not supported for ErgAddressInputField");
    }
}
