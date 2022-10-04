package com.darktornado.maplecoordisim

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import org.json.JSONObject
import java.util.*

class MainActivity : Activity() {

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
            1 -> selectSkin()
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


    fun selectSkin() {
        val names = arrayOfNulls<String>(Skin.list.size)
        for (n in names.indices) {
            names[n] = Skin.list[n].name
        }
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("피부 선택")
        dialog.setItems(names) { _dialog: DialogInterface?, w: Int ->
            mc!!.skin = Skin.list[w]
            mc!!.update()
            toast(names[w].toString() + "가 선택되었어요.")
        }
        dialog.setNegativeButton("취소", null)
        dialog.show()
    }

    fun toast(msg: String?) {
        runOnUiThread { Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show() }
    }
}