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
import com.darktornado.library.LoadingDialog
import org.json.JSONObject
import java.util.*

class MainActivity : Activity() {

    private val ITEM_LIST_URL = "https://raw.githubusercontent.com/DarkTornado/MapleCoordiSim/main/maple.json"
    private var adapter: ArrayAdapter<*>? = null
    private var mc: MapleChar? = null
    private val names = ArrayList<String>()
    private val parts = ArrayList<String>()
    private var items: HashMap<String, Item>? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, 0, 0, "아이템 추가")
        menu.add(0, 1, 0, "피부 변경")
        menu.add(0, 2, 0, "새로 고침")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            0 -> inputName()
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
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, names as List<Any?>)
        list.adapter = adapter
        list.onItemClickListener = OnItemClickListener { parent: AdapterView<*>?, view: View?, pos: Int, id: Long ->
            val type = parts[id.toInt()]
            val name = mc!!.items.get(type)!!.name
            mc!!.items.remove(type)
            mc!!.update()
            toast("$name(이)가 삭제되었어요.")
            update()
        }
        layout.addView(list)

        setContentView(layout)
        val dialog = LoadingDialog.create(this, "아이템 목록 불러오는 중...")
        Thread(Runnable {
            val _list: String = Utils.getWebText(ITEM_LIST_URL)
            items = Item.createList(JSONObject(_list))
            runOnUiThread { dialog.dismiss() }
        }).start()
    }
    
    fun inputName() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("아이템 추가")
        val txt = EditText(this)
        txt.hint = "아이템 이름 입력..."
        txt.setSingleLine()
        dialog.setView(txt)
        dialog.setNegativeButton("취소", null)
        dialog.setPositiveButton("검색") { _dialog: DialogInterface?, which: Int ->
            val input = txt.text.toString()
            val item = items!![input]
            if (item == null) {
                searchItem(input)
            } else {
                mc!!.add(item)
                mc!!.update()
                update()
            }
        }
        dialog.show()
    }

    private fun searchItem(input: String) {
        val result = ArrayList<Item>()
        val names: Set<String> = items!!.keys
        for (name in names) {
            if (name.contains(input)) result.add(items!![name]!!)
        }
        showSearhResult(result.toTypedArray())
    }

    fun showSearhResult(result: Array<Item>) {
        val names = arrayOfNulls<String>(result.size)
        for (n in result.indices) {
            names[n] = result[n].name
        }
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("아이템 선택")
        dialog.setItems(names) { _dialog: DialogInterface?, w: Int ->
            mc!!.add(result[w])
            mc!!.update()
            toast(names[w].toString() + "(이)가 추가되었어요.")
            update()
        }
        dialog.setNegativeButton("취소", null)
        dialog.show()
    }

    private fun update() {
        names.clear()
        parts.clear()
        for (type in mc!!.items.keys) {
            names.add(mc!!.items[type]!!.name)
            parts.add(mc!!.items[type]!!.type)
        }
        adapter!!.notifyDataSetChanged()
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