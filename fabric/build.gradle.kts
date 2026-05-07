val modId = project.property("mod_id") as String

architectury {
    platformSetupLoomIde()
    fabric()
}

val common: Configuration by configurations.creating
val developmentFabric: Configuration = configurations.getByName("developmentFabric")
val includeTransitive: Configuration = configurations.getByName("includeTransitive")

configurations {
    compileClasspath.get().extendsFrom(configurations["common"])
    runtimeClasspath.get().extendsFrom(configurations["common"])
    developmentFabric.extendsFrom(configurations["common"])
}

tasks {
    named<Jar>("mergeShadowAndJarJar") {
        from (
            zipTree( shadowJar.map { it.outputs.files.singleFile } ).matching {
                exclude("fabric.mod.json")
                exclude("LICENSE")
            },
            zipTree( jar.map { it.outputs.files.singleFile } ).matching {
                include("META-INF/jars/**")
                include("fabric.mod.json")
                include("LICENSE")
            }
        )
        archiveBaseName.set("${modId}-fabric")
    }

/*    remapJar {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.get().archiveFile)
        archiveBaseName.set("${modId}-fabric")
        archiveClassifier.set("")
        archiveVersion.set("")
    }*/

    shadowJar {
        archiveClassifier.set("dev-shadow")
        relocate("org.cloudburstmc", "org.geysermc.geyser.shaded.org.cloudburstmc")
    }

    jar {
        archiveClassifier.set("dev")
    }
}

dependencies {
    implementation(libs.fabric.loader)
    api(libs.fabric.api)
    common(project(":shared")) { isTransitive = false }
    compileOnly(libs.geyser.api)

    shadow(project(path = ":shared", configuration = "transformProductionFabric")) {
        isTransitive = false
    }

    compileOnly(libs.asm)

    runtimeOnly(libs.pack.converter)
    includeTransitive(libs.pack.converter)

    localRuntime(libs.geyser.fabric) {
        exclude(group = "io.netty")
        exclude(group = "io.netty.incubator")
    }
}

sourceSets {
    main {
        resources {
            srcDirs(project(":shared").sourceSets["main"].resources.srcDirs)
        }
    }
}