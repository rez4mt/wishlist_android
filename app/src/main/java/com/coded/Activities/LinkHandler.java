package com.coded.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.coded.Essens.ServerResponse;
import com.coded.Essens.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONObject;

import java.util.Arrays;

import static com.coded.Config.*;

public class LinkHandler extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        StringBuilder b = new StringBuilder(uri.toString());
        if(b.charAt(b.length()-1) == '/')
            b.deleteCharAt(b.length()-1);
        String[] strings = b.toString().split("/");

        getClient() .post(U("user/getinfo", strings[strings.length-1]), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ServerResponse resp = new ServerResponse(responseBody);
                if(resp.ok())
                {
                    startActivity(new Intent(getBaseContext() , ProfileActivity.class).putExtra("json",resp.getPayload().toString()));
                    finish();
                  }else {
                    toast(getBaseContext() , resp.getMessage());
                    finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
