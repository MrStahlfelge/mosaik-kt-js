package org.ergoplatform.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.ergoplatform.mosaik.model.MosaikApp
import org.ergoplatform.mosaik.model.actions.*
import org.ergoplatform.mosaik.model.ui.Icon
import org.ergoplatform.mosaik.model.ui.Image
import org.ergoplatform.mosaik.model.ui.LoadingIndicator
import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.input.TextInputField
import org.ergoplatform.mosaik.model.ui.layout.Box
import org.ergoplatform.mosaik.model.ui.layout.Column
import org.ergoplatform.mosaik.model.ui.layout.Row
import org.ergoplatform.mosaik.model.ui.text.Button
import org.ergoplatform.mosaik.model.ui.text.Label

object MosaikSerializers {
    private val jsonSerializer = Json {
        ignoreUnknownKeys = true
        serializersModule = SerializersModule {
            polymorphic(Action::class) {
                subclass(BackendRequestAction::class)
                subclass(BrowserAction::class)
                subclass(ChangeSiteAction::class)
                subclass(CopyClipboardAction::class)
                subclass(DialogAction::class)
                subclass(ErgoAuthAction::class)
                subclass(ErgoPayAction::class)
                subclass(NavigateAction::class)
                subclass(QrCodeAction::class)
                subclass(ReloadAction::class)
                subclass(TokenInformationAction::class)
            }
            polymorphic(ViewElement::class) {
                subclass(Button::class)
                subclass(Box::class)
                subclass(Column::class)
                subclass(Icon::class)
                subclass(Image::class)
                subclass(Label::class)
                subclass(LoadingIndicator::class)
                subclass(Row::class)
                subclass(TextInputField::class)
            }
        }
    }

    fun parseMosaikAppFromJson(json: String): MosaikApp {
        val jsonObject = jsonSerializer.parseToJsonElement(json).jsonObject

        // TODO parse view content and actions
        val view: ViewElement = jsonSerializer.decodeFromJsonElement(jsonObject["view"]!!)

        return MosaikApp(view).apply {
            manifest = jsonSerializer.decodeFromJsonElement(jsonObject["manifest"]!!)
            actions = jsonSerializer.decodeFromJsonElement(jsonObject["actions"]!!)
        }
    }
}