package org.ergoplatform.mosaik.model.ui.text;

import org.ergoplatform.mosaik.model.Since;
import org.ergoplatform.mosaik.model.actions.Action;
import org.ergoplatform.mosaik.model.actions.TokenInformationAction;
import org.ergoplatform.mosaik.model.ui.ViewElement;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Label showing a token name and balance formatted. The label is clickable with a default
 * TokenInformationAction if no other action is set
 */
public class TokenLabel extends ViewElement {
    @Nullable private String tokenId;
    @Nullable private String tokenName;
    private int decimals;
    private long amount;

    @Nonnull
    public String getTokenId() {
        if (tokenId == null) {
            throw new IllegalStateException("No tokenId provided for " + this.getClass().getSimpleName());
        }

        return tokenId;
    }

    public void setTokenId(@Nullable String tokenId) {
        this.tokenId = tokenId;
    }

    @Nullable
    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(@Nullable String tokenName) {
        this.tokenName = tokenName;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TokenLabel that = (TokenLabel) o;
        return getDecimals() == that.getDecimals() && getAmount() == that.getAmount() && Objects.equals(getTokenId(), that.getTokenId()) && Objects.equals(getTokenName(), that.getTokenName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTokenId(), getTokenName(), getDecimals(), getAmount());
    }
}
