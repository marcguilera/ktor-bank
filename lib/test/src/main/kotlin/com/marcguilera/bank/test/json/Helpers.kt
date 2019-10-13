package com.marcguilera.bank.test.json

import org.json.JSONArray
import org.json.JSONObject

/**
 * Creates a [JSONObject] with the given [Pair] values.
 */
fun jsonObjectOf(vararg map: Pair<String, Any>)
        = map.fold(JSONObject()) { o, p -> o.put(p.first, p.second) }

/**
 * Creates a [JSONArray] containing the given [JSONObject].
 */
fun jsonArrayOf(vararg obj: JSONObject)
        = obj.fold(JSONArray()) { o, p -> o.put(p) }