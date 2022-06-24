package org.ergoplatform.mosaik.model.ui.text;

import org.ergoplatform.mosaik.model.Since;

import java.util.Objects;

/**
 * Shows nanoERG amount in user's currency, formatted
 */
@Since(0)
public class FiatAmountLabel extends StyleableTextLabel<Long> {
    private boolean fallbackToErg = false;

    /**
     * @return true if label should show ERG amount when no fiat currency is sets
     */
    public boolean isFallbackToErg() {
        return fallbackToErg;
    }

    public void setFallbackToErg(boolean fallbackToErg) {
        this.fallbackToErg = fallbackToErg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FiatAmountLabel that = (FiatAmountLabel) o;
        return isFallbackToErg() == that.isFallbackToErg();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isFallbackToErg());
    }
}
