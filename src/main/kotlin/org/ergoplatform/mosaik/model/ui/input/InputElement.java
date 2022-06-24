package org.ergoplatform.mosaik.model.ui.input;

import org.ergoplatform.mosaik.model.actions.Action;

import javax.annotation.Nullable;

public interface InputElement<T> {
    @Nullable
    T getValue();

    void setValue(@Nullable T value);

    @Nullable
    String getOnValueChangedAction();

    void setOnValueChangedAction(@Nullable String action);

    boolean isEnabled();

    void setEnabled(boolean enabled);
}
