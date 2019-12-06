package com.coded.Essens;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.coded.R;

public class Progress {

    private View view;
    private TextView msg;
    private ProgressBar progressBar;
    private LinearLayout ll;
    private AlertDialog.Builder builder;
    private Dialog dialog;

    public Progress(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.progress, null);
        init();
    }

    private void init() {
        msg = view.findViewById(R.id.msg);
        progressBar = view.findViewById(R.id.loader);
        ll = view.findViewById(R.id.ll);
        builder = new AlertDialog.Builder(view.getContext());
        builder.setCancelable(false);
        setBackgroundColor(Color.WHITE);
        setProgressColor(Color.BLACK);
        setMessageColor(Color.BLACK);
    }

    public void light(String message) {
        setBackgroundColor(Color.WHITE);
        setProgressColor(Color.BLACK);
        setMessageColor(Color.BLACK);
        setMessage(message);
        show();
    }

    public void dark(String message) {
        setBackgroundColor(Color.BLACK);
        setProgressColor(Color.WHITE);
        setMessageColor(Color.WHITE);
        setMessage(message);
        show();
    }

    public Progress setBackgroundDrawable(Drawable drawable) {
        ll.setBackground(drawable);
        ll.setPadding(30, 30, 30, 30);
        return this;
    }

    public Progress setBackgroundColor(int color) {
        ll.setBackgroundColor(color);
        return this;
    }

    public Progress setProgressColor(int color) {
        progressBar.setIndeterminateTintList(ColorStateList.valueOf(color));
        return this;
    }

    public Progress setMessage(String message) {
        msg.setText(message);
        return this;
    }

    public Progress setMessageColor(int color) {
        msg.setTextColor(color);
        return this;
    }

    public void show() {
        if(view.getParent() == null)
        {
            builder.setView(view);
            dialog = builder.create();
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}