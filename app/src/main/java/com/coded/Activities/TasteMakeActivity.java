package com.coded.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.coded.Activities.Taste.CategoryChooser;
import com.coded.Essens.User;
import com.coded.R;

public class TasteMakeActivity extends AppCompatActivity {
    Button taste , make ;
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taste_make);
        taste = findViewById(R.id.taste);
        make = findViewById(R.id.make);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        taste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext() , CategoryChooser.class));
            }
        });
        make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext() , MakeActivity.class));
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
