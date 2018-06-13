package com.perezjquim.protodroid.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class ActionCardView extends CardView
{
    private LinearLayout content;
    private static final int PADDING_LEFT_RIGHT = 10;
    private static final int PADDING_TOP_BOTTOM = 50;

    public ActionCardView(Context context, AttributeSet attrs, String label, OnClickListener actionPlay, OnClickListener actionEdit, OnClickListener actionDelete)
    {
        super(context, attrs);
        init(context,label,actionPlay,actionEdit,actionDelete);
    }

    public ActionCardView(Context context, String label, OnClickListener actionPlay, OnClickListener actionEdit, OnClickListener actionDelete)
    {
        super(context);
        init(context,label,actionPlay,actionEdit,actionDelete);
    }

    public ActionCardView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context,"None",null,null,null);
    }
    public ActionCardView(Context context)
    {
        super(context);
        init(context,"None",null,null,null);
    }

    private void init(Context c,String name,OnClickListener actionPlay, OnClickListener actionEdit, OnClickListener actionDelete)
    {
        this.setContentPadding(PADDING_LEFT_RIGHT,PADDING_TOP_BOTTOM,PADDING_LEFT_RIGHT,PADDING_TOP_BOTTOM);
        content = new LinearLayout(c);
        initLabel(c,name);
        initButtons(c,name,actionPlay,actionEdit,actionDelete);
        this.addView(content);
    }

    private void initButtons(Context c,String name,OnClickListener actionPlay, OnClickListener actionEdit, OnClickListener actionDelete)
    {
        if(actionPlay != null)
        {
            ImageButton btnPlay = new ImageButton(c);
            btnPlay.setOnClickListener(actionPlay);
            btnPlay.setImageResource(android.R.drawable.ic_media_play);
            btnPlay.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,0.3f));
            content.addView(btnPlay);
        }

        if(actionEdit != null)
        {
            ImageButton btnEdit = new ImageButton(c);
            btnEdit.setOnClickListener(actionEdit);
            btnEdit.setImageResource(android.R.drawable.ic_menu_edit);
            btnEdit.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,0.3f));
            content.addView(btnEdit);
        }

        if(actionDelete != null)
        {
            ImageButton btnDelete = new ImageButton(c);
            btnDelete.setOnClickListener(actionDelete);
            btnDelete.setImageResource(android.R.drawable.ic_menu_delete);
            btnDelete.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,0.3f));
            content.addView(btnDelete);
        }
    }

    private void initLabel(Context c,String name)
    {
        TextView txtLabel = new TextView(c);
        txtLabel.setText(name);
        txtLabel.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,0.15f));
        content.addView(txtLabel);
    }
}
