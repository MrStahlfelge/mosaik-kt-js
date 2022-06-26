package org.ergoplatform.mosaik.model.ui;

import org.ergoplatform.mosaik.model.Since;
import org.ergoplatform.mosaik.model.actions.Action;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Shows a spinner/loading indicator
 */
@Since(0)
public class LoadingIndicator extends ViewElement {
    @Nonnull private Size size = Size.SMALL;

    @Nonnull
    public Size getSize() {
        return size;
    }

    public void setSize(@Nonnull Size size) {
        Objects.requireNonNull(size);
        this.size = size;
    }

    @Override
    public void setOnClickAction(@Nullable String action) {
        throw new IllegalArgumentException("OnClickAction can't be set for" +
                this.getClass().getSimpleName());
    }

    @Override
    public void setOnLongPressAction(@Nullable String action) {
        throw new IllegalArgumentException("OnOnLongPressAction can't be set for" +
                this.getClass().getSimpleName());
    }

    public enum Size {
        SMALL,
        MEDIUM
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LoadingIndicator that = (LoadingIndicator) o;
        return getSize() == that.getSize();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getSize());
    }
}
