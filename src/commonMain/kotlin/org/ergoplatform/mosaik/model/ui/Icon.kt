package org.ergoplatform.mosaik.model.ui;

import org.ergoplatform.mosaik.model.Since;

import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * Shows an icon
 */
@Since(0)
public class Icon extends ViewElement {
    @Nonnull
    private IconType iconType = IconType.INFO;
    @Nonnull
    private Size iconSize = Size.SMALL;
    @Nonnull
    private ForegroundColor tintColor = ForegroundColor.DEFAULT;

    @Nonnull
    public IconType getIconType() {
        return iconType;
    }

    public void setIconType(@Nonnull IconType iconType) {
        Objects.requireNonNull(iconType);
        this.iconType = iconType;
    }

    @Nonnull
    public Size getIconSize() {
        return iconSize;
    }

    public void setIconSize(@Nonnull Size iconSize) {
        Objects.requireNonNull(iconSize);
        this.iconSize = iconSize;
    }

    @Nonnull
    public ForegroundColor getTintColor() {
        return tintColor;
    }

    public void setTintColor(@Nonnull ForegroundColor tintColor) {
        Objects.requireNonNull(tintColor);
        this.tintColor = tintColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Icon icon = (Icon) o;
        return getIconType() == icon.getIconType() && getIconSize() == icon.getIconSize() && getTintColor() == icon.getTintColor();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getIconType(), getIconSize(), getTintColor());
    }

    public enum Size {
        SMALL,
        MEDIUM,
        LARGE
    }
}
