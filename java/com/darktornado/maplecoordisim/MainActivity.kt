package com.darktornado.maplecoordisim

import android.R
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {

    private val ITEM_LIST_URL = "https://raw.githubusercontent.com/DarkTornado/MapleCoordiSim/main/maple.json"
    private var adapter: ArrayAdapter<*>? = null
    private var mc: MapleChar? = null
    private val names = ArrayList<String>()
    private var items: HashMap<String, Item>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(this)
        layout.orientation = 1

        mc = MapleChar(this)
        layout.addView(mc!!.image)

        val list = ListView(this)
        adapter = ArrayAdapter<Any?>(this, R.layout.simple_list_item_1, names as List<Any?>)
        list.adapter = adapter
        list.onItemClickListener = OnItemClickListener { parent: AdapterView<*>?, view: View?, pos: Int, id: Long ->
        }
        layout.addView(list)

        setContentView(layout)
        Thread(Runnable {
            val _list: String = Utils.getWebText(ITEM_LIST_URL)
            items = Item.createList(JSONObject(_list))
        }).start()
    }
}