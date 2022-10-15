dependencies {
    api(project(":algoutils-student"))
    api(libs.spoon)
    api(libs.jagr)
    api(libs.mockito)
    testImplementation(libs.junit)
}

tasks {
    test {
        useJUnitPlatform()
    }
}
