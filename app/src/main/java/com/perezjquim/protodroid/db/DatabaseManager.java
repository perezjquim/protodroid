package com.perezjquim.protodroid.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
            "SELECT * FROM " + PROJECT_TABLE;
    private static final String SQL_INSERT_PROJECT =
            "INSERT INTO " + PROJECT_TABLE +" (name) VALUES ('_name')";
    private static final String SQL_DELETE_PROJECT =
            "DELETE FROM " + PROJECT_TABLE + " WHERE id='_id'";

    private static final String PAGE_TABLE = "page";
    private static final String SQL_CREATE_PAGE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + PAGE_TABLE +
                    " ("+
                    "`id` INTEGER NOT NULL PRIMARY KEY," +
                    "`project_id` INTEGER NOT NULL," +
                    "`name` VARCHAR(45) NOT NULL," +
                    "FOREIGN KEY (`project_id`) REFERENCES project(id) ON DELETE CASCADE "+
                    ")";
    private static final String SQL_GET_PAGES =
            "SELECT * FROM " + PAGE_TABLE + " WHERE project_id='_project_id'";
    private static final String SQL_INSERT_PAGE =
            "INSERT INTO " + PAGE_TABLE +" (project_id,name) VALUES ('_project_id','_name')";
    private static final String SQL_DELETE_PAGE =
            "DELETE FROM " + PAGE_TABLE + " WHERE id='_id'";

    private static final String ELEMENT_TABLE = "element";
    private static final String SQL_CREATE_ELEMENT_TABLE =
            "CREATE TABLE IF NOT EXISTS " + ELEMENT_TABLE +
                    " ("+
                    "`id` INTEGER NOT NULL PRIMARY KEY," +
                    "`type` INTEGER NOT NULL," +
                    "`label` VARCHAR(45) NOT NULL," +
                    "`config` VARCHAR(45)," +
                    "`page_id` INTEGER NOT NULL," +
                    "`page_destination_id` INTEGER," +
                    "FOREIGN KEY (`page_destination_id`) REFERENCES page(id) ON DELETE SET NULL,"+
                    "FOREIGN KEY (`page_id`) REFERENCES page(id) ON DELETE CASCADE "+
                    ")";
    private static final String SQL_GET_ELEMENTS =
            "SELECT * FROM " + ELEMENT_TABLE + " WHERE page_id='_page_id'";
    private static final String SQL_INSERT_ELEMENT =
            "INSERT INTO " + ELEMENT_TABLE +" (type,label,config,page_id) VALUES ('_type','_label','_config','_page_id')";
    private static final String SQL_INSERT_ELEMENT_WITH_DESTINATION =
            "INSERT INTO " + ELEMENT_TABLE +" (type,label,config,page_id,page_destination_id) VALUES ('_type','_label','_config','_page_id','_page_destination_id`)";
    private static final String SQL_UPDATE_ELEMENT =
            "UPDATE " + ELEMENT_TABLE +" SET type='_type' , label='_label' , config='_config' WHERE id='_id'";
    private static final String SQL_GET_INDIVIDUAL_ELEMENT =
            "SELECT * FROM " + ELEMENT_TABLE + " WHERE id='_id'";
    private static final String SQL_DELETE_ELEMENT =
            "DELETE FROM " + ELEMENT_TABLE + " WHERE id='_id'";


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
                .replace("_name", name));
    }
    public static void deleteProject(int projectID)
    {
        query(SQL_DELETE_PROJECT
                .replace("_project_id",""+projectID));
    }


    /* ********* */
    /* PAGES */
    /* ********* */
    public static Cursor getPages(int project_id)
    {
        // Obtém os markers
        return querySelect(SQL_GET_PAGES
                .replace("_project_id",""+project_id));
    }
    public static void insertPage (int project_id,String name)
    {
        query(SQL_INSERT_PAGE
                .replace("_project_id",""+project_id)
                .replace("_name",name));
    }
    public static void deletePage(int id)
    {
        query(SQL_DELETE_PAGE
                .replace("_id",""+id));
    }

    /* ************** */
    /* ELEMENTS */
    /* ************** */
    public static Cursor getElements(int page_id)
    {
        // Obtém os markers
        return querySelect(SQL_GET_ELEMENTS
                .replace("_page_id",""+page_id));
    }
    public static void insertElement(int type,String label,String config,int page_id)
    {
        query(SQL_INSERT_ELEMENT
                .replace("_type",""+type)
                .replace("_label",label)
                .replace("_config",config)
                .replace("_page_id",""+page_id));
    }
    public static void insertElement(int type,String label,String config,int page_id,int page_destination_id)
    {
        query(SQL_INSERT_ELEMENT
                .replace("_type",""+type)
                .replace("_label",label)
                .replace("_config",config)
                .replace("_page_id",""+page_id)
                .replace("_page_destination_id",""+page_destination_id));
    }
    public static void updateElement(int type,String label,String config, int id)
    {
        query(SQL_UPDATE_ELEMENT
                .replace("_type",""+type)
                .replace("_label",label)
                .replace("_config",config)
                .replace("_id",""+id));
    }
    public static void deleteElement(int id)
    {
        query(SQL_DELETE_ELEMENT
                .replace("_id",""+id));
    }
    public static Cursor getIndividualElement(int id)
    {
        Cursor c = querySelect(SQL_GET_INDIVIDUAL_ELEMENT
                .replace("_id",""+id));
        c.moveToNext();
        return c;
    }
}
