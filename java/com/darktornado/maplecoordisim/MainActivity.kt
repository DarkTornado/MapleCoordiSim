package com.darktornado.maplecoordisim

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, 0, 0, "아이템 추가")
        menu.add(0, 1, 0, "피부색 변경")
        menu.add(0, 2, 0, "새로 고침")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            0 -> inputName()
//            1 -> selectSkin()
            2 -> mc!!.update()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(this)
        layout.orientation = 1

        mc = MapleChar(this)
        layout.addView(mc!!.image)

        val list = ListView(this)
        adapter = ArrayAdapter<Any?>(this, android.R.layout.simple_list_item_1, names as List<Any?>)
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