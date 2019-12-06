package com.coded.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.coded.Essens.User;
import com.coded.R;

import static com.coded.Config.toast;

public class Landing extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing);
        //if logged in go to taste/make
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(User.from(getBaseContext()).loggedIn())
                {
                    //
                    startActivity(new Intent(getBaseContext() , TasteMakeActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(getBaseContext() , LogRegActivity.class));
                    finish();
                }
            }
        },1000);
    }
}
