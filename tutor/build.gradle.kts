dependencies {
    api(project(":algoutils-student"))
    api(libs.spoon)
    api(libs.docwatcher)
    api(libs.mockito)
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
}

tasks {
    test {
        useJUnitPlatform()
    }
}
