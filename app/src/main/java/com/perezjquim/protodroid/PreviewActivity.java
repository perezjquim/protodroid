package com.perezjquim.protodroid;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.perezjquim.protodroid.db.DatabaseManager;
import com.perezjquim.protodroid.db.Element;
import com.perezjquim.protodroid.db.Page;

public class PreviewActivity extends AppCompatActivity
{
    private int page_id;
    private String page_name;

    private static final int TYPE_BUTTON = 0;
    private static final int TYPE_CHECKBOX = 1;
    private static final int TYPE_SWITCH = 2;
    private static final int TYPE_FIELD = 3;

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
        page_id = i.getIntExtra("page_id",-1);
        page_name = i.getStringExtra("page_name");
        TextView title = findViewById(R.id.title);
        title.setText(page_name);
    }

    private void listElements()
    {
        Cursor elements = DatabaseManager.getElements(page_id);
        LinearLayout screen = findViewById(R.id.screen);
        while(elements.moveToNext())
        {
            final int type = elements.getInt(Element.TYPE.index);
            final String label = elements.getString(Element.LABEL.index);
            final int page_destination_id = elements.isNull(Element.PAGE_DESTINATION_ID.index) ? -1 :
                    elements.getInt(Element.PAGE_DESTINATION_ID.index);
            final String config = elements.getString(Element.CONFIG.index);

            switch(type)
            {
                case TYPE_BUTTON:
                    Button b = new Button(this);
                    b.setText(label);
                    if(page_destination_id != -1)
                    {
                        b.setOnClickListener((v)->
                                jumpToPage(page_destination_id));
                    }
                    screen.addView(b);
                    break;

                case TYPE_CHECKBOX:
                    CheckBox c = new CheckBox(this);
                    c.setText(label);
                    if(page_destination_id != -1)
                    {
                        c.setOnClickListener((v)->
                                jumpToPage(page_destination_id));
                    }
                    screen.addView(c);
                    break;

                case TYPE_SWITCH:
                    Switch s = new Switch(this);
                    s.setText(label);
                    if(page_destination_id != -1)
                    {
                        s.setOnClickListener((v)->
                                jumpToPage(page_destination_id));
                    }
                    screen.addView(s);
                    break;

                case TYPE_FIELD:
                    TextView t = new TextView(this);
                    t.setText(label);
                    EditText e = new EditText(this);
                    screen.addView(t);
                    screen.addView(e);
                    break;

                default:
                    break;
            }
        }
    }

    private void jumpToPage(int page_destination_id)
    {
        Intent i = new Intent(this,PreviewActivity.class);
        i.putExtra("page_id",page_destination_id);
        i.putExtra("page_name",DatabaseManager
                .getIndividualPage(page_destination_id)
                .getString(Page.NAME.index));
        startActivity(i);
    }
}
