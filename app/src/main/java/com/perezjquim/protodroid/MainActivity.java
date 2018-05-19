package com.perezjquim.protodroid;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
    }

    private void initPermissionChecker()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            permissionChecker = new PermissionChecker(this);
            permissionChecker.start();
        }
    }
}
