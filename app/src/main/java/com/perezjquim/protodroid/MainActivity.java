package com.perezjquim.protodroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.perezjquim.PermissionChecker;
import com.perezjquim.protodroid.db.DatabaseManager;
import com.perezjquim.protodroid.view.ActionCardView;

import static com.perezjquim.UIHelper.toast;

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
        for(int i = 0; projects.moveToNext(); i++)
        {
            final int id = projects.getInt(0);
            final String name = projects.getString(1);

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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Create a new project");
        alertDialog.setMessage("Project name:");

        final EditText input = new EditText(this);
        input.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Confirm",
                (dialog, which) ->
                {
                    String name = ""+input.getText();
                    DatabaseManager.insertProject(name);
                    startActivity(new Intent(this,MainActivity.class));
                    this.finish();
                });

        alertDialog.setNegativeButton("Cancel",
                (dialog, which) -> dialog.cancel());

        alertDialog.show();
    }

    private void deleteProject(LinearLayout list, ActionCardView card, int projectID)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Are you sure you want to delete this project?");
        alertDialog.setPositiveButton("Yes",
                (dialog, which) ->
                {
                    list.removeView(card);
                    DatabaseManager.deleteProject(projectID);
                    toast(this,"Project deleted!");
                });
        alertDialog.setNegativeButton("No",
                (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }
}
