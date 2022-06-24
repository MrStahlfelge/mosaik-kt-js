package org.ergoplatform.mosaik.model.ui.input;

import org.ergoplatform.mosaik.model.Since;
import org.ergoplatform.mosaik.model.ui.ViewElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A dropdown list element where user can select an option from entries key/text map
 */
@Since(0)
public class DropDownList extends ViewElement implements OptionalInputElement<String> {
    @Nullable
    private String value;
    @Nullable
    private String onValueChangedAction;
    private boolean enabled = true;
    @Nonnull
    private Map<String, String> entries = new HashMap<>();
    private boolean mandatory = true;

    @Override
    @Nullable
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(@Nullable String value) {
        this.value = value;
    }

    @Override
    @Nullable
    public String getOnValueChangedAction() {
        return onValueChangedAction;
    }

    @Override
    public void setOnValueChangedAction(@Nullable String onValueChangedAction) {
        this.onValueChangedAction = onValueChangedAction;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Nonnull
    public Map<String, String> getEntries() {
        return entries;
    }

    public void setEntries(@Nonnull Map<String, String> entries) {
        Objects.requireNonNull(entries);
        this.entries = entries;
    }

    @Override
    public boolean isMandatory() {
        return mandatory;
    }

    @Override
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DropDownList that = (DropDownList) o;
        return isEnabled() == that.isEnabled() && isMandatory() == that.isMandatory() && Objects.equals(getValue(), that.getValue()) && Objects.equals(getOnValueChangedAction(), that.getOnValueChangedAction()) && getEntries().equals(that.getEntries());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getValue(), getOnValueChangedAction(), isEnabled(), getEntries(), isMandatory());
    }
}
