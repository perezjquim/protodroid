package com.perezjquim.protodroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProjectActivity extends AppCompatActivity
{
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Intent i = getIntent();
        id = i.getIntExtra("id",-1);
        String name = i.getStringExtra("name");
        TextView title = findViewById(R.id.title);
        title.setText(name);
    }
}
