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
            "INSERT INTO " + PROJECT_TABLE +" (name) VALUES ('%name%')";
    private static final String SQL_DELETE_PROJECT =
            "DELETE FROM " + PROJECT_TABLE + " WHERE id='%id%'";

    private static final String PAGE_TABLE = "page";
    private static final String SQL_CREATE_PAGE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + PAGE_TABLE +
                    " ("+
                    "`id` INTEGER NOT NULL PRIMARY KEY," +
                    "`project_id` INTEGER NOT NULL," +
                    "`name` VARCHAR(45) NOT NULL," +
                    "`isMainPage` INT NOT NULL," +
                    "FOREIGN KEY (`project_id`) REFERENCES project(id) ON DELETE CASCADE "+
                    ")";
    private static final String SQL_GET_PAGES =
            "SELECT * FROM " + PAGE_TABLE + " WHERE project_id='%project_id%'";
    private static final String SQL_INSERT_PAGE =
            "INSERT INTO " + PAGE_TABLE +" (project_id,name,isMainPage) VALUES ('%project_id%','%name%','%isMainPage%')";
    private static final String SQL_DELETE_PAGE =
            "DELETE FROM " + PAGE_TABLE + " WHERE id='%id%'";
    private static final String SQL_GET_INDIVIDUAL_PAGE =
            "SELECT * FROM " + PAGE_TABLE + " WHERE id='%id%'";
    private static final String SQL_SELECT_MAIN_PAGE =
            "UPDATE " + PAGE_TABLE +" SET isMainPage='1' WHERE id='%id%'";
    private static final String SQL_SELECT_MAIN_PAGE_2 =
            "UPDATE " + PAGE_TABLE +" SET isMainPage='0' WHERE id!='%id%'";
    private static final String SQL_GET_MAIN_PAGE =
            "SELECT * FROM " + PAGE_TABLE + " WHERE project_id='%project_id%' AND isMainPage='1'";

    private static final String ELEMENT_TABLE = "element";
    private static final String SQL_CREATE_ELEMENT_TABLE =
            "CREATE TABLE IF NOT EXISTS " + ELEMENT_TABLE +
                    " ("+
                    "`id` INTEGER NOT NULL PRIMARY KEY," +
                    "`type` INTEGER NOT NULL," +
                    "`label` VARCHAR(45) NOT NULL," +
                    "`page_id` INTEGER NOT NULL," +
                    "`page_destination_id` INTEGER," +
                    "FOREIGN KEY (`page_destination_id`) REFERENCES page(id) ON DELETE SET NULL,"+
                    "FOREIGN KEY (`page_id`) REFERENCES page(id) ON DELETE CASCADE "+
                    ")";
    private static final String SQL_GET_ELEMENTS =
            "SELECT * FROM " + ELEMENT_TABLE + " WHERE page_id='%page_id%'";
    private static final String SQL_INSERT_ELEMENT =
             "INSERT INTO " + ELEMENT_TABLE +" (type,label,page_id,page_destination_id) VALUES ('%type%','%label%','%page_id%','%page_destination_id%')";
    private static final String SQL_UPDATE_ELEMENT =
            "UPDATE " + ELEMENT_TABLE +" SET type='%type%' , label='%label%' , page_destination_id='%page_destination_id%' WHERE id='%id%'";
    private static final String SQL_GET_INDIVIDUAL_ELEMENT =
            "SELECT * FROM " + ELEMENT_TABLE + " WHERE id='%id%'";
    private static final String SQL_DELETE_ELEMENT =
            "DELETE FROM " + ELEMENT_TABLE + " WHERE id='%id%'";


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
        return querySelect(SQL_GET_PROJECTS);
    }
    public static void insertProject(String name)
    {
        query(SQL_INSERT_PROJECT
                .replace("%name%", name));
    }
    public static void deleteProject(int projectID)
    {
        query(SQL_DELETE_PROJECT
                .replace("%project_id%",""+projectID));
    }
    public static Cursor getMainPage(int projectID)
    {
        return querySelect(SQL_GET_MAIN_PAGE
                .replace("%project_id%",""+projectID));
    }


    /* ********* */
    /* PAGES */
    /* ********* */
    public static Cursor getPages(int project_id)
    {
        // Obtém os markers
        return querySelect(SQL_GET_PAGES
                .replace("%project_id%",""+project_id));
    }
    public static void insertPage(int project_id,String name,boolean isMainPage)
    {
        query(SQL_INSERT_PAGE
                .replace("%project_id%",""+project_id)
                .replace("%name%",name)
                .replace("%isMainPage%", ""+Boolean.compare(isMainPage,false)));
    }
    public static void deletePage(int id)
    {
        query(SQL_DELETE_PAGE
                .replace("%id%",""+id));
    }
    public static Cursor getIndividualPage(int id)
    {
        Cursor c = querySelect(SQL_GET_INDIVIDUAL_PAGE
                .replace("%id%",""+id));
        c.moveToNext();
        return c;
    }
    public static void selectMainPage(int id)
    {
        queryInTransaction(
                SQL_SELECT_MAIN_PAGE
                    .replace("%id%",""+id),
                SQL_SELECT_MAIN_PAGE_2
                    .replace("%id%",""+id));
    }

    /* ************** */
    /* ELEMENTS */
    /* ************** */
    public static Cursor getElements(int page_id)
    {
        return querySelect(SQL_GET_ELEMENTS
                .replace("%page_id%",""+page_id));
    }
    public static void insertElement(int type,String label,int page_id,int page_destination_id)
    {
        query(SQL_INSERT_ELEMENT
                .replace("%type%",""+type)
                .replace("%label%",label)
                .replace("%page_id%",""+page_id)
                .replace("%page_destination_id%",""+page_destination_id)
                        .replace("'-1'","NULL"));
    }
    public static void updateElement(int type,String label,int id, int page_destination_id)
    {
        query(SQL_UPDATE_ELEMENT
                .replace("%type%",""+type)
                .replace("%label%",label)
                .replace("%id%",""+id)
                .replace("%page_destination_id%", ""+page_destination_id)
                        .replace("'-1'","NULL"));
    }
    public static void deleteElement(int id)
    {
        query(SQL_DELETE_ELEMENT
                .replace("%id%",""+id));
    }
    public static Cursor getIndividualElement(int id)
    {
        Cursor c = querySelect(SQL_GET_INDIVIDUAL_ELEMENT
                .replace("%id%",""+id));
        c.moveToNext();
        return c;
    }
}
