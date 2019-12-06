package com.coded;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class Config {
    public static String base = "http://192.168.8.147/s/tastebuds/";
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

}
