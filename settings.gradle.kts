dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        mavenCentral()
    }
}

rootProject.name = "algoutils"

sequenceOf(
    "student",
    "tutor",
).forEach {
    val project = ":algoutils-$it"
    include(project)
    project(project).projectDir = file(it)
}
