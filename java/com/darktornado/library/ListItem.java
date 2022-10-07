package com.darktornado.library;

import android.graphics.drawable.Drawable;

public class ListItem {
    public String title, subtitle;
    public Drawable icon;

    public ListItem(String title, String subtitle, Drawable icon) {
        this.title = title;
        this.subtitle = subtitle;
        this.icon = icon;
    }
}
