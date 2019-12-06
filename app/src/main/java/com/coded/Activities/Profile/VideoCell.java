package com.coded.Activities.Profile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.coded.R;

public class VideoCell extends RecyclerView.ViewHolder {
    TextView title , category ;
    ImageView img ;
    public VideoCell(View v, View.OnClickListener l)
    {
        super(v);
        title  = v.findViewById(R.id.title);
        category = v.findViewById(R.id.category);
        img = v.findViewById(R.id.image);
        v.setOnClickListener(l);
    }
    public void setTitle(String title)
    {
        this.title.setText(title);
    }
    public void setCategory(String cat)
    {
        this.category.setText(cat);
    }

}
