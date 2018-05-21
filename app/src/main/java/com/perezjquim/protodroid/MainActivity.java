package com.perezjquim.protodroid;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.perezjquim.PermissionChecker;
import com.perezjquim.protodroid.db.DatabaseManager;
import com.perezjquim.protodroid.view.ActionCardView;

import static com.perezjquim.UIHelper.askBinary;
import static com.perezjquim.UIHelper.askString;
import static com.perezjquim.UIHelper.toast;

public class MainActivity extends AppCompatActivity
{
    private static final int COLUMN_ID = 0;
    private static final int COLUMN_NAME = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        PermissionChecker.init(this);
        DatabaseManager.initDatabase();
        setContentView(R.layout.activity_main);
        listProjects();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case PermissionChecker.REQUEST_CODE:
                PermissionChecker.restart();
                break;
        }
    }

    private void listProjects()
    {
        Cursor projects = DatabaseManager.getProjects();
        LinearLayout projectListView = findViewById(R.id.projectList);
        for(int i = 0; projects.moveToNext(); i++)
        {
            final int id = projects.getInt(COLUMN_ID);
            final String name = projects.getString(COLUMN_NAME);

            final ActionCardView[] card = new ActionCardView[1];
            card[0] = new ActionCardView(this, name,
                    (v)->toast(this,"(project preview)"),
                    (v)->openProject(id,name),
                    (v)->deleteProject(projectListView,card[0],id));
            projectListView.addView(card[0]);
        }
    }

    private void openProject(int id, String name)
    {
        Intent i = new Intent(this,ProjectActivity.class);
        i.putExtra("id",id);
        i.putExtra("name",name);
        startActivity(i);
    }

    public void addProject(View v)
    {
        askString(this,"Create a new project","Project name:",(response)->
        {
            DatabaseManager.insertProject((String) response);
            startActivity(new Intent(this,MainActivity.class));
            this.finish();
        });
    }

    private void deleteProject(LinearLayout list, ActionCardView card, int projectID)
    {
        askBinary(this,"Are you sure you want to delete this project?",null,()->
        {
            list.removeView(card);
            DatabaseManager.deleteProject(projectID);
            toast(this,"Project deleted!");
        });
    }
}
