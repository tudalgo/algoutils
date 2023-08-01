import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import java.net.URL

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
}

dependencies {
    dokkaPlugin(libs.dokkaKotlinAsJavaPlugin)
}

tasks.dokkaHtmlPartial {
    // moduleName.set("Student")
    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        footerMessage = "(c) 2023 tudalgo.org"
    }
    dokkaSourceSets.configureEach {
        includes.from("Module.md")
        jdkVersion.set(17)
        sourceLink {
            localDirectory.set(projectDir.resolve("src"))
            remoteUrl.set(URL("https://github.com/tudalgo/algoutils/tree/master/student/src"))
            remoteLineSuffix.set("#L")
        }
    }
}
