package com.coded.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.coded.Activities.Profile.WishAdapter;
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
    TextView title , name ;
    User u ;
    RecyclerView recyclerView;
    Toolbar toolbar;
    FloatingActionButton newBtn , logOutBtn;
    WishAdapter adapter;
    String username = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        newBtn = findViewById(R.id.newbtn);
        logOutBtn = findViewById(R.id.logoutbtn);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        adapter = new WishAdapter();
        title = findViewById(R.id.title);
        name = findViewById(R.id.name);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false));
        recyclerView.setLayoutManager(new GridLayoutManager(this , 2));
        u = User.from(this);


        String data = getIntent().getStringExtra("json");

        if(data!=null)
        {
            logOutBtn.setVisibility(View.INVISIBLE);
            newBtn.setVisibility(View.INVISIBLE);
            try {
                JSONObject md = new JSONObject(data);
                title.setText(md.getString("name")+"'s");
                name.setText("Wish list");
                username = md.getString("username");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //load other user

        }else{
            title.setText("Hello");
            name.setText(u.getName());
            username = u.getUsername();
        }




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
                startActivity(new Intent(getBaseContext() , WishMaker.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }
    private void load()
    {
        getClient() . post(U("user/wishes",username), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ServerResponse resp = new ServerResponse(responseBody);
                System.out.println(resp.getAll());
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //check if it is possible to share
        shareUsing(U("share",u.getUniqueId()));
        return super.onOptionsItemSelected(item);
    }

    public void shareUsing(String val)
    {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, val);
        startActivity(Intent.createChooser(sharingIntent, "Share Using"));
    }
}
