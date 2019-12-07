package com.coded.Activities.Profile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.coded.Activities.WishDetails;
import com.coded.Essens.ServerResponse;
import com.coded.Essens.User;
import com.coded.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.coded.Config.*;

public class WishAdapter extends RecyclerView.Adapter<WishVH> {
    JSONArray data ;
    View.OnClickListener opener;
    View.OnLongClickListener checker ;
    @Override
    public void onBindViewHolder(@NonNull WishVH wishVH, int i) {
        try {
            JSONObject d = data.getJSONObject(i);
            wishVH.setTitle(d.getString("title"));
            wishVH.itemView.setTag(i);
            if(d.getString("checked").equals("1"))
                wishVH.itemView.setAlpha(0.3f);
            else
                wishVH.itemView.setAlpha(1f);
            if(!d.isNull("image"))
                load(wishVH.img , d.getString("image"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void setData(JSONArray data)
    {
        this.data = data;
        notifyDataSetChanged();
    }
    public WishAdapter()
    {
        checker = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                String id = "";
                String uuid = "";
                try {
                    id = data.getJSONObject(((int) v.getTag())).getString("id");
                    uuid = data.getJSONObject(((int) v.getTag())).getString("owner");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getClient().post(U("user/check", uuid,id,User.from(v.getContext()).getName() ), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        ServerResponse response = new ServerResponse(responseBody);
                        if(response.ok())
                        {
                            JSONObject b = (JSONObject)response.getPayload();
                            try {
                                if(!b.getBoolean("res"))
                                {
                                    v.setAlpha(0.5f);
                                }else
                                {
                                    v.setAlpha(1f);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
                return false;
            }
        };
        opener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open the shit details
                try {
                    v.getContext().startActivity(new Intent(v.getContext() , WishDetails.class).putExtra("json",
                            data.getJSONObject((int)v.getTag()).toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    @NonNull
    @Override
    public WishVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new WishVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wish_cell , viewGroup,false),opener,checker);
    }

    @Override
    public int getItemCount() {
        return data == null? 0 : data.length();
    }
}
