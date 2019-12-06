package com.coded.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import com.coded.Essens.Progress;
import com.coded.Essens.ServerResponse;
import com.coded.Essens.User;
import com.coded.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONObject;

import static com.coded.Config.*;

public class LogRegActivity extends AppCompatActivity {
    Button forReg , forLog ;
    FrameLayout content ;
    Progress p ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logreg_selection);

        forReg = findViewById(R.id.forregister);
        forLog = findViewById(R.id.forlogin);
        content = findViewById(R.id.content);
        p = new Progress(this);
        p.light("Please wait");
        p.dismiss();;

        forReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupReg();
            }
        });
        forLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupLog();
            }
        });
        setupLog();
    }
    private void setupReg()
    {
        content.removeAllViews();;
        LayoutInflater.from(this).inflate(R.layout.register,content , true);
        content.findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) content.findViewById(R.id.username)).getText().toString();
                String password = ((EditText) content.findViewById(R.id.password)).getText().toString();
                String name = ((EditText) content.findViewById(R.id.name)).getText().toString();
                if(username.isEmpty())
                {
                    toast(getBaseContext() , "Enter Username");
                    return ;
                }
                if(password.isEmpty())
                {
                    toast(getBaseContext() , "Enter Password");
                    return;
                }
                if(name.isEmpty())
                {
                    toast(getBaseContext() , "Enter Name");
                    return ;
                }
                //call for register
                p.show();
                getClient().post(U("user/register" , username  , name ,password), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        p.dismiss();
                        if(statusCode!=200)
                            return ;
                        ServerResponse resp = new ServerResponse(responseBody);
                        if(!resp.ok())
                        {
                            toast(getBaseContext() , resp.getMessage());
                            return ;
                        }
                        //registered .. save info and go to another page
                        Log.w(TAG, "onSuccess: "+resp.getAll() );
                        User.from(getBaseContext()).save((JSONObject)resp.getPayload());
                        startActivity(new Intent(getBaseContext() , TasteMakeActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        //error
                        p.dismiss();
                    }
                });

            }
        });
    }
    private void setupLog()
    {
        content.removeAllViews();
        LayoutInflater.from(this).inflate(R.layout.login,content , true);
        content.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) content.findViewById(R.id.username)).getText().toString();
                String password = ((EditText) content.findViewById(R.id.password)).getText().toString();
                if(username.isEmpty())
                {
                    toast(getBaseContext() , "Enter Username");
                    return ;
                }
                if(password.isEmpty())
                {
                    toast(getBaseContext() , "Enter Password");
                    return;
                }
                //call for register
                p.show();
                getClient().post(U("user/login" , username , password ), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        p.dismiss();
                        if(statusCode!=200)
                            return ;
                        ServerResponse resp = new ServerResponse(responseBody);
                        if(!resp.ok())
                        {
                            toast(getBaseContext() , resp.getMessage());
                            return ;
                        }
                        //registered .. save info and go to another page
                        User.from(getBaseContext()).save((JSONObject) resp.getPayload());
                        Log.w(TAG, "onSuccess: "+resp.getAll() );
                        startActivity(new Intent(getBaseContext() , TasteMakeActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        //error
                        p.dismiss();
                    }
                });

            }
        });
    }
}
