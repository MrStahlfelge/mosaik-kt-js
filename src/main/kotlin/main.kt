import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.ergoplatform.mosaik.bulma.*
import org.ergoplatform.mosaik.js.HashRouter
import org.ergoplatform.mosaik.js.JsBackendConnector
import org.ergoplatform.mosaik.js.JsMosaikRuntime
import org.ergoplatform.mosaik.model.MosaikContext
import org.ergoplatform.mosaik.model.MosaikManifest
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.url.URL
import kotlin.js.Date
import kotlin.js.RegExp
import kotlin.math.max

fun main() {
    val dialogHandler = MosaikComposeDialogHandler()
    val runtime = JsMosaikRuntime(dialogHandler)
    val hashRouter = HashRouter(runtime)

    // DEBUG logging MosaikLogger.logger = MosaikLogger.DefaultLogger

    MainScope().launch {
        val config = JsBackendConnector.getMosaikConfig(
            (URL("mosaikconfig.json", document.location.toString())).href
        )
        hashRouter.setConfig(config)
        runtime.mosaikConfig = config

        val currentHash = mutableStateOf(window.location.hash)

        window.onhashchange = {
            currentHash.value = window.location.hash
            hashRouter.hashChanged(currentHash.value)
            Unit
        }
        document.addEventListener("visibilitychange", {
            if (js("document.visibilityState === 'visible'") as Boolean) {
                runtime.checkViewTreeValidity()
            }
        })

        // RegEx pattern from detectmobilebrowsers.com (public domain)
        val pattern =
            "(android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec" +
                    "|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)" +
                    "i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)" +
                    "|vodafone|wap|windows ce|xda|xiino|android|ipad|playbook|silk"

        val isMobile = RegExp(pattern).test(window.navigator.userAgent.lowercase())

        val guidKey = "mosaikguid"
        val guid = localStorage.getItem(guidKey) ?: run {
            val newguid = Uuid.uuid()
            localStorage.setItem(guidKey, newguid)
            newguid
        }

        JsBackendConnector.setContextHeaders(
            MosaikContext(
                MosaikContext.LIBRARY_MOSAIK_VERSION,
                guid,
                window.navigator.language,
                "Kotlin/JS Web Executor",
                MosaikContext.EXECUTOR_VERSION,
                if (!isMobile) MosaikContext.Platform.DESKTOP
                else if (max(
                        window.outerHeight,
                        window.outerWidth
                    ) > 600
                ) MosaikContext.Platform.TABLET
                else MosaikContext.Platform.PHONE,
                Date().getTimezoneOffset() * -1,
            )
        )

        ConnectWalletDialog.setConfig(config)
        MosaikStyleConfig.apply {
            responsiveMobileTextSize = config.responsiveMobileTextSize
        }

        document.getElementById("loadingScreen")?.remove()
        hashRouter.hashChanged(currentHash.value)
    }


    val viewTree = runtime.viewTree
    val manifestState = mutableStateOf<MosaikManifest?>(null)
    runtime.appLoaded = {
        manifestState.value = it
        document.title = it.appName
        runtime.appUrl?.let { hashRouter.appLoaded(it) }
        window.scrollTo(0.0, 0.0)
    }

    require("./custom.scss")
    renderComposable(rootElementId = "root") {
        MosaikViewTree(viewTree)
        ErgoPayRequestDialog(runtime)
        ConnectWalletDialog(runtime)
        MosaikComposeDialog(dialogHandler)
        runtime.notificationState.value?.let { notification ->
            BulmaNotification(
                attrs = {
                    style {
                        position(Position.Fixed)
                        top(0.px)
                        right(0.px)
                    }
                },
                classes = listOf("m-2"),
                color = BulmaColor.INFO,
            ) {
                Text(notification)
            }
            LaunchedEffect(notification) {
                delay(2000)
                runtime.notificationState.value = null
            }
        }
    }
}

external fun require(module: String): dynamic
