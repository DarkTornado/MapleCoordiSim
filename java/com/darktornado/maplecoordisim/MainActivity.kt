package com.darktornado.maplecoordisim

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.StrictMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import com.darktornado.library.ListAdapter
import com.darktornado.library.ListItem
import com.darktornado.library.LoadingDialog
import org.json.JSONObject
import java.util.*

class MainActivity : Activity() {

    private val ITEM_LIST_URL = "https://raw.githubusercontent.com/DarkTornado/MapleCoordiSim/main/maple.json"
    private var adapter: ListAdapter? = null
    private var mc: MapleChar? = null
    private val names = ArrayList<String>()
    private val parts = ArrayList<String>()
    private var itemList: HashMap<String, Item>? = null
    private val items: ArrayList<ListItem> = ArrayList<ListItem>()

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
        adapter = ListAdapter()
        adapter!!.setItems(items)
        list.adapter = adapter!!
        list.onItemClickListener = OnItemClickListener { parent: AdapterView<*>?, view: View?, pos: Int, id: Long ->
            val item = items[pos]
            val type = item.subtitle
            showItemMenu(mc!!.items[type]!!, item.icon)
        }
        layout.addView(list)

        setContentView(layout)
        val dialog = LoadingDialog.create(this, "아이템 목록 불러오는 중...")
        Thread(Runnable {
            val _list: String = Utils.getWebText(ITEM_LIST_URL)
            itemList = Item.createList(JSONObject(_list))
            runOnUiThread { dialog.dismiss() }
        }).start()
        StrictMode.enableDefaults()
    }
    
    fun inputName() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("아이템 추가")
        val layout = LinearLayout(this)
        layout.orientation = 1
        val txt = EditText(this)
        txt.hint = "아이템 이름 입력..."
        txt.setSingleLine()
        layout.addView(txt)
        val pad = dip2px(16)
        layout.setPadding(pad, pad, pad, pad)
        dialog.setView(layout)
        dialog.setNegativeButton("취소", null)
        dialog.setPositiveButton("검색") { _dialog: DialogInterface?, which: Int ->
            val input = txt.text.toString()
            val item = itemList!![input]
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
        val names: Set<String> = itemList!!.keys
        for (name in names) {
            if (name.contains(input)) result.add(itemList!![name]!!)
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

    fun showItemMenu(item: Item, icon: Drawable) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(item.name)
        dialog.setIcon(icon)
        dialog.setNegativeButton("닫기", null)
        dialog.setPositiveButton("삭제") { _dialog: DialogInterface?, w: Int ->
            mc!!.items.remove(item.type)
            mc!!.update()
            toast(item.name + "(이)가 삭제되었어요.")
            update()
        }
        dialog.show()
    }

    private fun update() {
        items.clear()
        for (type in mc!!.items.keys) {
            val item = mc!!.items[type]
            val icon: Drawable = BitmapDrawable(Utils.getWebImage("https://maplestory.io/api/KMS/" + Item.VERSION.toString() + "/item/" + item!!.id + "/icon"))
            items.add(ListItem(item.name, item.type, icon))
        }
        adapter!!.setItems(items)
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

    fun dip2px(dips: Int) = Math.ceil((dips * this.resources.displayMetrics.density).toDouble()).toInt()
    
}