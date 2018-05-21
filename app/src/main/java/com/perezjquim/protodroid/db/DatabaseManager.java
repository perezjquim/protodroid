package com.perezjquim.protodroid.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public abstract class DatabaseManager
{
    private static SQLiteDatabase db;
    private static final String DB_NAME = "protodroid";

    private static final String PROJECT_TABLE = "project";
    private static final String SQL_CREATE_PROJECT_TABLE =
            "CREATE TABLE IF NOT EXISTS " + PROJECT_TABLE +
            " ("+
            "`id` INTEGER NOT NULL PRIMARY KEY," +
            "`name` VARCHAR(45) NOT NULL" +
            ")";
    private static final String SQL_GET_PROJECTS =
            "SELECT id,name FROM " + PROJECT_TABLE;
    private static final String SQL_INSERT_PROJECT =
            "INSERT INTO " + PROJECT_TABLE +" (name) VALUES ('1')";
    private static final String SQL_DELETE_PROJECT =
            "DELETE FROM " + PROJECT_TABLE + " WHERE id='1'";

    private static final String PAGE_TABLE = "page";
    private static final String SQL_CREATE_PAGE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + PAGE_TABLE +
                    " ("+
                    "`id` INTEGER NOT NULL PRIMARY KEY," +
                    "`Project_id` INTEGER NOT NULL," +
                    "`name` VARCHAR(45) NOT NULL," +
                    "FOREIGN KEY (`Project_id`) REFERENCES Project(id) ON DELETE CASCADE "+
                    ")";
    private static final String SQL_GET_PAGES =
            "SELECT id,name FROM " + PAGE_TABLE + " WHERE Project_id='1'";
    private static final String SQL_INSERT_PAGE =
            "INSERT INTO " + PAGE_TABLE +" (Project_id,name) VALUES ('1','2')";
    private static final String SQL_DELETE_PAGE =
            "DELETE FROM " + PAGE_TABLE + " WHERE id='1'";

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
                    "FOREIGN KEY (`Page_destination_id`) REFERENCES Page(id) ON DELETE CASCADE,"+
                    "FOREIGN KEY (`Page_id`) REFERENCES Page(id) ON DELETE CASCADE "+
                    ")";
    private static final String SQL_INSERT_ELEMENT =
            "INSERT INTO " + ELEMENT_TABLE +" (type,label,config,Page_id,Page_destination_id) VALUES ('1','2','3','4','5')";


    private static final String SQL_CLEAR_DB =
            "DELETE FROM " + PROJECT_TABLE + "," + PAGE_TABLE + "," + ELEMENT_TABLE;
    private static final String SQL_ENABLE_FOREIGN_KEYS =
            "PRAGMA foreign_keys=ON;";

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

    private static Cursor querySelect(String s)
    {
        Log.e("-- Executing query --",s);
        return db.rawQuery(s,null);
    }

    private static void query(String s)
    {
        Log.e("-- Executing query --",s);
        db.execSQL(s);
    }

    private static void queryInTransaction(String ... queries)
    {
        db.beginTransaction();
        try
        {
            for(String q : queries)
            {
                query(q);
            }
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.endTransaction();
        }
    }

    private static void createDatabase()
    {
        query(SQL_ENABLE_FOREIGN_KEYS);
        queryInTransaction(SQL_CREATE_PROJECT_TABLE,
                                             SQL_CREATE_PAGE_TABLE,
                                             SQL_CREATE_ELEMENT_TABLE);
    }

    public static void clearDatabase()
    {
        query(SQL_CLEAR_DB);
    }

    /* ************* */
    /* PROJECTS */
    /* ************* */
    public static Cursor getProjects()
    {
        // Obtém os markers
        return querySelect(SQL_GET_PROJECTS);
    }
    public static void insertProject(String name)
    {
        query(SQL_INSERT_PROJECT
                .replace("1", name));
    }
    public static void deleteProject(int projectID)
    {
        query(SQL_DELETE_PROJECT
                .replace("1",""+projectID));
    }


    /* ********* */
    /* PAGES */
    /* ********* */
    public static Cursor getPages(int projectID)
    {
        // Obtém os markers
        return querySelect(SQL_GET_PAGES
                .replace("1",""+projectID));
    }
    public static void insertPage (int projectID,String pageName)
    {
        query(SQL_INSERT_PAGE
                .replace("1",""+projectID)
                .replace("2",pageName));
    }
    public static void deletePage(int pageID)
    {
        query(SQL_DELETE_PAGE
                .replace("1",""+pageID));
    }
}
