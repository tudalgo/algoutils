import org.tudalgo.algoutils.script.AlgoUtilsPublishPlugin

plugins {
    `java-library`
}

val projectVersion = file("version").readLines().first()

allprojects {
    apply(plugin = "java-library")
    group = "org.tudalgo"
    version = projectVersion
    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
            sourceCompatibility = "17"
            targetCompatibility = "17"
        }
    }
}

subprojects {
    apply<AlgoUtilsPublishPlugin>()
}
