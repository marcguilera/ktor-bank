plugins {
    kotlin("jvm")
}

dependencies {
    kotlin.forEach(::api)
    kodein.forEach(::api)
    logging.forEach(::api)
    consul.forEach(::api)
    ktor.forEach(::api)
    api(project(":lib:config"))
    api(project(":lib:consul"))
    api(project(":lib:time"))
    api(project(":lib:identifier"))
    api(project(":lib:error"))
}
