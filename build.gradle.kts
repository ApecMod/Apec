plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.loom)
    alias(libs.plugins.publishing)
    alias(libs.plugins.ksp)
    alias(libs.plugins.fletchingtable.fabric)
}

class ModData {
    val id = property("mod.id").toString()
    val name = property("mod.name")
    val version = property("mod.version")
    val group = property("mod.group").toString()
}

class Dependencies {
    val fabricApiVersion = property("deps.fabric_api_version")
}

class McData {
    val version = property("mod.mc_version")
    val dep = property("mod.mc_dep").toString()
}

val mc = McData()
val mod = ModData()
val deps = Dependencies()

version = "${mod.version}+mc${mc.version}"
group = mod.group
base { archivesName.set(mod.id) }

loom {
    runConfigs.all {
        ideConfigGenerated(stonecutter.current.isActive)
        runDir = "../../run" // This sets the run folder for all mc versions to the same folder. Remove this line if you want individual run folders.
    }

    runConfigs.remove(runConfigs["server"]) // Removes server run configs
}

loom.runs {
    afterEvaluate {
        val mixinJarFile = configurations.runtimeClasspath.get().incoming.artifactView {
            componentFilter {
                it is ModuleComponentIdentifier && it.group == "net.fabricmc" && it.module == "sponge-mixin"
            }
        }.files.first()

        configureEach {
            vmArg("-javaagent:$mixinJarFile")
            vmArg("-XX:+AllowEnhancedClassRedefinition")

            property("mixin.hotSwap", "true")
            property("mixin.debug.export", "true") // Puts mixin outputs in /run/.mixin.out
        }
    }
}

//fletchingTable {
//    mixins.create("main") {
//        mixin("default", "${mod.id}.mixins.json")
//    }
//}

repositories {
    fun strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
        forRepository { maven(url) { name = alias } }
        filter { groups.forEach(::includeGroup) }
    }

    maven("https://maven.parchmentmc.org") // Parchment
    maven("https://maven.terraformersmc.com") // Mod Menu
    maven("https://maven.nucleoid.xyz/") // Placeholder API - required by Mod Menu
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1") // DevAuth
    maven("https://maven.bawnorton.com/releases") // MixinSquared
    strictMaven("https://api.modrinth.com/maven", "Modrinth", "maven.modrinth") // Modrinth
    strictMaven("https://www.cursemaven.com", "Curseforge", "curse.maven") // CurseForge
}

dependencies {
    minecraft("com.mojang:minecraft:${mc.version}")

    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        // Mojmap mappings
        officialMojangMappings()

        // Parchment mappings (it adds parameter mappings & javadoc)
        optionalProp("deps.parchment_version") {
            parchment("org.parchmentmc.data:parchment-${mc.version}:$it@zip")
        }
    })

    modImplementation(libs.fabricloader)
    modRuntimeOnly(libs.devauth)
    include(implementation(libs.mixinconstraints.get())!!)!!
    include(implementation(annotationProcessor(libs.mixinsquared.get())!!)!!)

    modImplementation("net.fabricmc.fabric-api:fabric-api:${deps.fabricApiVersion}+${mc.version}")
    modImplementation(fletchingTable.modrinth("modmenu", "${mc.version}", "fabric"))

    // REI integration - optional dependency
    modCompileOnly(fletchingTable.modrinth("rei", "${mc.version}", "fabric"))
}

// mc_dep fields must be in the format 'x', '>=x', '>=x <=y'
val rangeRegex = Regex(""">=\s*([0-9.]+)(?:\s*<=\s*([0-9.]+))?""")
val exactVersionRegex = Regex("""^\d+\.\d+(\.\d+)?$""")

val modrinthId = findProperty("publish.modrinth")?.toString()?.takeIf { it.isNotBlank() }
val curseforgeId = findProperty("publish.curseforge")?.toString()?.takeIf { it.isNotBlank() }

// accessTokens should be placed in the user Gradle gradle.properties file
// for example, on Windows this would be "C:\Users\{user}\.gradle\gradle.properties"
// then add:
// modrinth.token=
// curseforge.token=
publishMods {
    file = project.tasks.remapJar.get().archiveFile

    displayName = "${mod.name} ${mod.version}"
    this.version = mod.version.toString()

    // Allow overriding changelog via -Ppublish.changelogFile=path or -Ppublish.changelog=text
    val changelogFromFile = optionalProp("publish.changelogFile") { path ->
        rootProject.file(path).takeIf { it.exists() }?.readText()
    }
    changelog = changelogFromFile
        ?: optionalProp("publish.changelog") { it }
        ?: project.rootProject.file("CHANGELOG.md").takeIf { it.exists() }?.readText()
        ?: "No changelog provided."

    // Allow overriding release type via -Ppublish.type=[stable|beta|alpha]
    type = optionalProp("publish.type") {
        when (it.lowercase()) {
            "stable" -> STABLE
            "beta" -> BETA
            "alpha" -> ALPHA
            else -> STABLE
        }
    } ?: STABLE

    modLoaders.add("fabric")

    dryRun = modrinthId == null && curseforgeId == null

    if (modrinthId != null) {
        modrinth {
            projectId = property("publish.modrinth").toString()
            accessToken = findProperty("modrinth.token").toString()

            if (rangeRegex.matches(mc.dep)) {
                val match = rangeRegex.find(mc.dep)!!
                val minVersion = match.groupValues[1]
                val maxVersion = match.groupValues.getOrNull(2)?.takeIf { it.isNotBlank() } ?: "latest"

                minecraftVersionRange {
                    start = minVersion
                    end = maxVersion
                }
            } else if (exactVersionRegex.matches(mc.dep)) {
                minecraftVersions.add(mc.dep)
            }

            requires("fabric-api")
            requires("modmenu")
        }
    }

    if (curseforgeId != null) {
        curseforge {
            projectId = property("publish.curseforge").toString()
            accessToken = findProperty("curseforge.token").toString()

            if (rangeRegex.matches(mc.dep)) {
                val match = rangeRegex.find(mc.dep)!!
                val minVersion = match.groupValues[1]
                val maxVersion = match.groupValues.getOrNull(2)?.takeIf { it.isNotBlank() } ?: "latest"

                minecraftVersionRange {
                    start = minVersion
                    end = maxVersion
                }
            } else if (exactVersionRegex.matches(mc.dep)) {
                minecraftVersions.add(mc.dep)
            }

            requires("fabric-api")
            optional("modmenu")
        }
    }
}

java {
    // withSourcesJar() // Uncomment if you want sources
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.processResources {
    val props = buildMap {
        put("id", mod.id)
        put("name", mod.name)
        put("version", mod.version)
        put("mcdep", mc.dep)
    }

    props.forEach(inputs::property)

    filesMatching("**/lang/en_us.json") { // Defaults description to English translation
        expand(props)
        filteringCharset = "UTF-8"
    }

    filesMatching("fabric.mod.json") { expand(props) }
}

if (stonecutter.current.isActive) {
    rootProject.tasks.register("buildActive") {
        group = "project"
        dependsOn(tasks.named("build"))
    }
}

fun <T> optionalProp(property: String, block: (String) -> T?): T? =
    findProperty(property)?.toString()?.takeUnless { it.isBlank() }?.let(block)

tasks.remapJar {
    destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
}
