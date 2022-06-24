package org.ergoplatform.mosaik.model.ui.text;

import java.util.Objects;

/**
 * Label showing an address, providing share/copy and address book functionality
 * (in case application supports it).
 */
public class ErgoAddressLabel extends StyleableTextLabel<String> {
    private boolean expandOnClick = true;

    public ErgoAddressLabel() {
        setTruncationType(TruncationType.MIDDLE);
        setMaxLines(1);
    }

    // TODO default to OpenAddressBookAction


    public boolean isExpandOnClick() {
        return expandOnClick;
    }

    public void setExpandOnClick(boolean expandOnClick) {
        this.expandOnClick = expandOnClick;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ErgoAddressLabel that = (ErgoAddressLabel) o;
        return isExpandOnClick() == that.isExpandOnClick();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isExpandOnClick());
    }
}
