plugins {
    // Apply the java-library plugin for API and implementation separation.
    `java-library`
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    implementation("org.junit.jupiter:junit-jupiter:5.8.1")

    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api("org.apache.commons:commons-math3:3.6.1")

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation("com.google.guava:guava:30.1.1-jre")

    implementation(project(":algoutils-student"))
    implementation("fr.inria.gforge.spoon:spoon-core:10.0.0")
    implementation("org.sourcegrade:docwatcher-api:0.1")
    implementation("org.mockito:mockito-core:4.3.1")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
