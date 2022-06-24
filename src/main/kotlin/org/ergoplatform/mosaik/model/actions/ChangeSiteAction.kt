package org.ergoplatform.mosaik.model.actions

import org.ergoplatform.mosaik.model.ViewContent

/**
 * Action containing a new [org.ergoplatform.mosaik.model.ui.ViewElement].
 * The current view's content is replaced by the new
 * element by a difference analysis, thus resulting in scrollbars and element focus remaining.
 * If the new element's id is present in the current view, only that element is replaced. Thus it is
 * possible to change only very few elements of the tree.
 */
open class ChangeSiteAction : Action {
    lateinit var newContent: ViewContent

    override lateinit var id: String

}