plugins {
    idea
    java

    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("io.freefair.lombok") version "6.3.0"
    kotlin("jvm") version "1.6.21"
}


group = "com.github.Fernthedev"
version = "1.2"

var archivesBaseName: String
    get() = base.archivesName.get()
    set(value) = base.archivesName.set(value)

archivesBaseName = "FCommands"

java.sourceCompatibility = JavaVersion.VERSION_17 // Need this here so eclipse task generates correctly.
java.targetCompatibility = java.sourceCompatibility


repositories {
    mavenLocal()
    maven(url = "https://repo.aikar.co/content/groups/aikar/")
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

    maven(url = "https://repo.md-5.net/content/repositories/snapshots/")

    maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi/")

    maven(url = "https://nexus.hc.to/content/repositories/pub_releases")

    maven(url = "https://jitpack.io")

    maven(url = "https://repo.maven.apache.org/maven2")

    maven(url = "https://repo.md-5.net/content/repositories/releases/")
    maven(url = "https://ci.ender.zone/plugin/repository/everything/") {
        name = "ess-repo"
    }

    maven(url = "https://nexus.velocitypowered.com/repository/maven-public/") {
        name = "velocity"
    }

    flatDir() {
        dirs("Dependencies")
    }
    mavenCentral()

    maven(url = "https://maven.enginehub.org/repo/") {  }

}

val system: Configuration by configurations.creating {
    configurations.compileOnly.get().extendsFrom(this)
}

dependencies {
    implementation ("com.github.Fernthedev.FernAPI:all:35afca1cdc") { //1.9.0-rc4"
        exclude(group = "fr.minuskube.inv")
    }
    implementation ("com.github.Fernthedev.FernAPI:core") { //1.9.0-rc4"
        version() {
            strictly("35afca1cdc")
        }
    }


    // TODO: Publish this
    compileOnly("com.github.Fernthedev.preferences_api:api:0.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
    // kotlin coroutines baby
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.2.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.2.0")
    implementation("com.zaxxer:HikariCP:5.0.1")

    implementation("com.github.Fernthedev:FernUtils:1.3.3")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.0.4")
    compileOnly ("net.md-5:bungeecord-api:1.18-R0.1-SNAPSHOT")
    compileOnly ("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")
    compileOnly ("me.clip:placeholderapi:2.9.2")
    compileOnly ("net.milkbowl.vault:VaultAPI:1.7")
    compileOnly ("com.sk89q.worldguard:worldguard-bukkit:7.0.4-SNAPSHOT")
    implementation("com.velocitypowered:velocity-api:3.0.0")
    annotationProcessor("com.velocitypowered:velocity-api:3.0.0")
    system(name = "AdvancedBan-2.2.1-RELEASE", group = "")
    compileOnly ("com.github.sgtcaze:NametagEdit:8bbd20628b")
    compileOnly ("fr.neatmonster:nocheatplus:3.16.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}


tasks.jar {
    archiveClassifier.set("old")
    dependsOn("shadowJar")
}

tasks.shadowJar {
    configurations = listOf(project.configurations["runtimeClasspath"])
    archiveClassifier.set("")
    minimize {
        exclude(dependency("org.mariadb.jdbc:.*:.*"))

        exclude(dependency("org.mariadb.*:"))
        exclude(dependency("com.github.shynixn.mccoroutine.*:.*-core"))
        exclude(dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core"))
    }
    mergeServiceFiles()

    dependencies {
        exclude(dependency("${group}:preferences_api"))
    }

    relocate ("co.aikar.commands", "${project.group}.${archivesBaseName}.acf.commands")
    relocate ("co.aikar.locales", "${project.group}.${archivesBaseName}.acf.locales")


    val depGroupId = "${project.group}.${archivesBaseName}.dep"


    relocate("com.github.fernthedev.fcommands", "com.github.fernthedev.fcommands")
    relocate("com.github.fernthedev.preferences", "com.github.fernthedev.preferences")
    relocate("com.github.fernthedev", "${depGroupId}.fernthedev")
        // Why doesn't this work???
//    {
//        exclude("com.github.fernthedev.fcommands")
//        exclude("com.github.fernthedev.fcommands*")
//        exclude("com.github.fernthedev.fcommands*.")
//        exclude("com.github.fernthedev.fcommands.*")
//    }


    relocate("com.zaxxer", "${depGroupId}.com.zaxxer")
    relocate("org.jetbrains", "${depGroupId}.org.jetbrains")
    relocate("org.mariadb", "${depGroupId}.org.mariadb")
    relocate("com.google", "${depGroupId}.com.google")
    relocate("com.github.shynixn", "${depGroupId}.com.github.shynixn")
    relocate("kotlin", "${depGroupId}.kotlin")
    relocate("fr", "${depGroupId}.fr")


    finalizedBy("javadocJar")
    finalizedBy("sourcesJar")
}

tasks.processResources {
    dependsOn(tasks.compileJava)
    inputs.property("version", project.version)

    val velPath = tasks.compileJava.get().destinationDirectory.asFile.get()

    from(velPath) {
        include("velocity-plugin.json")

        expand("version" to project.version)
    }

    from(sourceSets.main.get().resources.srcDirs) {
        duplicatesStrategy = DuplicatesStrategy.WARN
        include("plugin.yml")
        include("bungee.yml")


        expand("version" to project.version)
    }

}

val deleteVel by tasks.registering(Delete::class) {
    dependsOn(tasks.compileJava.get())
    val velPath = tasks.compileJava.get().destinationDirectory.asFile.get()

    delete(File("${velPath.path}/velocity-plugin.json"))
}

tasks.jar.get().dependsOn(deleteVel.get())

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.compileJava {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
    options.isFork = true
    options.forkOptions.executable = "javac"
}

tasks.compileKotlin {
    kotlinOptions.javaParameters = true
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

tasks.compileTestKotlin {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])


            artifactId = archivesBaseName

            artifact(tasks["sourcesJar"]) {
                classifier = ("sources")
            }

            artifact(tasks["javadocJar"]) {
                classifier = ("javadoc")
            }
        }
    }
}



tasks.javadoc {
    isFailOnError = false
}