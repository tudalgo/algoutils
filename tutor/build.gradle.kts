dependencies {
    api(project(":algoutils-student"))
    api(libs.spoon)
    api(libs.jagr)
    api(libs.mockito)
    api(libs.asm)
    testImplementation(libs.bundles.junit)
}

tasks {
    test {
        useJUnitPlatform()
    }
}
