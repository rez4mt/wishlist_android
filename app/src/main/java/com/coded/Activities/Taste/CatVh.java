package com.coded.Activities.Taste;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.coded.R;

public class CatVh extends RecyclerView.ViewHolder {
    TextView title ;
    public ImageView img ;

    public CatVh(View v, View.OnClickListener opener)
    {
        super(v);
        title = v.findViewById(R.id.title);
        img = v.findViewById(R.id.img);
        v.setOnClickListener(opener);
    }
    public void setTitle(String text)
    {
        title.setText(text);
    }

}
