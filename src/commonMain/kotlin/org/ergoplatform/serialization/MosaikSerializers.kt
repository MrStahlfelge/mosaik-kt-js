package org.ergoplatform.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.ergoplatform.mosaik.model.MosaikApp
import org.ergoplatform.mosaik.model.actions.*
import org.ergoplatform.mosaik.model.ui.*
import org.ergoplatform.mosaik.model.ui.input.*
import org.ergoplatform.mosaik.model.ui.layout.*
import org.ergoplatform.mosaik.model.ui.text.Button
import org.ergoplatform.mosaik.model.ui.text.ErgAmountLabel
import org.ergoplatform.mosaik.model.ui.text.FiatAmountLabel
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
                subclass(Card::class)
                subclass(Column::class)
                subclass(DecimalInputField::class)
                subclass(DropDownList::class)
                subclass(ErgAmountInputField::class)
                subclass(ErgAmountLabel::class)
                subclass(ErgAddressInputField::class)
                subclass(ErgoAddressChooseButton::class)
                subclass(FiatAmountLabel::class)
                subclass(FiatOrErgAmountInputField::class)
                subclass(HorizontalRule::class)
                subclass(Icon::class)
                subclass(IntegerInputField::class)
                subclass(Image::class)
                subclass(Label::class)
                subclass(LazyLoadBox::class)
                subclass(LoadingIndicator::class)
                subclass(PasswordInputField::class)
                subclass(Row::class)
                subclass(TextInputField::class)
                subclass(WalletChooseButton::class)
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