package ness.com.etw.localDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import ness.com.etw.goals.model.AllGoalsDO;
import ness.com.etw.goals.model.GoalTypeDO;
import ness.com.etw.goals.model.GoalsDO;
import ness.com.etw.notes.model.NotesDO;


public class DatabaseManager extends SQLiteOpenHelper {

    private String TAG = "DatabaseManager";
    private SQLiteDatabase database;
    private static DatabaseManager databaseManager;
    private int responseType = 0;
    private Object extraParam;

    private DatabaseManager(Context context) {
        super(context, DBConstants.DB_NAME, null, DBConstants.DB_VERSION);
        WriteOnDatabase();
    }

    public void WriteOnDatabase() {
        this.database = getWritableDatabase();
    }

    public static DatabaseManager getDatabaseManager(Context _context) {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager(_context);
        }
        return databaseManager;
    }

    public void CloseDatabaseConnection() {
        database.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Creation of Tables in DB
        try {
            db.execSQL(DBConstants.CREATE_SQL_TABLE.CREATE_NOTES_TABLE);
            db.execSQL(DBConstants.CREATE_SQL_TABLE.CREATE_GOALS_TABLE);
            db.execSQL(DBConstants.CREATE_SQL_TABLE.CREATE_PLAN_TABLE);

        } catch (Exception e) {
            Log.d(TAG, "Exception " + e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Upgrade the new version of Database

    }


    //region Create Operations

    /*synchronized <T> void createBulkOps(List<T> objectsList) {

        database.beginTransaction();
        try {

            Object object = objectsList.get(0);

            //tblNotes
            if (object instanceof NotesDO) {
                //notesBulkInsert(objectsList);
            }

            //tblGoal
            if (object instanceof GoalTypeDO) {
                goalsBulkInsert(objectsList);
            }


            database.setTransactionSuccessful();

        } catch (Exception ex) {
            Log.d(TAG, "createBulkOps: " + ex);
        } finally {
            database.endTransaction();
            database.close();
        }
    }
*/
    @SuppressWarnings("unchecked")
    synchronized <T> void createBulkOps(Object... params) {

        database.beginTransaction();
        try {

            List<Object> objectsList = (List<Object>) params[1];
            responseType = (int) params[2];
            extraParam = params.length != 4 ? 0 : params[3];

            Object object = objectsList.get(0);

            if (object instanceof NotesDO) {
                notesBulkInsert(objectsList, (int) extraParam);
            }

            if (object instanceof GoalTypeDO) {
                goalsBulkInsert(objectsList);
            }


            database.setTransactionSuccessful();

        } catch (Exception ex) {
            Log.d(TAG, "createBulkOps: " + ex);
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    //endregion

    //region Update Operations

    public synchronized int updateOps(String table, ContentValues contentValues, String where, String[] whereArgs) {

        try {
            return database.update(table, contentValues, where, whereArgs);
        } catch (Exception e) {
            Log.d(TAG, "Exception " + e);
        }
        return 0;
    }

    //endregion

    //region  Retrieve Operation

    Cursor retrieveOps(boolean distinct, String tableName, String[] columnName, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {

        Cursor cursor = null;
        try {

            cursor = database.query(distinct, tableName, columnName, selection, selectionArgs, groupBy, having, orderBy, limit);

        } catch (Exception e) {
            Log.d(TAG, "Exception " + e);
        }
        return cursor;
    }

    public Cursor processMultiTableOps(String query, String[] selectionArgs) {

        Cursor cursor = null;
        try {
            cursor = database.rawQuery(query, selectionArgs);

        } catch (Exception ex) {
            Log.d(TAG, "processMultiTableOps: " + ex);
        }
        return cursor;

    }

    //endregion

    //region delete records Currently Not In Use

    synchronized boolean deleteRecords(String tableName, String where, String[] args) {
        boolean isDeleteSuccess = true;
        try {
            database.delete(tableName, where, args);
        } catch (Exception ex) {
            isDeleteSuccess = false;
            Log.d(TAG, "deleteRecords: " + tableName);
        }
        return isDeleteSuccess;
    }

    public synchronized boolean deleteMultipleRecords(String query) {
        boolean isDeleteSuccess = true;
        try {
            database.execSQL(query);
        } catch (Exception ex) {
            isDeleteSuccess = false;
            Log.d(TAG, "deleteRecords: ");
        }
        return isDeleteSuccess;
    }

    //endregion

    //region Bulk Insert operation


    private synchronized <T> void notesBulkInsert(List<T> objectsList, int isArchive) {

        ContentValueConstant contentValueConstant = new ContentValueConstant();

        for (int i = 0; i < objectsList.size(); i++) {
            NotesDO notesDO = NotesDO.class.cast(objectsList.get(i));
            ContentValues cv = contentValueConstant.insertNotesCV(notesDO, isArchive);
            database.insertWithOnConflict(DBConstants.Tables.TBL_NOTES, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    private synchronized <T> void goalsBulkInsert(List<T> objectsList) {

        ContentValueConstant contentValueConstant = new ContentValueConstant();
        GoalTypeDO goalTypeDO = (GoalTypeDO) objectsList.get(0);
        List<AllGoalsDO> goalsDOList = goalTypeDO.getAllGoals();

        for (int i = 0; i < goalsDOList.size(); i++) {
            AllGoalsDO allGoalsDO = AllGoalsDO.class.cast(goalsDOList.get(i));
            ContentValues cv = contentValueConstant.insertGoalCV(allGoalsDO);
            database.insertWithOnConflict(DBConstants.Tables.TBL_GOALS, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
            if (allGoalsDO.getPlan() != null) {
                ContentValues planCV = contentValueConstant.insertGoalPlanCV(allGoalsDO.getPlan(), String.valueOf(allGoalsDO.getId()));
                database.insertWithOnConflict(DBConstants.Tables.TBL_PLAN, null, planCV, SQLiteDatabase.CONFLICT_REPLACE);
            }
        }
    }


    //endregion
}
