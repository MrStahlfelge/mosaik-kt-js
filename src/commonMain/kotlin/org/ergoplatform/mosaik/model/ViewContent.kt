package org.ergoplatform.mosaik.model

import org.ergoplatform.mosaik.model.actions.Action
import org.ergoplatform.mosaik.model.ui.ViewElement

/**
 * Root view element with list of actions
 */
open class ViewContent(var view: ViewElement, var actions: List<Action> = emptyList())