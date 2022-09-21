# Mosaik web executor

MosaikKtJs is a [Ergo Mosaik](https://github.com/MrStahlfelge/mosaik) executor for web browsers.

You can use it to run your Mosaik dApp UI on the browser. This way, you don't need to write any HTML
or JS code, but can target users preferring to use a website or that don't use a wallet application supporting Mosaik.

## Use it

To use it, use the precompiled mosaikweb.zip or build MosaikKtJs yourself. To adapt to your own
Mosaik app, you will need to edit the index.html file and the mosaikconfig.json files:

* index.html
  * Edit the title as wanted
  * Edit the loadingScreen section as wanted

* mosaikconfig.json
  * Edit the starturl entry to the URL where your Mosaik app is served
  * All other configuration entries are not mandatory, see section below for more information

Test by opening index.html in a web browser. Note that this must be served from a web server in
order to
work. [How to set up a local testing server.](https://developer.mozilla.org/en-US/docs/Learn/Common_questions/set_up_a_local_testing_server)

## Build yourself

This is not needed generally, but of course you can build yourself - this is needed for some UI
customization.

* You need Java 11.
* Clone this repository
* Call `./gradlew jsBrowserDistribution`

You will find the compiled version in build/distributions directory.

### Customize the build

You can customize colors and some of the layout by changing the [src/main/resources/custom.scss](https://github.com/MrStahlfelge/mosaik-kt-js/blob/master/src/main/resources/custom.scss) file
before compiling. As MosaikKtJs' interface is built up on [Bulma](https://bulma.io/), you can refer
to the [Bulma customization doc](https://bulma.io/documentation/customize/variables/).

## Configuration

For detailed information how to configure the web executor, check out the documentation in the  
[MosaikConfiguration.kt](https://github.com/MrStahlfelge/mosaik-kt-js/blob/master/src/main/kotlin/org/ergoplatform/mosaik/js/MosaikConfiguration.kt)
file.

## Differences to the reference executor

Although all efforts were made to keep the web executor behave as much as the reference implementation 
executor, some differences are expected and should be taken care of. If needed, you can [check for 
the calling executor in your Mosaik app](https://github.com/MrStahlfelge/mosaik-ageusddemo/blob/6871aba205be961c27cd58255759ba9fb553a949/src/main/kotlin/org/ergoplatform/mosaik/example/ageusd/AgeUsdController.kt#L221)
and change your layout accordingly. 

Here are some known differences to watch for:

### Row group element width
Under certain cirsumstances, row group elements expand horizontally when they do not on the reference Mosaik UI. You can suppress this by setting `packed=true` on the row when needed.

### Column child element widths
Some column child elements are not at max width, while they are on the reference executor. Simply work around
this by wrapping these elements in a `layout(HAlignment.JUSTIFY)` block.

### Rows and columns with weights
Behaviour is not always same as on reference executor. Please handle with care when you mix multiple children with weights and without weights.

### Box size
The first child element defines the size of the box, while on other platforms the biggest element defines the size