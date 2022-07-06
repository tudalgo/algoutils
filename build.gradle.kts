plugins {
    `java-library`
}
allprojects {
    apply(plugin = "java-library")
    dependencies {
        api("org.apache.commons:commons-math3:3.6.1")
        implementation("com.google.guava:guava:31.1-jre")
    }
}
