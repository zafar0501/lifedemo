package ness.com.etw.goals.presenter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ness.com.etw.common.constant.StringConstant;
import ness.com.etw.common.presenter.IResultView;
import ness.com.etw.common.validationMessage.AppsValidationMessage;
import ness.com.etw.goals.model.GoalTypeDO;
import ness.com.etw.goals.model.GoalsDO;
import ness.com.etw.localDatabase.CreateQueryAsyncTask;
import ness.com.etw.localDatabase.DBConstants;
import ness.com.etw.localDatabase.IDBCallback;
import ness.com.etw.localDatabase.MultiTableQueryAsyncTask;
import ness.com.etw.localDatabase.SqliteUtility;
import ness.com.etw.network.IEndPointsAPI;
import ness.com.etw.network.INetworkResult;
import ness.com.etw.network.NetworkClient;
import ness.com.etw.network.NetworkConstants;
import ness.com.etw.network.NetworkHandler;
import ness.com.etw.utility.HelperUtility;
import ness.com.etw.utility.PreferenceUtility;
import retrofit2.Call;

public class GoalsListViewImpl implements INetworkResult, IDBCallback {

    private static final String TAG = GoalsListViewImpl.class.getSimpleName();
    private Context context;
    private IEndPointsAPI endPointsAPI;
    private NetworkHandler networkHandler;
    private IResultView.ILoadMoreResult iResultViewListener;
    private boolean isPullToRefresh;
    private List<GoalsDO> goalsDOList = new ArrayList<>();

    public GoalsListViewImpl(Context context, IResultView.ILoadMoreResult iResultViewListener) {
        this.context = context;
        this.iResultViewListener = iResultViewListener;
        endPointsAPI = NetworkClient.getClient().create(IEndPointsAPI.class);
        networkHandler = new NetworkHandler(this);
    }

    //region SQLite

    private void insertNotesInDB(List<GoalTypeDO> goalTypeDO, int responseType) {

        Object[] params = new Object[]{DBConstants.Tables.TBL_GOALS, goalTypeDO, responseType};
        CreateQueryAsyncTask createQueryAsyncTask = new CreateQueryAsyncTask(context, this);
        createQueryAsyncTask.execute(params);
    }


    public void retrieveNonCustomGoalsListFromDB() {

        MultiTableQueryAsyncTask multiTableQueryAsyncTask = new MultiTableQueryAsyncTask(context, this);
        Object[] params;
        String strOrgQuery = nonCustomGoalQuery();
        params = new Object[]{strOrgQuery, null, NetworkConstants.RESPONSE_TYPE.RETRIEVE_TYPE_2};
        multiTableQueryAsyncTask.execute(params);

    }

    private void retrieveCustomGoalsListFromDB() {

        MultiTableQueryAsyncTask multiTableQueryAsyncTask = new MultiTableQueryAsyncTask(context, this);
        Object[] params;
        String strOrgQuery = customGoalQuery();
        params = new Object[]{strOrgQuery, null, NetworkConstants.RESPONSE_TYPE.RETRIEVE_TYPE_3};
        multiTableQueryAsyncTask.execute(params);

    }

    //Performance / Role
    private String nonCustomGoalQuery() {

        return retrieveGoalQuery()
                + " WHERE tbg." + DBConstants.Columns.GOAL_TYPE_HEADER_ID + " <> 13"
                + " ORDER BY tbg." + DBConstants.Columns.GOAL_TYPE_HEADER_ID + " , tbg." + DBConstants.Columns.GOAL_TYPE_ID
                + " , tbg." + DBConstants.Columns.STATE_STATUS + " , tbg." + DBConstants.Columns.TITLE;

    }


    // Custom Goals
    private String customGoalQuery() {

        return retrieveGoalQuery()
                + " WHERE tbg." + DBConstants.Columns.GOAL_TYPE_HEADER_ID + "= 13"
                + " ORDER BY tbg." + DBConstants.Columns.GROUP_SORT_ID;
        // + " , tbg." + DBConstants.Columns.STATE_STATUS + " , tbg." + DBConstants.Columns.TITLE;

    }


    private String retrieveGoalQuery() {

        return "SELECT DISTINCT tbg." + DBConstants.Columns.ID
                + " ,tbg." + DBConstants.Columns.GOAL_TYPE_HEADER_ID
                + " ,tbg." + DBConstants.Columns.GROUP_SORT_ID + " ,tbg." + DBConstants.Columns.GOAL_TYPE_ID
                + " ,tbg." + DBConstants.Columns.STATE_STATUS + " ,tbg." + DBConstants.Columns.GROUP_TITLE
                + " ,tbg." + DBConstants.Columns.TITLE + " ,tbg." + DBConstants.Columns.DUE_DATE
                + " ,tbg." + DBConstants.Columns.ALIGNMENT_GOAL_TYPE + " ,tbg." + DBConstants.Columns.PERFORMANCE_GOAL_TYPE
                + " ,tbg." + DBConstants.Columns.STATE + " ,tbg." + DBConstants.Columns.STATUS
                + " ,tbg." + DBConstants.Columns.ROLE_NAME + " ,tbp." + DBConstants.Columns.PLAN_TITLE
                + " FROM " + DBConstants.Tables.TBL_GOALS + " tbg LEFT OUTER JOIN " + DBConstants.Tables.TBL_PLAN + " tbp"
                + " ON tbg." + DBConstants.Columns.ID + " == tbp." + DBConstants.Columns.ID;

    }

    //endregion

    //region API

    @SuppressWarnings("unchecked")
    public void retrieveGoalListAPI(boolean isPullToRefresh, String authorId) {

        if (HelperUtility.InternetCheck(context)) {
            this.isPullToRefresh = isPullToRefresh;
            String contentType = StringConstant.ICommon.CONTENT_TYPE;
            String cookieAuth = StringConstant.ICommon.AUTHORIZATION + PreferenceUtility.getAuthToken();
            Log.d(TAG, "retrieveGoalListAPI: " + cookieAuth);
            Call<GoalTypeDO> goalTypeDOCall = endPointsAPI.goalTypeListAPI(contentType, cookieAuth, authorId);
            goalTypeDOCall.enqueue(networkHandler.EnqueueRequest(NetworkConstants.RESPONSE_TYPE.RETRIEVE_TYPE));
        } else {
            iResultViewListener.onDisplayMessage(AppsValidationMessage.ICommonError.NO_INTERNET);
        }

    }

    //endregion

    @SuppressWarnings("unchecked")
    @Override
    public void OnRetrieveQuerySuccess(Cursor cursor, int responseType) {


        switch (responseType) {
            case NetworkConstants.RESPONSE_TYPE.RETRIEVE_TYPE_2:
                //iResultViewListener.showResultList(parseDBResponse(cursor));
                goalsDOList = parseDBResponse(cursor);
                retrieveCustomGoalsListFromDB();
                break;
            case NetworkConstants.RESPONSE_TYPE.RETRIEVE_TYPE_3:
                goalsDOList.addAll(parseDBResponse(cursor));
                iResultViewListener.showResultList(goalsDOList);
                break;
        }
    }

    @Override
    public void onSuccess(Object responseBody, int responseType) {

        List<GoalTypeDO> goalTypeDOList = new ArrayList<>();
        GoalTypeDO goalTypeDO = (GoalTypeDO) responseBody;
        goalTypeDOList.add(goalTypeDO);

        switch (responseType) {

            case NetworkConstants.RESPONSE_TYPE.RETRIEVE_TYPE:

                if (isPullToRefresh) {
                    SqliteUtility.deleteRecordInSqlite(context, DBConstants.Tables.TBL_GOALS, null, null, this, NetworkConstants.RESPONSE_TYPE.DELETE_TYPE);

                }
                insertNotesInDB(goalTypeDOList, responseType);
        }

    }

    @Override
    public void onCreateQuerySuccess(long id, int responseType) {
        switch (responseType) {
            case NetworkConstants.RESPONSE_TYPE.RETRIEVE_TYPE:
                retrieveNonCustomGoalsListFromDB();
                break;
        }
    }

    @Override
    public void onError(String message) {

        iResultViewListener.onDisplayMessage(message);

    }

    @Override
    public void onUpdateQuerySuccess(int responseType) {

    }

    @Override
    public void onDeleteQuerySuccess(boolean isDelete, int responseType) {

    }

    private List<GoalsDO> parseDBResponse(Cursor cursor) {

        List<GoalsDO> goalsDOList = new ArrayList<>();
        HashMap<String, Integer> goalSectionItemList = new HashMap<>();
        Integer goalSectionCount = 0;
        try {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {

                        GoalsDO goalsDO = new GoalsDO();
                        goalsDO.setId(cursor.getInt(cursor.getColumnIndex(DBConstants.Columns.ID)));
                        goalsDO.setGoalTypeHeaderId(cursor.getInt(cursor.getColumnIndex(DBConstants.Columns.GOAL_TYPE_HEADER_ID)));
                        goalsDO.setStateStatus(cursor.getInt(cursor.getColumnIndex(DBConstants.Columns.STATE_STATUS)));
                        goalsDO.setGroupTitle(cursor.getString(cursor.getColumnIndex(DBConstants.Columns.GROUP_TITLE)));
                        goalsDO.setTitle(cursor.getString(cursor.getColumnIndex(DBConstants.Columns.TITLE)));
                        goalsDO.setDueDate(cursor.getString(cursor.getColumnIndex(DBConstants.Columns.DUE_DATE)));
                        goalsDO.setPerformanceGoalType(cursor.getString(cursor.getColumnIndex(DBConstants.Columns.PERFORMANCE_GOAL_TYPE)));
                        goalsDO.setAlignmentGoalType(cursor.getString(cursor.getColumnIndex(DBConstants.Columns.ALIGNMENT_GOAL_TYPE)));
                        goalsDO.setState(cursor.getInt(cursor.getColumnIndex(DBConstants.Columns.STATE)));
                        goalsDO.setStatus(cursor.getInt(cursor.getColumnIndex(DBConstants.Columns.STATUS)));
                        goalsDO.setRoleName(cursor.getString(cursor.getColumnIndex(DBConstants.Columns.ROLE_NAME)));
                        goalsDO.setPlanTitle(cursor.getString(cursor.getColumnIndex(DBConstants.Columns.PLAN_TITLE)));

                        if (!goalSectionItemList.containsKey(goalsDO.getGroupTitle())) {
                            goalSectionCount = 1;
                            goalSectionItemList.put(goalsDO.getGroupTitle(), goalSectionCount);
                        } else {
                            goalSectionCount++;
                            goalSectionItemList.put(goalsDO.getGroupTitle(), goalSectionCount);
                        }

                        goalsDO.setTotalGoalSection(goalSectionItemList);
                        goalsDOList.add(goalsDO);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            Log.d(TAG, "OnQuerySuccess: " + ex);
        }

        return goalsDOList;
    }

}


/*String strOrgQuery = "SELECT DISTINCT tbg." + DBConstants.Columns.ID
                + " ,tbg." + DBConstants.Columns.GOAL_TYPE_HEADER_ID + " ,tbg." + DBConstants.Columns.GOAL_TYPE_ID
                + " ,tbg." + DBConstants.Columns.STATE_STATUS + " ,tbg." + DBConstants.Columns.GROUP_TITLE
                + " ,tbg." + DBConstants.Columns.TITLE + " ,tbg." + DBConstants.Columns.DUE_DATE
                + " ,tbg." + DBConstants.Columns.ALIGNMENT_GOAL_TYPE + " ,tbg." + DBConstants.Columns.PERFORMANCE_GOAL_TYPE
                + " ,tbg." + DBConstants.Columns.STATE + " ,tbg." + DBConstants.Columns.STATUS
                + " ,tbg." + DBConstants.Columns.ROLE_NAME + " ,tbp." + DBConstants.Columns.PLAN_TITLE
                + " FROM " + DBConstants.Tables.TBL_GOALS + " tbg LEFT OUTER JOIN " + DBConstants.Tables.TBL_PLAN + " tbp"
                + " ON tbg." + DBConstants.Columns.ID + " == tbp." + DBConstants.Columns.ID
                + " ORDER BY tbg."
                + DBConstants.Columns.GOAL_TYPE_HEADER_ID + " , tbg." + DBConstants.Columns.GOAL_TYPE_ID
                + " , tbg." + DBConstants.Columns.STATE_STATUS + " , tbg." + DBConstants.Columns.TITLE;*/
