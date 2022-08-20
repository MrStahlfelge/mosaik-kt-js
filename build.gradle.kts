import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
    google()
}

kotlin {
    js(IR) {
        browser {
            testTask {
                useMocha {
                    timeout = "30000"
                }
            }
        }
        binaries.executable()
    }
    jvm() // needed to make unit tests executable on jvm

    // TODO: separate Bulma and kotlinx-serialization to own repos

    sourceSets {
        val serializationVersion = "1.3.2"

        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
                implementation("com.github.MrStahlfelge.kig-decimal:KigDecimal:87932d8b8b")
                implementation("org.jetbrains:markdown:0.3.1")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmTest by getting

        val jsMain by getting {
            kotlin.srcDir("src/main/kotlin")
            resources.srcDir("src/main/resources")
            val ktorVersion = "1.6.7"

            dependencies {
                implementation(compose.web.core)
                implementation(npm("bulma", "0.9.4"))
                implementation(npm("qrcode", "1.5.1"))
                implementation(devNpm("sass-loader", "^13.0.0"))
                implementation(devNpm("sass", "^1.52.1"))
                implementation(devNpm("css-loader", "^6.5.1"))
                implementation(devNpm("style-loader", "^3.3.0"))
                implementation("io.ktor:ktor-client-js:$ktorVersion")
            }
        }
        val jsTest by getting
    }
}

// a temporary workaround for a bug in jsRun invocation - see https://youtrack.jetbrains.com/issue/KT-48273
afterEvaluate {
    rootProject.extensions.configure<NodeJsRootExtension> {
        nodeVersion = "16.0.0"
        versions.webpackDevServer.version = "4.0.0"
        versions.webpackCli.version = "4.10.0"
    }
}

