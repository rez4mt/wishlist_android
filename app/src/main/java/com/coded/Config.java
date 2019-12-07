package com.coded;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class Config {
    public static String base = "http://192.168.8.100/s/wishlist/";
    public static String TAG = "RR";

    public static String U(String path , String... params)
    {
        StringBuilder s = new StringBuilder(base + path );
        if(s.charAt(s.length()-1) == '/')
            s.deleteCharAt(s.length()-1);
        for(int i = 0 ; i < params.length ; i++)
        {
            s.append("/");
            s.append(Uri.encode(params[i]==null || params[i].isEmpty()?" ":params[i]));
        }

        Log.w(TAG, "U: "+s.toString() );
        return s.toString();
    }

    public static void toast(Context ctx , String msg)
    {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }
    public static AsyncHttpClient getClient()
    {
        return new AsyncHttpClient();
    }

    public static void load(final ImageView img , final String image)
    {
        Picasso.get().load(U("image/get",image)).into(img, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }
        });

    }
    public static boolean askPerms(Activity ac)
    {
        if (ContextCompat.checkSelfPermission(ac.getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(ac.getBaseContext() , Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ac,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
            // Permission is not granted
            return false;
        }
        return true;
    }


}
