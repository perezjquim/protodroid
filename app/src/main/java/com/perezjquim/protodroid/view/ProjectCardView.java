package com.perezjquim.protodroid.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.TextView;

public class ProjectCardView extends CardView
{
    private TextView txtLabel;
    private static final int PADDING_LEFT_RIGHT = 10;
    private static final int PADDING_TOP_BOTTOM = 50;

    public ProjectCardView(Context context, AttributeSet attrs, String label, OnClickListener action)
    {
        super(context, attrs);
        init(context,label,action);
    }

    public ProjectCardView(Context context, String label, OnClickListener action)
    {
        super(context);
        init(context,label,action);
    }

    public ProjectCardView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context,"None",null);
    }

    public ProjectCardView(Context context)
    {
        super(context);
        init(context,"None",null);
    }

    private void init(Context c,String label,OnClickListener action)
    {
        this.setContentPadding(PADDING_LEFT_RIGHT,PADDING_TOP_BOTTOM,PADDING_LEFT_RIGHT,PADDING_TOP_BOTTOM);
        this.setOnClickListener(action);
        initLabel(c,label);
    }

    private void initLabel(Context c,String label)
    {
        txtLabel = new TextView(c);
        txtLabel.setText(label);
        this.addView(txtLabel);
    }
}
