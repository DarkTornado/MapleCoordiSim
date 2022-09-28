package com.darktornado.maplecoordisim;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Utils {
    public static String getWebText(String link) {
        try {
            URL url = new URL(link);
            URLConnection con = url.openConnection();
            if (con != null) {
                con.setConnectTimeout(5000);
                con.setUseCaches(false);
                con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36");
                InputStreamReader isr = new InputStreamReader(con.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                String str = br.readLine();
                String line = "";
                while ((line = br.readLine()) != null) {
                    str += "\n" + line;
                }
                br.close();
                isr.close();
                return str;
            }
        } catch (Exception e) {
            //toast(e.toString());
        }
        return null;
    }

    public static Bitmap getWebImage(String link) {
        try {
            URL url = new URL(link);
            URLConnection con = url.openConnection();
            if (con != null) {
                con.setConnectTimeout(5000);
                con.setUseCaches(false);
                BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
                Bitmap bitmap = BitmapFactory.decodeStream(bis);
                bis.close();
                return bitmap;
            }
        } catch (Exception e) {
            //toast(e.toString());
        }
        return null;
    }

}
