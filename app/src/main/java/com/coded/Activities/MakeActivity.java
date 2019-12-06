package com.coded.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.coded.Essens.Progress;
import com.coded.Essens.ServerResponse;
import com.coded.Essens.User;
import com.coded.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;

import static com.coded.Config.*;

public class MakeActivity extends AppCompatActivity {
    EditText url ;
    Spinner categories ;
    Button submit;
    int current = 0 ;
    JSONArray data ;
    Progress p ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.makelayout);

        p = new Progress(this);
        p.light("Please Wait");
        p.dismiss();
        submit = findViewById(R.id.submit);
        url = findViewById(R.id.url);
        categories = findViewById(R.id.spinner);

        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                current = (int)id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        load();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    p.show();
                    getClient().post(U("home/addvideo", User.from(getBaseContext()).getToken(), Uri.encode(url.getText().toString()),
                            data.getJSONObject(current).getString("id")
                    ), new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            p.dismiss();
                            ServerResponse response = new ServerResponse(responseBody);
                            if(!response.ok())
                            {
                                toast(getBaseContext() , response.getMessage());
                                return;
                            }
                            toast(getBaseContext() , "Video Added");
                            finish();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            p.dismiss();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private String[] getList(JSONArray data)
    {
        this.data = data;
        String[] d = new String[data.length()];
        for(int i = 0 ; i  < d.length ; i++) {
            try {
                d[i] = data.getJSONObject(i).getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return d;
    }
    private void load()
    {
        //load categories and show in spinner
        getClient().get(U("home/getcategories"), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ServerResponse resp = new ServerResponse(responseBody);
                if(!resp.ok())
                {
                    toast(getBaseContext() , resp.getMessage());
                    return ;
                }

                ArrayAdapter cat_adapter = new ArrayAdapter<>(getBaseContext() ,R.layout.spinner_row,getList((JSONArray) resp.getPayload()));
                categories.setAdapter(cat_adapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
