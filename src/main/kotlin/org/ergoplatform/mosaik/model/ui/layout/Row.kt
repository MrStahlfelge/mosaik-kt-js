package org.ergoplatform.mosaik.model.ui.layout;

import org.ergoplatform.mosaik.model.Since;

import java.util.Objects;

/**
 * Use Row to place items horizontally on the screen.
 * By default, Row will fill all available horizontal space. If that is not wanted, set it's
 * `packed` property to `true`
 */
@Since(0)
public class Row extends LinearLayout<VAlignment> {

    private boolean packed = false;

    @Override
    public VAlignment defaultChildAlignment() {
        return VAlignment.CENTER;
    }

    public boolean isPacked() {
        return packed;
    }

    public void setPacked(boolean packed) {
        this.packed = packed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Row row = (Row) o;
        return isPacked() == row.isPacked();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isPacked());
    }
}
