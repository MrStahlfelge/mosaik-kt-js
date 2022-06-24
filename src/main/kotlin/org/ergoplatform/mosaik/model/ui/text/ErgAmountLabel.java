package org.ergoplatform.mosaik.model.ui.text;

import org.ergoplatform.mosaik.model.Since;

import java.util.Objects;

/**
 * Shows nanoERG amount formatted
 */
@Since(0)
public class ErgAmountLabel extends StyleableTextLabel<Long> {
    private boolean withCurrencySymbol = true;
    private int maxDecimals = 4;
    private boolean trimTrailingZero = false;

    /**
     * @return true if currency symbol ("ERG") should be shown
     */
    public boolean isWithCurrencySymbol() {
        return withCurrencySymbol;
    }

    public void setWithCurrencySymbol(boolean withCurrencySymbol) {
        this.withCurrencySymbol = withCurrencySymbol;
    }

    /**
     * @return max number of decimals to use
     */
    public int getMaxDecimals() {
        return maxDecimals;
    }

    public void setMaxDecimals(int maxDecimals) {
        this.maxDecimals = maxDecimals;
    }

    /**
     * @return true if trailing zeroes should be omitted.
     */
    public boolean isTrimTrailingZero() {
        return trimTrailingZero;
    }

    public void setTrimTrailingZero(boolean trimTrailingZero) {
        this.trimTrailingZero = trimTrailingZero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ErgAmountLabel that = (ErgAmountLabel) o;
        return isWithCurrencySymbol() == that.isWithCurrencySymbol() && getMaxDecimals() == that.getMaxDecimals() && isTrimTrailingZero() == that.isTrimTrailingZero();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isWithCurrencySymbol(), getMaxDecimals(), isTrimTrailingZero());
    }
}
