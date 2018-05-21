package com.perezjquim.protodroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.perezjquim.protodroid.db.DatabaseManager;
import com.perezjquim.protodroid.view.ActionCardView;

import static com.perezjquim.UIHelper.toast;

public class ProjectActivity extends AppCompatActivity
{
    private int id;
    private String name;

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
            final int id = pages.getInt(0);
            final String name = pages.getString(1);

            final ActionCardView[] card = new ActionCardView[1];
            card[0] = new ActionCardView(this, name,
                    (v)->toast(this,"(page preview)"),
                    (v)->toast(this,"(page edit)"),
                    (v)->deletePage(pageListView,card[0],id));
            pageListView.addView(card[0]);
        }
    }

    public void addPage(View v)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Create a new page");
        alertDialog.setMessage("Page name:");

        final EditText input = new EditText(this);
        input.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Confirm",
                (dialog, which) ->
                {
                    String pageName = ""+input.getText();
                    DatabaseManager.insertPage(id,pageName);
                    Intent i = new Intent(this,ProjectActivity.class);
                    i.putExtra("id",id);
                    i.putExtra("name",name);
                    startActivity(i);
                    this.finish();
                });

        alertDialog.setNegativeButton("Cancel",
                (dialog, which) -> dialog.cancel());

        alertDialog.show();
    }

    private void deletePage(LinearLayout list, ActionCardView card, int pageID)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Are you sure you want to delete this page?");
        alertDialog.setPositiveButton("Yes",
                (dialog, which) ->
                {
                    list.removeView(card);
                    DatabaseManager.deletePage(id);
                    toast(this,"Page deleted!");
                });
        alertDialog.setNegativeButton("No",
                (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

}
