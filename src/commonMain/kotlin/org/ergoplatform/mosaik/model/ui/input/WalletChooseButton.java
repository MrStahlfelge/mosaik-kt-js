package org.ergoplatform.mosaik.model.ui.input;

import org.ergoplatform.mosaik.model.Since;
import org.ergoplatform.mosaik.model.ui.ViewElement;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

/**
 * Button type element to let the user choose one of his wallets.
 * <p>
 * {@link #getValue()} might contain a list of wallet addresses or a single address.
 */
@Since(0)
public class WalletChooseButton extends ViewElement implements OptionalInputElement<List<String>> {
    @Nullable
    private List<String> addresses;
    @Nullable
    private String onValueChangedAction;
    private boolean enabled = true;
    private boolean mandatory = true;

    @Nullable
    @Override
    public List<String> getValue() {
        return addresses;
    }

    @Override
    public void setValue(@Nullable List<String> value) {
        addresses = value;
    }

    @Nullable
    @Override
    public String getOnValueChangedAction() {
        return onValueChangedAction;
    }

    @Override
    public void setOnValueChangedAction(@Nullable String action) {
        onValueChangedAction = action;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
    public void setOnClickAction(@Nullable String action) {
        throw new IllegalArgumentException("OnClickAction can't be set for" +
                this.getClass().getSimpleName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WalletChooseButton that = (WalletChooseButton) o;
        return isMandatory() == that.isMandatory() && isEnabled() == that.isEnabled() && Objects.equals(addresses, that.addresses) && Objects.equals(getOnValueChangedAction(), that.getOnValueChangedAction());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), addresses, getOnValueChangedAction(), isEnabled(), isMandatory());
    }
}
