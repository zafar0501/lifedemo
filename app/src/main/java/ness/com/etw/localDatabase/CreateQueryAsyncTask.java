package ness.com.etw.localDatabase;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

public class CreateQueryAsyncTask extends AsyncTask<Object, Void, Long> {

    private Context context;
    private DatabaseManager databaseManager;
    private IDBCallback queryCallback;
    private int responseType = 0;
    private Object extraParam;

    public CreateQueryAsyncTask(Context _context, IDBCallback _queryCallback) {
        this.context = _context;
        databaseManager = DatabaseManager.getDatabaseManager(_context);
        queryCallback = _queryCallback;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Long doInBackground(Object... params) {

        long insertRowValue = 0;
        databaseManager.WriteOnDatabase();
        try {

            //Takes 3 parameters
            // [0] = tableName, [1] = List, [2]=responseType, [3]=extra parameters
            //String tableName = (String) params[0];
           /* List<Object> objectList = (List<Object>) params[1];

            extraParam = params[3];*/
            //databaseManager.createBulkOps(objectList, extraParam);

            responseType = (int) params[2];
            databaseManager.createBulkOps(params);

        } catch (Exception e) {
            String TAG = CreateQueryAsyncTask.class.getSimpleName();
            Log.d(TAG, "Exception " + e);
        }
        return insertRowValue;
    }

    @Override
    protected void onPostExecute(Long result) {
        queryCallback.onCreateQuerySuccess(result, responseType);
    }
}

