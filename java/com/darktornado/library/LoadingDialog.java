package com.darktornado.library;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadingDialog extends AlertDialog {

    private LoadingDialog(Context context) {
        super(context);
    }

    public static LoadingDialog create(Context ctx, String text) {
        LoadingDialog dialog = new LoadingDialog(ctx);
        LinearLayout layout = new LinearLayout(ctx);
        layout.setOrientation(1);
        layout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        ProgressBar bar = new ProgressBar(ctx);
        layout.addView(bar);
        TextView txt = new TextView(ctx);
        txt.setText(text);
        txt.setTextColor(Color.BLACK);
        txt.setTextSize(14);
        txt.setGravity(Gravity.CENTER);
        layout.addView(txt);
        int pad = dip2px(ctx, 5);
        layout.setPadding(pad, pad, pad, pad);
        dialog.setView(layout);
        dialog.show();
        return dialog;
    }

    private static int dip2px(Context ctx, int dips) {
        return (int) Math.ceil(dips * ctx.getResources().getDisplayMetrics().density);
    }



}
