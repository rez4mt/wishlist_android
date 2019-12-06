package com.coded.Activities.Taste;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.coded.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.coded.Config.load;

public class CatChooserAdapter extends RecyclerView.Adapter<CatVh> {
    JSONArray data ;
    View.OnClickListener opener;

    @Override
    public int getItemCount() {
        return data == null  ? 0 : data.length();
    }
    public void setData(JSONArray data)
    {
        this.data = data;
        notifyDataSetChanged();
    }
    public CatChooserAdapter()
    {
        opener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    v.getContext().startActivity(new Intent(v.getContext() ,VoteActivity.class).putExtra("id",
                            data.getJSONObject((int)v.getTag()).getString("id")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    @NonNull
    @Override
    public CatVh onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CatVh(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_cell , viewGroup,false),opener);
    }

    @Override
    public void onBindViewHolder(@NonNull CatVh catVh, int i) {
            try {
                JSONObject obj = data.getJSONObject(i);
                catVh.setTitle(obj.getString("name"));
                load(catVh.img , obj.getString("image"));
                catVh.itemView.setTag(i);
            } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
