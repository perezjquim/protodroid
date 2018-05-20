package com.perezjquim.protodroid;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.perezjquim.protodroid.db.DatabaseManager;
import com.perezjquim.protodroid.view.PageCardView;
import com.perezjquim.protodroid.view.ProjectCardView;

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
        while(pages.moveToNext())
        {
            final int id = pages.getInt(0);
            final String name = pages.getString(1);

            PageCardView card = new PageCardView(this, name,null,null,null);
            pageListView.addView(card);;
        }
    }

    public void addPage(View v)
    {

    }

}
