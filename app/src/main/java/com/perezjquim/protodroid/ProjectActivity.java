package com.perezjquim.protodroid;

import android.content.Intent;
import android.database.Cursor;
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

            CardView card = new CardView(this);
            LinearLayout content = new LinearLayout(this);

            TextView label = new TextView(this);
            label.setText(name);
            label.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,0.7f));

            ImageButton btnPlay = new ImageButton(this);
            btnPlay.setImageResource(android.R.drawable.ic_media_play);
            label.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,0.1f));

            ImageButton btnEdit = new ImageButton(this);
            btnEdit.setImageResource(android.R.drawable.ic_menu_edit);
            label.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,0.1f));

            ImageButton btnDelete = new ImageButton(this);
            btnDelete.setImageResource(android.R.drawable.ic_menu_delete);
            label.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,0.1f));

            content.addView(label);
            content.addView(btnPlay);
            content.addView(btnEdit);
            content.addView(btnDelete);

            card.setContentPadding(10,50,10,50);
            card.addView(content);
            pageListView.addView(card);
        }
    }

    public void addPage(View v)
    {

    }

}
