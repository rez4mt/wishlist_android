package com.coded.Activities.Taste;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.coded.Activities.ProfileActivity;
import com.coded.Essens.Progress;
import com.coded.Essens.ServerResponse;
import com.coded.Essens.User;
import com.coded.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;

import static com.coded.Config.*;

public class CategoryChooser extends AppCompatActivity {
    Progress p ;
    RecyclerView recyclerView ;
    CatChooserAdapter adapter ;
    Toolbar toolbar ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cat_selector);

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new CatChooserAdapter();

        setSupportActionBar(toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false));
        recyclerView.setAdapter(adapter);

        p = new Progress(this);
        p.light("Please Wait .. ");
        p.dismiss();
        //load data ..
        getClient() . get(U("home/getcategories"), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                p.dismiss();
                ServerResponse resp = new ServerResponse(responseBody);
                if(!resp.ok())
                {
                    toast(getBaseContext() , resp.getMessage());
                    finish();
                    return;
                }
                adapter.setData((JSONArray) resp.getPayload());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                p.dismiss();
            }
        });


        ((TextView) toolbar.findViewById(R.id.username)).setText("Hello "+ User.from(this).getName());
        ( toolbar.findViewById(R.id.username)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext() , ProfileActivity.class));
            }
        });
    }
}
