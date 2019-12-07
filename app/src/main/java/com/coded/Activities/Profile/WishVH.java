package com.coded.Activities.Profile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.coded.R;

public class WishVH extends RecyclerView.ViewHolder {
    ImageView img;
    TextView title;
    public WishVH(View v , View.OnClickListener l ,View.OnLongClickListener l2)
    {

        super(v);
        img = v.findViewById(R.id.image);
        title = v.findViewById(R.id.title);
        v.setOnClickListener(l);
        v.setOnLongClickListener(l2);
    }
    public void setTitle(String title)
    {
        this.title.setText(title);
    }
}
