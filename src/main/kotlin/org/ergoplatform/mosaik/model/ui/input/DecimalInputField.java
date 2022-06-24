package org.ergoplatform.mosaik.model.ui.input;

import org.ergoplatform.mosaik.model.Since;

import java.util.Objects;

/**
 * Text field to enter decimal numbers.
 */
@Since(0)
public class DecimalInputField extends LongTextField {
    private int scale = 9;

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DecimalInputField that = (DecimalInputField) o;
        return getScale() == that.getScale();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getScale());
    }
}
