package com.perezjquim.protodroid;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.perezjquim.protodroid.db.DatabaseManager;
import com.perezjquim.protodroid.view.ActionCardView;

public class PreviewActivity extends AppCompatActivity
{
    private int id;
    private String name;

    private static final int COLUMN_ID = 0;
    private static final int COLUMN_TYPE = 1;
    private static final int COLUMN_LABEL = 2;
    private static final int COLUMN_CONFIG = 3;
    private static final int COLUMN_PAGE_ID = 4;
    private static final int COLUMN_PAGE_DESTINATION_ID = 5;


    private static final int TYPE_BUTTON = 0;
    private static final int TYPE_CHECKBOX = 1;
    private static final int TYPE_SWITCH = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        initPage();
        listElements();
    }

    private void initPage()
    {
        Intent i = getIntent();
        id = i.getIntExtra("id",-1);
        name = i.getStringExtra("name");
        TextView title = findViewById(R.id.title);
        title.setText(name);
    }

    private void listElements()
    {
        Cursor elements = DatabaseManager.getElements(id);
        LinearLayout screen = findViewById(R.id.screen);
        while(elements.moveToNext())
        {
            final int type = elements.getInt(COLUMN_TYPE);
            final String label = elements.getString(COLUMN_LABEL);
            final int page_destination_id = elements.getInt(COLUMN_PAGE_DESTINATION_ID);
            final String config = elements.getString(COLUMN_CONFIG);

            switch(type)
            {
                case TYPE_BUTTON:
                    Button b = new Button(this);
                    b.setText(label);
                    screen.addView(b);
                    break;

                case TYPE_CHECKBOX:
                    CheckBox c = new CheckBox(this);
                    c.setText(label);
                    screen.addView(c);
                    break;

                case TYPE_SWITCH:
                    Switch s = new Switch(this);
                    s.setText(label);
                    screen.addView(s);
                    break;

                default:
                    break;
            }
        }
    }
}
