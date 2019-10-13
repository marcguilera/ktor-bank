package com.marcguilera.bank.test.json

import assertk.Assert
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.json.JSONArray
import org.json.JSONObject
import org.skyscreamer.jsonassert.JSONAssert

private val mapper = ObjectMapper().registerKotlinModule()

fun Assert<String?>.isJsonEqualTo(obj: Any) = given {
    val json = obj.toJSON()
    if (obj is Iterable<*>) JSONAssert.assertEquals(it, JSONArray(json), false)
    else JSONAssert.assertEquals(it, JSONObject(json), false)
}

fun Any.toJSON(): String
        = mapper.writeValueAsString(this)