/*
 *   algo-utils - tudalgo.org
 *   Copyright (C) 2021-2022 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.tudalgo.algoutils.script

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugins.signing.SigningExtension
import org.gradle.plugins.signing.SigningPlugin
import java.net.URI

class AlgoUtilsPublishPlugin : Plugin<Project> {
    override fun apply(target: Project) = target.afterEvaluate { configure() }
    private fun Project.configure() {
        apply<JavaBasePlugin>()
        apply<MavenPublishPlugin>()
        apply<SigningPlugin>()
        extensions.configure<JavaPluginExtension> {
            withJavadocJar()
            withSourcesJar()
        }
        extensions.configure<PublishingExtension> {
            repositories {
                maven {
                    credentials {
                        username = project.findProperty("sonatypeUsername") as? String
                        password = project.findProperty("sonatypePassword") as? String
                    }
                    val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                    val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots"
                    url = URI(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
                }
            }
            publications {
                create<MavenPublication>("maven") {
                    from(components["java"])
                    pom {
                        name.set("algo-utils")
                        description.set("Common utilities for the Fachgebiet Algorithmik of TU Darmstadt")
                        url.set("https://www.tudalgo.org")
                        scm {
                            url.set("https://github.com/tudalgo/algo-utils")
                            connection.set("scm:git:https://github.com/tudalgo/algo-utils.git")
                            developerConnection.set("scm:git:https://github.com/tudalgo/algo-utils.git")
                        }
                        licenses {
                            license {
                                name.set("GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
                                url.set("https://www.gnu.org/licenses/agpl-3.0.html")
                                distribution.set("repo")
                            }
                        }
                        developers {
                            developer {
                                id.set("alexstaeding")
                                name.set("Alexander Staeding")
                            }
                            developer {
                                id.set("dst97")
                                name.set("Dustin Glaser")
                                email.set("dustin.glaser@postoe.de")
                            }
                            developer {
                                id.set("Rdeisenroth")
                                name.set("Ruben Deisenroth")
                                email.set("ruben@ruben-deisenroth.de")
                            }
                        }
                    }
                }
            }
        }
        extensions.configure<SigningExtension> {
            sign(extensions.getByType<PublishingExtension>().publications)
        }
    }
}
