dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

rootProject.name = "AlgoUtils"

sequenceOf(
    "student",
    "tutor",
).forEach {
    val project = ":algoutils-$it"
    include(project)
    project(project).projectDir = file(it)
}
