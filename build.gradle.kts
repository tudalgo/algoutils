plugins {
    `java-library`
}

val projectVersion = file("version").readLines().first()

allprojects {
    apply(plugin = "java-library")
    version = projectVersion
}
