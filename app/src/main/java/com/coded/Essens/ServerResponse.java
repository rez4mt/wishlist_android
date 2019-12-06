package com.coded.Essens;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerResponse {
    JSONObject data ;
    boolean ok = false;
    public ServerResponse(byte[] data)
    {
        this(new String(data ) , true);
    }
    public ServerResponse(String data)
    {
        this(data, true);
    }
    public ServerResponse(String data,boolean log)
    {
        try {
            System.out.println(data);
            this.data = new JSONObject(data);
            ok = this.data.getString("status").equals("ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ServerResponse(byte[] data , boolean log)
    {
        this(new String(data) , log);
    }

    public Object getPayload()
    {
        try {
            return data.getJSONObject("payload");
        } catch (JSONException e) {
            try {
                return data.getJSONArray("payload");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        return null;
    }
    public boolean ok()
    {
        return ok;
    }
    public String getAll()
    {
        try {
            return  data!=null ? data.toString(4): "NULL RESPONSE !";
        } catch (JSONException e) {
            e.printStackTrace();
            return "NULL" ;
        }
    }
    public String getMessage()
    {
        try {
            return data.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
