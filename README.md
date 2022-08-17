### AlgoUtils-Student

Das Modul _algoutils-student_ enthält Werkzeuge für die Student-Vorlagen.

### AlgoUtils-Tutor

Das Modul _algoutils-tutor_ enthält insbesondere Werkzeuge zum Testen von Student-Abgaben.

*   [Reflection Utils](https://github.com/tudalgo/algo-utils/tree/master/tutor/src/main/java/org/tudalgo/algoutils/reflect): Werkzeuge zum fehlertoleranten Prüfen von Deklarationen mittels Reflections.
    *   Wird _nur_ dann verwendet, wenn von Studierenden gefordert wird, Deklarationen selber vorzunehmen.
    *   Besonderheit ist, dass Deklarationen trotz Schreibfehlern in Identifiers gefunden werden.
    *   Wurde beispielsweise verwendet in [FOP-2122-H05-Root](https://github.com/FOP-2022/FOP-2022-H05-Root).

# Snapshot-Versionen

Zurzeit existieren nur Snapshot-Versionen von AlgoUtils. Um eine Snapshot-Version zu verwenden, ergänze in der build.gradle.kts folgendes Repository und folgende Dependencies:

*   Repository `maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")`
*   Dependencies (hier v0.1.0)
    *   AlgoUtils-Student `implementation("org.tudalgo:algoutils-student:0.1.0-SNAPSHOT")`
    *   AlgoUtils-Tutor `"graderImplementation"("org.tudalgo:algoutils-tutor:0.1.0-SNAPSHOT")`

# Publishing

```text
1. PGP Setup
2. Gradle muss PGP Setup kennen
3. Einrichten von sonatypeUsername+sonatypePassword
4. gradlew publish (kein Builden notwendig)
```
