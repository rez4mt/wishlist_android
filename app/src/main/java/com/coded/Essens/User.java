package com.coded.Essens;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;

public class User extends ContextWrapper {
    JSONObject data ;
    SharedPreferences preferences;

    public User(Context ctx)
    {
        super(ctx.getApplicationContext());
        preferences = ctx.getApplicationContext().getSharedPreferences("SHARED_PREF",Context.MODE_PRIVATE);
        reload();
    }
    public static User from(Context ctx)
    {
        return new User(ctx);
    }
    public boolean save(JSONObject payload )
    {


        try {
            this.data = payload;
            preferences.edit().putString("user_info",data.toString())
                    .putBoolean("logged_in",true)
                    .putLong("u_date",System.currentTimeMillis())
                    .apply();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public void clear()
    {
        preferences.edit()
                .putLong("u_date",0)
                .putBoolean("logged_in",false)
                .putString("user_info",null).apply();
    }
    public void reload()
    {
        try {
            String s =  preferences.getString("user_info",null);
            if(s == null)
                return ;
            data = new JSONObject(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName()
    {
        try {
            System.out.println(data.toString());
            return data.getString("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Guest";
    }
    public String getUsername()
    {
        try {
            if(data.isNull("username"))
                return "";
            return data.getString("username");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public String getToken()
    {
        try {
            return data.getString("token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public boolean loggedIn()
    {
        return preferences.getBoolean("logged_in",false);
    }

    public String getPoints()
    {
        try {
            return data.getString("points");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getId()
    {
        if(!loggedIn())
            return null;
        try {
            return data.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
