package com.darktornado.maplecoordisim

import org.json.JSONObject
import java.util.*

class Item {

    @kotlin.jvm.JvmField
    var name: String
    @kotlin.jvm.JvmField
    val id: String
    @kotlin.jvm.JvmField
    var type: String

    constructor(name: String, id: String, type: String) {
        this.name = name
        this.id = id
        this.type = type
    }

    constructor(name: String, id: Int, type: String) {
        this.name = name
        this.id = id.toString()
        this.type = type
    }

    companion object {
        const val VERSION = 366

        @Throws(Exception::class)
        fun createList(obj: JSONObject): HashMap<String, Item> {
            val map =
                HashMap<String, Item>()
            val keys = obj.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val item = obj.getJSONObject(key)
                map[key] = Item(key, item.getString("id"), item.getString("type"))
            }
            return map
        }

        
    }
}