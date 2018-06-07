package com.perezjquim.protodroid;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.perezjquim.protodroid.db.DatabaseManager;
import com.perezjquim.protodroid.db.Page;
import com.perezjquim.protodroid.view.ActionCardView;

import static com.perezjquim.UIHelper.askBinary;
import static com.perezjquim.UIHelper.askString;
import static com.perezjquim.UIHelper.toast;

public class ProjectActivity extends AppCompatActivity
{
    private int project_id;
    private String project_name;

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
        project_id = i.getIntExtra("project_id",-1);
        project_name = i.getStringExtra("project_name");
        TextView title = findViewById(R.id.title);
        title.setText(project_name);
    }

    private void listPages()
    {
        Cursor pages = DatabaseManager.getPages(project_id);
        LinearLayout pageListView = findViewById(R.id.pageList);
        while(pages.moveToNext())
        {
            final int id = pages.getInt(Page.ID.ordinal());
            final String name = pages.getString(Page.NAME.ordinal());

            final ActionCardView[] card = new ActionCardView[1];
            card[0] = new ActionCardView(this, name,
                    (v)->previewPage(id,name),
                    (v)->openPage(id,name),
                    (v)->deletePage(pageListView,card[0],id));
            pageListView.addView(card[0]);
        }
    }

    private void openPage(int page_id, String page_name)
    {
        Intent i = new Intent(this,PageActivity.class);
        i.putExtra("project_id",project_id);
        i.putExtra("page_id",page_id);
        i.putExtra("page_name",page_name);
        startActivity(i);
    }

    private void previewPage(int page_id,String page_name)
    {
        Intent i = new Intent(this,PreviewActivity.class);
        i.putExtra("page_id",page_id);
        i.putExtra("page_name",page_name);
        startActivity(i);
    }

    public void addPage(View v)
    {
        askString(this,"Create a new page","Page name:",(response)->
        {
            DatabaseManager.insertPage(project_id,(String) response);
            toast(this,"Page created!");
            Intent i = new Intent(this,ProjectActivity.class);
            i.putExtra("project_id",project_id);
            i.putExtra("project_name",project_name);
            startActivity(i);
            this.finish();
        });
    }

    private void deletePage(LinearLayout list, ActionCardView card, int page_id)
    {
        askBinary(this,"Are you sure you want to delete this page?",null,()->
        {
            list.removeView(card);
            DatabaseManager.deletePage(page_id);
            toast(this,"Page deleted!");
        });
    }

}
