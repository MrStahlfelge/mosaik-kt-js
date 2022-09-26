package org.ergoplatform.mosaik.model.ui

import kotlinx.serialization.Serializable

@Serializable
enum class IconType {
    INFO,
    WARN,
    ERROR,
    CONFIG,
    ADD,
    EDIT,
    REFRESH,
    DELETE,
    CROSS,
    WALLET,
    SEND,
    RECEIVE,
    MORE,
    OPENLIST,
    CHEVRON_UP,
    CHEVRON_DOWN,
    COPY,
    BACK,
    FORWARD,
    SWITCH,
    QR_CODE,
    QR_SCAN,
    SEARCH,
}