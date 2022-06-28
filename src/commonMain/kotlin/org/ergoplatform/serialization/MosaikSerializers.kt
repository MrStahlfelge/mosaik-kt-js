package org.ergoplatform.serialization

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.ergoplatform.mosaik.model.FetchActionResponse
import org.ergoplatform.mosaik.model.MosaikApp
import org.ergoplatform.mosaik.model.MosaikContext
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

        val viewObject = jsonObject["view"]!!
        val view: ViewElement =
            jsonSerializer.decodeFromJsonElement(preprocessViewElements(viewObject.jsonObject))

        return MosaikApp(view).apply {
            manifest = jsonSerializer.decodeFromJsonElement(jsonObject["manifest"]!!)
            jsonObject["actions"]?.let { actions = jsonSerializer.decodeFromJsonElement(it) }
            // TODO actions with view elements need to get preprocessed
        }
    }

    fun fetchActionResponseFromJson(json: String): FetchActionResponse {
        val jsonObject = jsonSerializer.parseToJsonElement(json).jsonObject
        // TODO actions with view elements need to get preprocessed
        return jsonSerializer.decodeFromJsonElement(jsonObject)
    }

    private fun preprocessViewElements(jsonObject: JsonObject): JsonObject {
        // check for "children", "weights" and "alignment"
        val childrenKey = "children"
        val weightKey = "weight"
        val alignKey = "align"
        val hAlignKey = "hAlign"
        val vAlignKey = "vAlign"

        return if (jsonObject.containsKey(childrenKey) && jsonObject.containsKey("type")) {
            val weightArray = mutableListOf<JsonElement>()
            val objectIsBox = listOf(
                "Box",
                "LazyLoadBox",
                "Card"
            ).contains(jsonObject["type"]!!.jsonPrimitive.content)
            val alignment1 = mutableListOf<JsonElement>()
            val alignment2 = mutableListOf<JsonElement>()

            JsonObject(jsonObject.toMutableMap().apply {
                put(
                    childrenKey,
                    JsonArray(jsonObject[childrenKey]!!.jsonArray.map {
                        val child = it.jsonObject

                        weightArray.add(JsonPrimitive(child[weightKey]?.jsonPrimitive?.int ?: 0))
                        if (objectIsBox) {
                            alignment1.add(
                                JsonPrimitive(
                                    child[hAlignKey]?.jsonPrimitive?.content ?: "CENTER"
                                )
                            )
                            alignment2.add(
                                JsonPrimitive(
                                    child[vAlignKey]?.jsonPrimitive?.content ?: "CENTER"
                                )
                            )
                        } else {
                            alignment1.add(
                                JsonPrimitive(
                                    child[alignKey]?.jsonPrimitive?.content ?: "CENTER"
                                )
                            )
                        }

                        // check next level
                        preprocessViewElements(child)
                    })
                )

                put("childWeight", JsonArray(weightArray))
                if (objectIsBox) {
                    put("childHAlignment", JsonArray(alignment1))
                    put("childVAlignment", JsonArray(alignment2))
                } else {
                    put("childAlignment", JsonArray(alignment1))
                }
            })
        } else jsonObject
    }

    fun valuesMapToJson(values: Map<String, Any?>): String {
        val newMap: Map<String, JsonElement> = values.filterValues { it != null }
            .mapValues {
                val value = it.value!!

                if (value is Number)
                    JsonPrimitive(value)
                else if (value is String)
                    JsonPrimitive(value)
                else if (value is Boolean)
                    JsonPrimitive(value)
                else if (value is List<*>)
                    throw UnsupportedOperationException("List not yet serialized") // TODO
                else
                    throw UnsupportedOperationException("Can't serialize type")
            }
        return jsonSerializer.encodeToString(JsonObject(newMap))
    }

    const val HTTP_HEADER_PREFIX = "Mosaik-"

    fun contextHeadersMap(context: MosaikContext): Map<String, String?> =
        jsonSerializer.encodeToJsonElement(context).jsonObject.mapValues { it.value.jsonPrimitive.content }
            .mapKeys { HTTP_HEADER_PREFIX + it.key }
}