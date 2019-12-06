package com.coded.Activities.Taste;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.coded.Activities.ProfileActivity;
import com.coded.Activities.YoutubeLiveStream;
import com.coded.Essens.ServerResponse;
import com.coded.Essens.User;
import com.coded.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.coded.Config.*;

public class VoteActivity extends AppCompatActivity {
    String id = "";
    View panel_1 , panel_2 ;
    Button playagain , startover ;
    Toolbar toolbar;
    JSONArray data ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getStringExtra("id");
        setContentView(R.layout.voter);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        panel_1 = findViewById(R.id.panel_1);
        panel_2 = findViewById(R.id.panel_2);
        playagain = findViewById(R.id.playagain);
        startover = findViewById(R.id.startover);

        loadVideos();
        voter = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //unhide parent of play again
                getClient().post(U("home/vote", (String) v.getTag()), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        ServerResponse resp = new ServerResponse(responseBody);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
                String tag = (String) v.getTag();
                try {
                    if(tag.equals(data.getJSONObject(0).getString("id")))
                    {
                        ((TextView) panel_1.findViewById(R.id.vote_count)).setText("Votes for this video :\n"+(data.getJSONObject(0).getInt("votes")+1));

                    }else
                    {
                        ((TextView) panel_2.findViewById(R.id.vote_count)).setText("Votes for this video :\n"+(data.getJSONObject(1).getInt("votes")+1));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                findViewById(R.id.parent).setVisibility(View.VISIBLE);
                playagain.setEnabled(true);
                startover.setEnabled(true);
                panel_1.findViewById(R.id.vote_count).setVisibility(View.VISIBLE);
                panel_2.findViewById(R.id.vote_count).setVisibility(View.VISIBLE);
                panel_1.findViewById(R.id.owner_name).setVisibility(View.VISIBLE);
                panel_2.findViewById(R.id.owner_name).setVisibility(View.VISIBLE);
                panel_1.findViewById(R.id.vote_btn).setVisibility(View.INVISIBLE);
                panel_2.findViewById(R.id.vote_btn).setVisibility(View.INVISIBLE);

            }
        };
        startover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        playagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadVideos();
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
    private void loadVideos()
    {
        findViewById(R.id.parent).setVisibility(View.INVISIBLE);
        getClient().get(U("home/getVideo", id), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ServerResponse resp = new ServerResponse(responseBody) ;
                if(!resp.ok())
                {
                    toast(getBaseContext() , resp.getMessage());
                    finish();
                    return ;
                }
                JSONArray payload = (JSONArray) resp.getPayload();
                try {
                    setFor(panel_1,payload.getJSONObject(0));
                    setFor(panel_2,payload.getJSONObject(1));
                    data = payload;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    private View.OnClickListener voter ;

    private void setFor(View v , final JSONObject data)
    {
        try {
            ((TextView) v.findViewById(R.id.title)).setText(data.getString("title"));
            ((TextView) v.findViewById(R.id.vote_count)).setText("Votes for this video :\n"+data.getString("votes"));
            ((TextView) v.findViewById(R.id.owner_name)).setText("Video created by :\n"+data.getJSONObject("owner").getString("name"));
            (v.findViewById(R.id.owner_name)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        //open profile ..
                        JSONObject owner = data.getJSONObject("owner");
                        startActivity(new Intent(getBaseContext() , ProfileActivity.class).putExtra("username",
                                owner.getString("username"))
                                .putExtra("id" , owner.getString("id"))
                                .putExtra("name",owner.getString("name"))
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            ( v.findViewById(R.id.owner_name)).setVisibility(View.INVISIBLE);
            ( v.findViewById(R.id.vote_count)).setVisibility(View.INVISIBLE);
            (v.findViewById(R.id.vote_btn)).setVisibility(View.VISIBLE);
            //http://i3.ytimg.com/vi/PezHpWI_X9Q/maxresdefault.jpg
            String url = data.getString("url").split("\\?v=")[1];
            View img = v.findViewById(R.id.image);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        startActivity(new Intent(getBaseContext() , YoutubeLiveStream.class).putExtra("data",data.getString("url")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Picasso.get().load(String.format("http://i3.ytimg.com/vi/%s/maxresdefault.jpg",url)).into((ImageView) img);
            v.findViewById(R.id.vote_btn).setOnClickListener(voter);
            (v.findViewById(R.id.vote_btn)).setTag(data.getString("id"));
            v.setTag(data.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
