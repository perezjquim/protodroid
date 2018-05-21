package com.perezjquim.protodroid;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.perezjquim.protodroid.db.DatabaseManager;
import com.perezjquim.protodroid.view.ActionCardView;

import static com.perezjquim.UIHelper.askBinary;
import static com.perezjquim.UIHelper.askString;
import static com.perezjquim.UIHelper.toast;

public class ProjectActivity extends AppCompatActivity
{
    private int id;
    private String name;

    private static final int COLUMN_ID = 0;
    private static final int COLUMN_PROJECT_ID = 1;
    private static final int COLUMN_NAME = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        initProject();
        listPages();
    }

    private void initProject()
    {
        Intent i = getIntent();
        id = i.getIntExtra("id",-1);
        name = i.getStringExtra("name");
        TextView title = findViewById(R.id.title);
        title.setText(name);
    }

    private void listPages()
    {
        Cursor pages = DatabaseManager.getPages(id);
        LinearLayout pageListView = findViewById(R.id.pageList);
        for(int i = 0; pages.moveToNext(); i++)
        {
            final int id = pages.getInt(COLUMN_ID);
            final String name = pages.getString(COLUMN_NAME);

            final ActionCardView[] card = new ActionCardView[1];
            card[0] = new ActionCardView(this, name,
                    (v)->toast(this,"(page preview)"),
                    (v)->openPage(id,name),
                    (v)->deletePage(pageListView,card[0],id));
            pageListView.addView(card[0]);
        }
    }

    private void openPage(int id, String name)
    {
        Intent i = new Intent(this,PageActivity.class);
        i.putExtra("id",id);
        i.putExtra("name",name);
        startActivity(i);
    }

    public void addPage(View v)
    {
        askString(this,"Create a new page","Page name:",(response)->
        {
            DatabaseManager.insertPage(id,(String) response);
            toast(this,"Page created!");
            Intent i = new Intent(this,ProjectActivity.class);
            i.putExtra("id",id);
            i.putExtra("name",name);
            startActivity(i);
            this.finish();
        });
    }

    private void deletePage(LinearLayout list, ActionCardView card, int pageID)
    {
        askBinary(this,"Are you sure you want to delete this page?",null,()->
        {
            list.removeView(card);
            DatabaseManager.deletePage(id);
            toast(this,"Page deleted!");
        });
    }

}
