package com.coded.Activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.coded.Essens.FileUtils;
import com.coded.Essens.ServerResponse;
import com.coded.Essens.User;
import com.coded.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;

import java.io.File;
import java.io.FileNotFoundException;

import static com.coded.Config.*;

public class WishMaker extends AppCompatActivity {
    ImageView img ;
    User u ;
    boolean img_selected = false;
    EditText titletv,extratv;
    File img_file ;
    Button submit ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make);
        img = findViewById(R.id.image);
        titletv = findViewById(R.id.title);
        extratv = findViewById(R.id.extra);
        submit = findViewById(R.id.submit);
        u = User.from(this);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!askPerms(WishMaker.this))
                {
                    System.out.println("Not params");
                    return;
                }
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if empy
                RequestParams params = new RequestParams();
                try {
                    if(img_selected)
                        params.put("img" , img_file);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                }
                String extra = ".";
                if(extratv.getText().length()>2)
                {
                    extra = Uri.encode(extratv.getText().toString());

                }

                getClient().post(U("user/addwish", u.getToken(),titletv.getText().toString(),extra),params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        ServerResponse resp = new ServerResponse(responseBody);
                        if(!resp.ok())
                        {
                            toast(getBaseContext() , resp.getMessage());
                            return ;
                        }
                        toast(getBaseContext() , "Done");
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if (data == null) return;
            try{
                img_selected = true;
                img_file = new File(FileUtils.getPath(this , data.getData()));
                img.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
