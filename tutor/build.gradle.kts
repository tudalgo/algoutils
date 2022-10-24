dependencies {
    api(project(":algoutils-student"))
    api(libs.spoon)
    api(libs.jagr)
    api(libs.mockito)
    api(libs.asm)
    testImplementation(libs.junit)
}

tasks {
    test {
        useJUnitPlatform()
    }
}
