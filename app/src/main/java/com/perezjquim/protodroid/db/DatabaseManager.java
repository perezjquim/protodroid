package com.perezjquim.protodroid.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;

import java.io.File;

public abstract class DatabaseManager
{
    private static final String DB_NAME = "protodroid";

    private static final String PROJECT_TABLE = "project";
    private static final String SQL_CREATE_PROJECT_TABLE =
            "CREATE TABLE IF NOT EXISTS " + PROJECT_TABLE +
            " ("+
            "`id` INTEGER NOT NULL PRIMARY KEY," +
            "`name` VARCHAR(45) NOT NULL" +
            ")";

    private static final String PAGE_TABLE = "page";
    private static final String SQL_CREATE_PAGE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + PAGE_TABLE +
                    " ("+
                    "`id` INTEGER NOT NULL PRIMARY KEY," +
                    "`Project_id` INTEGER NOT NULL," +
                    "`name` VARCHAR(45) NOT NULL," +
                    "FOREIGN KEY (`Project_id`) REFERENCES Project(id)"+
                    ")";

    private static final String ELEMENT_TABLE = "element";
    private static final String SQL_CREATE_ELEMENT_TABLE =
            "CREATE TABLE IF NOT EXISTS " + ELEMENT_TABLE +
                    " ("+
                    "`id` INTEGER NOT NULL PRIMARY KEY," +
                    "`type` INTEGER NOT NULL," +
                    "`label` VARCHAR(45) NOT NULL," +
                    "`config` VARCHAR(45) NOT NULL," +
                    "`Page_id` INTEGER (45) NOT NULL," +
                    "`Page_destination_id` INTEGER," +
                    "FOREIGN KEY (`Page_destination_id`) REFERENCES Page(id),"+
                    "FOREIGN KEY (`Page_id`) REFERENCES Page(id)"+
                    ")";


    private static final String SQL_CLEAR_DB =
            "DELETE FROM " + PROJECT_TABLE + "," + PAGE_TABLE + "," + ELEMENT_TABLE;
    private static final String SQL_INSERT_PROJECT =
            "INSERT INTO " + PROJECT_TABLE +" (name) VALUES (?)";
    private static final String SQL_INSERT_PAGE =
            "INSERT INTO " + PAGE_TABLE +" (Project_id,name) VALUES (?,?)";
    private static final String SQL_INSERT_ELEMENT =
            "INSERT INTO " + ELEMENT_TABLE +" (type,label,config,Page_id,Page_destination_id) VALUES (?,?,?,?,?)";

    private static final String SQL_GET_PROJECTS =
            "SELECT id,name FROM " + PROJECT_TABLE;

    private static SQLiteDatabase db;

    public static void initDatabase()
    {
        try
        {
            File dbFolder = new File(Environment.getExternalStorageDirectory(), "/"+DB_NAME);
            if(!dbFolder.exists())
            {
                if (!dbFolder.mkdir())
                { throw new Exception("Could not create database folder"); }
            }
            File dbFile = new File(dbFolder, DB_NAME);
            if(!dbFile.exists())
            {
                dbFile.createNewFile();
            }
            db = SQLiteDatabase.openDatabase(
                    Environment.getExternalStorageDirectory() + "/"+DB_NAME+"/" + DB_NAME,
                    null,
                    SQLiteDatabase.CREATE_IF_NECESSARY);
            createDatabase();
        }
        catch(Exception e)
        { e.printStackTrace(); }
    }

    private static void createDatabase()
    {
        db.execSQL(SQL_CREATE_PROJECT_TABLE);
        db.execSQL(SQL_CREATE_PAGE_TABLE);
        db.execSQL(SQL_CREATE_ELEMENT_TABLE);
    }

    public static void clearDatabase()
    {
        db.execSQL(SQL_CLEAR_DB);
    }

    /*public static void insertMarker(String title, String subtitle, double latitude, double longitude)
    {
        try
        {
            insert(SQL_INSERT_MARKER,title,subtitle,""+latitude,""+longitude);
        }
        catch(InsertFailedException e)
        {
            e.printStackTrace();
        }
    }*/

    private static void beginTransaction()
    { db.beginTransaction(); }

    private static void insert(String sql, String ... args) throws InsertFailedException
    {
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindAllArgsAsStrings(args);
        long state = statement.executeInsert();
        if (state == -1)
            throw new InsertFailedException(args);
    }

    public static Cursor getProjects()
    {
    	// Obtém os markers
    	return db.rawQuery(SQL_GET_PROJECTS,null);
    }
}
