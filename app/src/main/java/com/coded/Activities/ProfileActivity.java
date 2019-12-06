package com.coded.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.coded.Activities.Profile.VideoAdapter;
import com.coded.Essens.ServerResponse;
import com.coded.Essens.User;
import com.coded.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.coded.Config.*;

public class ProfileActivity extends AppCompatActivity {
    String username , receivedName , id;
    TextView points , rank ;
    TextView title , name ;
    User u ;
    RecyclerView recyclerView;
    VideoAdapter adapter;
    Toolbar toolbar;
    FloatingActionButton newBtn , logOutBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        newBtn = findViewById(R.id.newbtn);
        logOutBtn = findViewById(R.id.logoutbtn);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        points = findViewById(R.id.points);
        rank = findViewById(R.id.rank);
        title = findViewById(R.id.title);
        name = findViewById(R.id.name);
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new VideoAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false));
        recyclerView.setAdapter(adapter);
        u = User.from(this);


        username = getIntent().getStringExtra("username");
        receivedName = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");

        if(username == null)
            username = u.getUsername();
        if(id == null)
            id = u.getId();
        if(receivedName == null)
            receivedName = u.getName();
        if(u.getUsername().equals(username))
        {
            title.setText("Hello");
            name.setText(u.getName());
            logOutBtn.setVisibility(View.VISIBLE);
            newBtn.setVisibility(View.VISIBLE);
        }else
        {
            title.setText(receivedName+"'s");
            name.setText("Profile");
        }
        //check if uname == myuname then show FAB button
        load();
        profile();

        ((TextView) toolbar.findViewById(R.id.username)).setText("");

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u.clear();
                startActivity(new Intent(getBaseContext() , LogRegActivity.class));
                finishAffinity();
            }
        });
        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext() , MakeActivity.class));
            }
        });
    }
    private void profile()
    {
        getClient().get(U("user/profile", username), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ServerResponse resp = new ServerResponse(responseBody);
                JSONObject b = (JSONObject) resp.getPayload();
                try {
                    rank.setText(b.getString("rank"));
                    points.setText(b.getString("points"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    private void load()
    {
        getClient() . post(U("user/videos",id), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ServerResponse resp = new ServerResponse(responseBody);
                if(!resp.ok())
                {
                    toast(getBaseContext() , resp.getMessage());
                    return ;
                }
                adapter.setData((JSONArray) resp.getPayload());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
