package com.darktornado.maplecoordisim;

import android.content.Context;
import android.os.Build;
import android.webkit.WebView;
import android.widget.LinearLayout;

import java.util.HashMap;

public class MapleChar {

    public Item skin = Skin.list[Skin.DEFAULT_ID];
    public final HashMap<String, Item> items = new HashMap<>();
    public final WebView image;

    public MapleChar(Context ctx) {
        image = new WebView(ctx);
        image.setLayoutParams(new LinearLayout.LayoutParams(-1, ctx.getResources().getDisplayMetrics().heightPixels / 3));
        image.setBackgroundColor(0);
        update();
    }

    public void add(Item item) {
        items.put(item.type, item);
    }

    public void update() {
        String src = "<meta name='viewport' content='user-scalable=no width=device-width' />" +
                "<center><img src='" + createImageURL() + "' height=100%></center>";
        if (Build.VERSION.SDK_INT > 23) {
            image.loadDataWithBaseURL(null, src, "text/html; charset=UTF-8", null, null);
        } else {
            image.loadData(src, "text/html; charset=UTF-8", null);
        }
    }

    public String createImageURL() {
        StringBuilder url = new StringBuilder("https://maplestory.io/api/character/");

        //피부
        url.append("{\"itemId\":" + skin.id + ",\"region\":\"KMS\",\"version\":\"" + Item.VERSION + "\"},{\"itemId\":1" + skin.id + ",\"region\":\"KMS\",\"version\":\"" + Item.VERSION + "\"}");

        //아이템
        for (String type : items.keySet()) {
            url.append(",{\"itemId\":" + items.get(type).id + ",\"region\":\"KMS\",\"version\":\"" + Item.VERSION + "\"}");
        }

        url.append("/stand1/0/?renderMode=0");
        return url.toString();
    }

}
