package com.coded.Activities.Profile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.coded.Activities.YoutubeLiveStream;
import com.coded.R;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.coded.Config.load;

public class VideoAdapter extends RecyclerView.Adapter<VideoCell> {
    JSONArray data ;
    View.OnClickListener opener ;

    public VideoAdapter()
    {
        opener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id =(int) v.getTag();
                try {
                    v.getContext().startActivity(new Intent(v.getContext() , YoutubeLiveStream.class)
                    .putExtra("data",data.getJSONObject(id).getString("url"))
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    @NonNull
    @Override
    public VideoCell onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VideoCell(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.video_cell,viewGroup,false),opener);
    }
    public void setData(JSONArray data)
    {
        this.data = data;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull VideoCell videoCell, int i) {
        try {
            JSONObject d = data.getJSONObject(i);
            videoCell.setCategory(d.getString("category_name"));
            videoCell.setTitle(d.getString("title"));
            videoCell.itemView.setTag(i);
            String url = d.getString("url").split("\\?v=")[1];
            Picasso.get().load(String.format("http://i3.ytimg.com/vi/%s/maxresdefault.jpg",url)).into(videoCell.img);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data == null? 0 : data.length();
    }
}
