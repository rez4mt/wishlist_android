package com.coded.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.coded.Essens.ServerResponse;
import com.coded.Essens.User;
import com.coded.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;

import static com.coded.Config.*;

public class WishDetails extends AppCompatActivity {
    String my_id ;
    String owner;
    Toolbar toolbar;
    User u ;
    String checked
            = "0";
    ImageView img;
    TextView title , extra ,modifier;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wish_details);

        u = User.from(this);
        img = findViewById(R.id.image);
        title = findViewById(R.id.title);
        extra = findViewById(R.id.extra);
        toolbar = findViewById(R.id.toolbar);
        modifier = findViewById(R.id.modifier);

        String json_data = getIntent().getStringExtra("json");
        if(json_data == null)
            finish();
        try {
            JSONObject data = new JSONObject(json_data);
            setData(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setSupportActionBar(toolbar);
    }
    private void setData(JSONObject data)throws JSONException
    {
        my_id = data.getString("id");
        owner = data.getString("owner");
        checked = data.getString("checked");
        if(!data.isNull("image"))
            load(img , data.getString("image"));
        title.setText(data.getString("title"));
        extra.setText(Uri.decode(data.getString("extra")));
        if(!data.isNull("checker"))
            modifier.setText("Last Check modifier : "+data.getString("checker"));

        if(extra.getText().toString().contains("amazon"))
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(extra.getText().toString()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            },3000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //check if it is possible to share
        if(!u.getId().equals(owner))
        {
            toast(getBaseContext() , "You cant delete others wishes.");
            return super.onOptionsItemSelected(item);

        }
        delete();
        return super.onOptionsItemSelected(item);
    }
    public void delete()
    {

        getClient().get(U("user/remove",u.getToken(), my_id), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ServerResponse resp = new ServerResponse(responseBody);
                if(!resp.ok())
                {
                    toast(getBaseContext() , resp.getMessage());
                    return ;
                }
                toast(getBaseContext() , "Removed");
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

}
