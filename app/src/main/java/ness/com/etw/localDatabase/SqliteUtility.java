package ness.com.etw.localDatabase;

import android.content.Context;


public class SqliteUtility {



    //delete record
    public static void deleteRecordInSqlite(Context context, String tableName, String where, String[] args, IDBCallback idbQueryCallbackListener, int responseType) {

        Object[] params = new Object[]{tableName, where, args, responseType};
        DeleteQueryAsyncTask deleteQueryAsyncTask = new DeleteQueryAsyncTask(context, idbQueryCallbackListener);
        deleteQueryAsyncTask.execute(params);
    }


    //retrieve record
    public static void retrieveRecordInSqlite(Context context, boolean distinct, String tableName, String[] columnName, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit, int responseType, IDBCallback idbQueryCallbackListener) {

        Object[] params = new Object[]{distinct, tableName, columnName, selection, selectionArgs, groupBy, having, orderBy, limit, responseType};
        RetrieveQueryAsyncTask queryAsyncTask = new RetrieveQueryAsyncTask(context, idbQueryCallbackListener);
        queryAsyncTask.execute(params);
    }

}
