package org.ergoplatform.mosaik.model.ui.text;

import org.ergoplatform.mosaik.model.Since;
import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.layout.HAlignment;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Since(0)
public class Button extends ViewElement implements TextLabel<String> {
    @Nullable private String text;
    private int maxLines;
    @Nonnull private TruncationType truncationType = TruncationType.END;
    @Nonnull private HAlignment textAlignment = HAlignment.CENTER;
    @Nonnull ButtonStyle style = ButtonStyle.PRIMARY;
    private boolean isEnabled = true;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    @Nullable
    public String getText() {
        return text;
    }

    @Override
    public void setText(@Nullable String text) {
        this.text = text;
    }

    @Override
    public int getMaxLines() {
        return maxLines;
    }

    @Override
    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    @Override
    @Nonnull
    public TruncationType getTruncationType() {
        return truncationType;
    }

    @Override
    public void setTruncationType(@Nonnull TruncationType truncationType) {
        Objects.requireNonNull(truncationType);
        this.truncationType = truncationType;
    }

    @Override
    @Nonnull
    public HAlignment getTextAlignment() {
        return textAlignment;
    }

    @Override
    public void setTextAlignment(@Nonnull HAlignment textAlignment) {
        Objects.requireNonNull(textAlignment);
        this.textAlignment = textAlignment;
    }

    @Nonnull
    public ButtonStyle getStyle() {
        return style;
    }

    public void setStyle(@Nonnull ButtonStyle style) {
        Objects.requireNonNull(style);
        this.style = style;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Button button = (Button) o;
        return getMaxLines() == button.getMaxLines() && isEnabled() == button.isEnabled() && Objects.equals(getText(), button.getText()) && getTruncationType() == button.getTruncationType() && getTextAlignment() == button.getTextAlignment() && getStyle() == button.getStyle();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getText(), getMaxLines(), getTruncationType(), getTextAlignment(), getStyle(), isEnabled());
    }

    public enum ButtonStyle {
        PRIMARY,
        SECONDARY,
        TEXT
    }
}
