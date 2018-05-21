package com.perezjquim.protodroid;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.perezjquim.protodroid.db.DatabaseManager;
import com.perezjquim.protodroid.view.ActionCardView;

import static com.perezjquim.UIHelper.askBinary;
import static com.perezjquim.UIHelper.askString;
import static com.perezjquim.UIHelper.toast;

public class PageActivity extends AppCompatActivity
{
    private int id;
    private String name;

    private static final int COLUMN_ID = 0;
    private static final int COLUMN_TYPE = 1;
    private static final int COLUMN_LABEL = 2;
    private static final int COLUMN_PAGE_ID = 3;
    private static final int COLUMN_PAGE_DESTINATION_ID = 4;
    private static final int COLUMN_CONFIG = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
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
        LinearLayout elementListView = findViewById(R.id.elementList);
        for(int i = 0; elements.moveToNext(); i++)
        {
            final int id = elements.getInt(COLUMN_ID);
            final String name = elements.getString(COLUMN_LABEL);

            final ActionCardView[] card = new ActionCardView[1];
            card[0] = new ActionCardView(this, name,
                    null,
                    (v)->toast(this,"(element edit)"),
                    (v)->deleteElement(elementListView,card[0],id));
            elementListView.addView(card[0]);
        }
    }

    public void addElement(View v)
    {
        askString(this,"Create a new element","Label:",(response)->
        {
            DatabaseManager.insertElement(-1,(String)response,"ddd",id);
            Intent i = new Intent(this,PageActivity.class);
            i.putExtra("id",id);
            i.putExtra("name",name);
            startActivity(i);
            this.finish();
        });
    }

    private void deleteElement(LinearLayout list, ActionCardView card, int elementID)
    {
        askBinary(this,"Are you sure you want to delete this element?",null,()->
        {
            list.removeView(card);
            DatabaseManager.deleteElement(id);
            toast(this,"Element deleted!");
        });
    }

}
