
// Services
arrayOf(
        "account",
        "balance",
        "transfer"
).forEach {
    include("$it:$it-service", "$it:$it-api", "$it:$it-client")
}

// Libs
arrayOf(
        "time",
        "identifier",
        "consul",
        "error",
        "service",
        "config",
        "test"
).forEach {
    include("lib:$it")
}
