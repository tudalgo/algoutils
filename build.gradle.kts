import org.tudalgo.algoutils.script.AlgoUtilsPublishPlugin

plugins {
    `java-library`
}

val projectVersion = file("version").readLines().first()

allprojects {
    apply(plugin = "java-library")
    group = "org.tudalgo"
    version = projectVersion
}

subprojects {
    apply<AlgoUtilsPublishPlugin>()
}
