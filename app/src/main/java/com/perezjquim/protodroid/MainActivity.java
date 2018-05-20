package com.perezjquim.protodroid;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.perezjquim.PermissionChecker;
import com.perezjquim.protodroid.db.DatabaseManager;
import com.perezjquim.protodroid.view.ProjectCardView;

public class MainActivity extends AppCompatActivity
{

    private PermissionChecker permissionChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initPermissionChecker();
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
                permissionChecker.restart();
                break;
        }
    }

    private void initPermissionChecker()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            permissionChecker = new PermissionChecker(this);
            permissionChecker.start();
        }
    }

    private void listProjects()
    {
        Cursor projects = DatabaseManager.getProjects();
        LinearLayout projectListView = findViewById(R.id.projectList);
        while(projects.moveToNext())
        {
            final int id = projects.getInt(0);
            final String name = projects.getString(1);

            ProjectCardView card = new ProjectCardView(this, name,
                    (v)-> openProject(id,name));
            projectListView.addView(card);
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

    }
}
