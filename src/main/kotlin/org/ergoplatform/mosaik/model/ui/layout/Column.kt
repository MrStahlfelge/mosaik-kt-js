package org.ergoplatform.mosaik.model.ui.layout;

import org.ergoplatform.mosaik.model.Since;

/**
 * Use Column to place items vertically on the screen
 */
@Since(0)
public class Column extends LinearLayout<HAlignment> {
    @Override
    public HAlignment defaultChildAlignment() {
        return HAlignment.CENTER;
    }
}
