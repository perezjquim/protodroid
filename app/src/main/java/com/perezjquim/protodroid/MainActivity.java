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
            CardView card = new CardView(this);
            TextView label = new TextView(this);
            label.setText(projects.getString(1));
            card.setContentPadding(50,50,50,50);
            card.addView(label);
            projectListView.addView(card);
        }
    }

    public void addProject(View v)
    {

    }
}
