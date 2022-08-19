package org.ergoplatform.mosaik.model.ui;

import java.util.Objects;

import javax.annotation.Nullable;

/**
 * shows a QR code
 */
public class QrCode extends ViewElement {
    @Nullable String content;

    @Nullable
    public String getContent() {
        return content;
    }

    public void setContent(@Nullable String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QrCode qrCode = (QrCode) o;
        return Objects.equals(getContent(), qrCode.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getContent());
    }

    public enum Size {
        SMALL,
        MEDIUM,
        BIG
    }
}
