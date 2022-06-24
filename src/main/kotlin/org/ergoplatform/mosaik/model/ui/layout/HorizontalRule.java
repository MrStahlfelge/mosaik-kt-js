package org.ergoplatform.mosaik.model.ui.layout;

import org.ergoplatform.mosaik.model.Since;
import org.ergoplatform.mosaik.model.ui.ViewElement;

import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * Horizontal Rule displays a horizontal separator or divider
 */
@Since(0)
public class HorizontalRule extends ViewElement {
    @Nonnull
    private Padding vPadding = Padding.DEFAULT;

    @Nonnull
    public Padding getvPadding() {
        return vPadding;
    }

    public void setvPadding(@Nonnull Padding vPadding) {
        Objects.requireNonNull(vPadding);
        this.vPadding = vPadding;
    }
}
