dependencies {
    api(project(":algoutils-student"))
    api(libs.spoon)
    api(libs.jagr)
    api(libs.mockito)
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
}

tasks {
    test {
        useJUnitPlatform()
    }
}
