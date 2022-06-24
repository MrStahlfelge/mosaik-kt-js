import org.ergoplatform.mosaik.model.MosaikApp
import org.ergoplatform.mosaik.model.MosaikContext
import org.ergoplatform.mosaik.model.MosaikManifest
import org.ergoplatform.mosaik.model.ViewContent
import org.ergoplatform.mosaik.model.actions.NavigateAction
import org.ergoplatform.mosaik.model.ui.Image
import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.layout.*
import org.ergoplatform.mosaik.model.ui.text.Label
import org.ergoplatform.mosaik.model.ui.text.LabelStyle

fun selectorApp(): MosaikApp {
    val loadBoxManifest = MosaikManifest(
        "App Selector",
        1,
        MosaikContext.LIBRARY_MOSAIK_VERSION,
        null,
        0
    )
    val mainContainer = Column()
    val appInfo = MosaikApp(mainContainer)
    appInfo.manifest = loadBoxManifest
    val headline = Label()
    headline.text = "Welcome to the Mosaik demo backend"
    headline.style = LabelStyle.HEADLINE2
    val description = Label()
    description.text = "Choose a demo to view from the buttons below"
    description.textAlignment = HAlignment.CENTER
    val row1 = Row()
    val visitorApp = addAppButton(appInfo, "Visitor demo", "visitors/")
    row1.addChild(visitorApp, VAlignment.CENTER, 1)
    val lazyBox = addAppButton(appInfo, "Lazy box demo", "lazybox/")
    row1.addChild(lazyBox, VAlignment.CENTER, 1)
    val row2 = Row()
    val alignmentApp = addAppButton(appInfo, "Alignments demo", "alignments/")
    row2.addChild(alignmentApp, VAlignment.CENTER, 1)
    row2.addChild(Box(), VAlignment.CENTER, 1)
    mainContainer.addChild(headline)
    mainContainer.addChild(description)
    mainContainer.addChild(row1)
    mainContainer.addChild(row2)
    return appInfo
}

private fun addAppButton(
    appInfo: ViewContent,
    text: String,
    url: String,
): ViewElement {
    val container = Box()
    container.padding = Padding.HALF_DEFAULT
    val button = Column()
    button.padding = Padding.DEFAULT
    val buttonImage = Image()
    buttonImage.size = Image.Size.MEDIUM
    buttonImage.url = "https://picsum.photos/400"
    val buttonLabel = Label()
    buttonLabel.style = LabelStyle.BODY1BOLD
    buttonLabel.textAlignment = HAlignment.CENTER
    buttonLabel.text = text
    val seperator = Box()
    seperator.padding = Padding.HALF_DEFAULT
    button.addChild(buttonImage)
    button.addChild(seperator)
    button.addChild(buttonLabel)
    val action = NavigateAction()
    action.id = url
    action.url = url
    val actions = appInfo.actions.toMutableList()
    actions.add(action)
    appInfo.actions = actions
    container.onClickAction = url
    container.addChild(button)
    return container
}