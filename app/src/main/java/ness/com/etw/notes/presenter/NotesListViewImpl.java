package ness.com.etw.notes.presenter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ness.com.etw.common.constant.IntegerConstant;
import ness.com.etw.common.constant.StringConstant;
import ness.com.etw.common.presenter.IResultView;
import ness.com.etw.common.validationMessage.AppsValidationMessage;
import ness.com.etw.localDatabase.CreateQueryAsyncTask;
import ness.com.etw.localDatabase.DBConstants;
import ness.com.etw.localDatabase.IDBCallback;
import ness.com.etw.localDatabase.RetrieveQueryAsyncTask;
import ness.com.etw.localDatabase.SqliteUtility;
import ness.com.etw.network.IEndPointsAPI;
import ness.com.etw.network.INetworkResult;
import ness.com.etw.network.NetworkClient;
import ness.com.etw.network.NetworkConstants;
import ness.com.etw.network.NetworkHandler;
import ness.com.etw.notes.model.NotesDO;
import ness.com.etw.utility.HelperUtility;
import ness.com.etw.utility.PreferenceUtility;
import retrofit2.Call;


public class NotesListViewImpl implements INetworkResult, IDBCallback {

    private static final String TAG = NotesListViewImpl.class.getSimpleName();
    private Context context;
    private IEndPointsAPI endPointsAPI;
    private NetworkHandler networkHandler;
    private IResultView.ILoadMoreResult iResultViewListener;
    private boolean isPullToRefresh;
    private int isArchive;


    public NotesListViewImpl(Context context, IResultView.ILoadMoreResult iResultViewListener) {
        this.context = context;
        this.iResultViewListener = iResultViewListener;
        endPointsAPI = NetworkClient.getClient().create(IEndPointsAPI.class);
        networkHandler = new NetworkHandler(this);
    }

    //region Sqlite
    private void insertNotesInDB(List<NotesDO> notesDOList, int responseType) {

        Object[] params = new Object[]{DBConstants.Tables.TBL_NOTES, notesDOList, responseType, isArchive};
        CreateQueryAsyncTask createQueryAsyncTask = new CreateQueryAsyncTask(context, this);
        createQueryAsyncTask.execute(params);
    }

    public void retrieveNotesFromDB(int isArchive) {

        this.isArchive = isArchive;
        String[] columnName = new String[]{DBConstants.Columns.ID, DBConstants.Columns.CLIENT_ID, DBConstants.Columns.GOAL_TITLE, DBConstants.Columns.GOAL_TYPE,
                DBConstants.Columns.AUTHOR_ID, DBConstants.Columns.FIRST_NAME, DBConstants.Columns.LAST_NAME, DBConstants.Columns.STATUS,
                DBConstants.Columns.CREATED_DATE, DBConstants.Columns.PLAN_ID, DBConstants.Columns.PLAN_TITLE, DBConstants.Columns.PROFILE_IMAGE_URL,
                DBConstants.Columns.MESSAGE_BODY, DBConstants.Columns.LAST_EVAL_KEY};

        retrieveFromDB(false, DBConstants.Tables.TBL_NOTES, columnName, DBConstants.Columns.IS_ARCHIVE + " = ?",
                new String[]{String.valueOf(isArchive)}, null, null, null, null, NetworkConstants.RESPONSE_TYPE.RETRIEVE_TYPE);
    }


    private void retrieveFromDB(boolean distinct, String tableName, String[] columnName,
                                String selection, String[] selectionArgs, String groupBy,
                                String having, String orderBy, String limit, int responseType) {

        Object[] params = new Object[]{distinct, tableName, columnName, selection, selectionArgs,
                groupBy, having, orderBy, limit, responseType};
        RetrieveQueryAsyncTask retrieveQueryAsyncTask = new RetrieveQueryAsyncTask(context, this);
        retrieveQueryAsyncTask.execute(params);
    }


    //endregion

    //region API
    @SuppressWarnings("unchecked")
    public void retrieveNotesListAPI(boolean isPullToRefresh, String lastEvalKey, int isArchive) {

        if (HelperUtility.InternetCheck(context)) {
            Call<List<NotesDO>> notesListDOCall;
            this.isArchive = isArchive;
            this.isPullToRefresh = isPullToRefresh;
            String cookieAuth = StringConstant.ICommon.AUTHORIZATION + PreferenceUtility.getAuthToken();
            String contentType = StringConstant.ICommon.CONTENT_TYPE;

            if (isArchive == IntegerConstant.INotesArchive.NOTES_DISPLAY) {
                notesListDOCall = endPointsAPI.unreadNotesAPI(contentType, cookieAuth, lastEvalKey);
            } else {
                notesListDOCall = endPointsAPI.archiveNotesAPI(contentType, cookieAuth, lastEvalKey);
            }


            notesListDOCall.enqueue(networkHandler.EnqueueRequest(NetworkConstants.RESPONSE_TYPE.RETRIEVE_TYPE));
        } else {
            iResultViewListener.onDisplayMessage(AppsValidationMessage.ICommonError.NO_INTERNET);
        }


    }


    //endregion

    @SuppressWarnings("unchecked")
    @Override
    public void onSuccess(Object responseBody, int responseType) {

        List<NotesDO> notesDOList = (List<NotesDO>) responseBody;

        switch (responseType) {

            case NetworkConstants.RESPONSE_TYPE.RETRIEVE_TYPE:

                if (isPullToRefresh) {
                    //delete the data from table on Pull to Refresh
                    SqliteUtility.deleteRecordInSqlite(context, DBConstants.Tables.TBL_NOTES,
                            DBConstants.Columns.IS_ARCHIVE + " = ?", new String[]{String.valueOf(isArchive)},
                            this, NetworkConstants.RESPONSE_TYPE.DELETE_TYPE);
                }

                //Save data in sqlite\
                insertNotesInDB(notesDOList, responseType);
                break;


        }

    }

    @Override
    public void onError(String message) {

        iResultViewListener.onDisplayMessage(message);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void OnRetrieveQuerySuccess(Cursor cursor, int responseType) {
        switch (responseType) {
            case NetworkConstants.RESPONSE_TYPE.RETRIEVE_TYPE:
                iResultViewListener.showResultList(parseDBResponse(cursor));
                break;
        }
    }

    private List<NotesDO> parseDBResponse(Cursor cursor) {

        List<NotesDO> notesDOList = new ArrayList<>();
        try {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        NotesDO notesDO = new NotesDO();

                        notesDO.setId(cursor.getString(cursor.getColumnIndex(DBConstants.Columns.ID)));
                        notesDO.setClientId(cursor.getString(cursor.getColumnIndex(DBConstants.Columns.CLIENT_ID)));
                        notesDO.setGoalTitle(cursor.getString(cursor.getColumnIndex(DBConstants.Columns.GOAL_TITLE)));
                        notesDO.setGoalType(cursor.getString(cursor.getColumnIndex(DBConstants.Columns.GOAL_TYPE)));

                        notesDO.setAuthorId(cursor.getInt(cursor.getColumnIndex(DBConstants.Columns.AUTHOR_ID)));
                        notesDO.setFirstName(cursor.getString(cursor.getColumnIndex(DBConstants.Columns.FIRST_NAME)));
                        notesDO.setLastName(cursor.getString(cursor.getColumnIndex(DBConstants.Columns.LAST_NAME)));
                        notesDO.setStatus(cursor.getInt(cursor.getColumnIndex(DBConstants.Columns.STATUS)));

                        notesDO.setCreatedDate(cursor.getString(cursor.getColumnIndex(DBConstants.Columns.CREATED_DATE)));
                        notesDO.setPlanId(cursor.getInt(cursor.getColumnIndex(DBConstants.Columns.PLAN_ID)));
                        notesDO.setPlanTitle(cursor.getString(cursor.getColumnIndex(DBConstants.Columns.PLAN_TITLE)));
                        notesDO.setProfileImageUrl(cursor.getString(cursor.getColumnIndex(DBConstants.Columns.PROFILE_IMAGE_URL)));
                        notesDO.setMessageBody(cursor.getString(cursor.getColumnIndex(DBConstants.Columns.MESSAGE_BODY)));
                        notesDO.setLastEvalKey(cursor.getString(cursor.getColumnIndex(DBConstants.Columns.LAST_EVAL_KEY)));

                        notesDOList.add(notesDO);
                    } while (cursor.moveToNext());
                }
            }

        } catch (Exception e) {
            Log.d(TAG, "OnQuerySuccess: " + e);
        }

        return notesDOList;
    }

    @Override
    public void onCreateQuerySuccess(long id, int responseType) {

        switch (responseType) {
            case NetworkConstants.RESPONSE_TYPE.RETRIEVE_TYPE:
                retrieveNotesFromDB(isArchive);
                break;
        }

    }

    @Override
    public void onUpdateQuerySuccess(int responseType) {

    }

    @Override
    public void onDeleteQuerySuccess(boolean isDelete, int responseType) {
        Log.d(TAG, "onDeleteQuerySuccess: ");

    }

    //region MOCK
    /*public List<NotesDO> getMockNotesList() {
        String mockJSON = "[{\"id\":\"db552633-b40e-4500-a4f5-4b0aaf3260a4\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test Plan Goal 5th April 2017 01 Note 07</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":1,\"createdDate\":\"2017-06-15T05:54:51Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2099,2105,2106],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"1a294c81-997f-4cd2-b15d-ae32ba525dbc\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06<br />\\nTest Plan Goal 5th April 2017 01 Note 06</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":1,\"createdDate\":\"2017-06-15T05:54:15Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"7b6e1bb5-5c24-4e4e-a989-761b673d4bb4\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":1,\"createdDate\":\"2017-06-15T05:52:54Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"b235d99e-f5a3-4c69-a388-a1c7f0f583c4\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test Plan 5th April 2017 01 Note 01</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":1,\"createdDate\":\"2017-06-15T05:45:26Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"9bce8288-0164-4f83-a944-6fb48b82f9e7\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test Note 02</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":2,\"createdDate\":\"2017-06-09T05:47:52Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"4717310a-fba0-489e-bd53-63262c045702\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test Normal Heading</p>\\n\\n<h1 title=\\\"\\\">Test Heading 1</h1>\\n\\n<h2 title=\\\"\\\">Test Heading 2</h2>\\n\\n<h3 title=\\\"\\\">Test Heading 3</h3>\\n\\n<h4 title=\\\"\\\">Test Heading 4</h4>\\n\\n<h5 title=\\\"\\\">Test Heading 5</h5>\\n\\n<h6 title=\\\"\\\">Test Heading 6</h6>\\n\\n<pre title=\\\"\\\">\\nTest Formatted\\n\\n<span style=\\\"color:#c0392b;\\\">Test Font Color</span>\\n</pre>\\n\\n<p><span style=\\\"color:#000000;\\\"><span style=\\\"background-color:#c0392b;\\\">Test Background Color</span></span></p>\\n\\n<p><span style=\\\"color:#000000;\\\"><span style=\\\"background-color:#ffffff;\\\"><a href=\\\"https://projects.invisionapp.com/d/main#/console/9985233/217090298/preview\\\" target=\\\"_blank\\\">Test Link</a></span></span></p>\\n\\n<p><strong><span style=\\\"color:#000000;\\\"><span style=\\\"background-color:#ffffff;\\\">Test Bold</span></span></strong></p>\\n\\n<p><em><strong><span style=\\\"color:#000000;\\\"><span style=\\\"background-color:#ffffff;\\\">Test Italics</span></span></strong></em></p>\\n\\n<p><u><em><strong><span style=\\\"color:#000000;\\\"><span style=\\\"background-color:#ffffff;\\\">Test Underline</span></span></strong></em></u></p>\\n\\n<p><s><u><em><strong><span style=\\\"color:#000000;\\\"><span style=\\\"background-color:#ffffff;\\\">Test Strikeout</span></span></strong></em></u></s></p>\\n\\n<ol>\\n\\t<li>Test Numbering 01</li>\\n\\t<li>Test Numbering 02</li>\\n</ol>\\n\\n<ul>\\n\\t<li>Test Bullet Point 01</li>\\n\\t<li>Test Bullet Point 02</li>\\n</ul>\\n\\n<table border=\\\"5\\\" style=\\\"width: 100%;\\\">\\n\\t<tbody>\\n\\t\\t<tr>\\n\\t\\t\\t<td>Test Row 1 Column 1</td>\\n\\t\\t\\t<td>Test Row 1 Column 2</td>\\n\\t\\t\\t<td>Test Row 1 Column 3</td>\\n\\t\\t</tr>\\n\\t\\t<tr>\\n\\t\\t\\t<td>Test Row 2 Column 1</td>\\n\\t\\t\\t<td>Test Row 2 Column 2</td>\\n\\t\\t\\t<td>Test Row 2 Column 3</td>\\n\\t\\t</tr>\\n\\t\\t<tr>\\n\\t\\t\\t<td>Test Row 3 Column 1</td>\\n\\t\\t\\t<td>Test Row 3 Column 2</td>\\n\\t\\t\\t<td>Test Row 1 Column 3</td>\\n\\t\\t</tr>\\n\\t</tbody>\\n</table>\\n\\n<p>&nbsp;</p>\\n\",\"authorId\":2096,\"author\":{\"id\":2096,\"firstName\":\"Test\",\"lastName\":\"User 05\",\"username\":\"test.user05\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":3,\"createdDate\":\"2017-06-08T11:38:16Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"378669e2-aae1-45ed-abc3-7ed370f06e71\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test Note From Web App 17</p>\\n\",\"authorId\":2096,\"author\":{\"id\":2096,\"firstName\":\"Test\",\"lastName\":\"User 05\",\"username\":\"test.user05\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":3,\"createdDate\":\"2017-06-08T11:21:48Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"c5c416da-3286-43b7-9315-8c689e87cfb9\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test Note From Web App 16</p>\\n\",\"authorId\":2096,\"author\":{\"id\":2096,\"firstName\":\"Test\",\"lastName\":\"User 05\",\"username\":\"test.user05\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":3,\"createdDate\":\"2017-06-08T11:20:14Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"20d48094-8e59-42e0-ac3f-d98aed460258\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test Note From Web App 03</p>\\n\",\"authorId\":2096,\"author\":{\"id\":2096,\"firstName\":\"Test\",\"lastName\":\"User 05\",\"username\":\"test.user05\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":3,\"createdDate\":\"2017-06-08T10:31:12Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"626537fc-25b0-4484-85bf-1e49694490eb\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test Note From web app 02</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":3,\"createdDate\":\"2017-06-08T09:57:50Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"517a7a18-6b11-48df-9dae-277e6240fd7f\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test Note From web app 01</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":3,\"createdDate\":\"2017-06-08T09:55:37Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"d3d5e64c-f11e-4866-addc-e612e429736f\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test 7th June Note 30</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":3,\"createdDate\":\"2017-06-07T05:25:10Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"c36bddfb-a33e-43d4-9cd1-a8727142d5ba\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test 7th June Note 26 Reply 01</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":3,\"createdDate\":\"2017-06-07T05:22:36Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"834d97f3-762f-45a2-974f-ca3fd4a5c026\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test 7th June Note 26 Reply 01</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":3,\"createdDate\":\"2017-06-07T05:15:36Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"d2df1aef-a403-43c0-b69e-95411181991c\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test 7th June Note 26 Reply 28</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":3,\"createdDate\":\"2017-06-07T05:14:44Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"bbbacf82-f081-4965-a865-e17812712863\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test 7th June Note 26 Reply 27</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":3,\"createdDate\":\"2017-06-07T05:14:12Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"b62a8e75-e438-4725-8234-38a64820b001\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test 7th June Note 26</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":3,\"createdDate\":\"2017-06-07T05:10:57Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"71e5fbcb-c9ee-4841-b907-0973c164b75f\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test 7th June Note 25</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":3,\"createdDate\":\"2017-06-07T05:09:43Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"233220d3-4dba-4bb7-862a-3a5a6c561a10\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test 7th June Note 24</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":3,\"createdDate\":\"2017-06-07T05:08:59Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"57692df8-b9b4-4036-a352-58cf6d076d91\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test 7th June Note 23</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":3,\"createdDate\":\"2017-06-07T05:08:43Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"c73f72d6-66a9-48fd-8ae5-a2d58d79d1e0\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test 7th June Note 22</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":3,\"createdDate\":\"2017-06-07T05:08:00Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"440ffcef-e3a6-4e55-80aa-43a1f70a8077\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test 7th June Note 21</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":3,\"createdDate\":\"2017-06-07T05:07:52Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"b00016b0-7d81-4785-95e5-1780011dbcf3\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test 7th June Note 20</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":3,\"createdDate\":\"2017-06-07T05:07:46Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"a07b6e80-aa55-4dee-9b01-fa8a0ee3cf90\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test 7th June Note 19</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":3,\"createdDate\":\"2017-06-07T05:07:11Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"1f2ceb86-7785-4023-8035-01280da6353c\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test 7th June Note 18</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":3,\"createdDate\":\"2017-06-07T05:06:32Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":\"2017-06-07T05:06:32.548Z\",\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null}]";
        //String mockJSON = "[{\"id\":\"db552633-b40e-4500-a4f5-4b0aaf3260a4\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test Plan Goal 5th April 2017 01 Note 07</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":1,\"createdDate\":\"2017-06-15T05:54:51Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2099,2105,2106],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"1a294c81-997f-4cd2-b15d-ae32ba525dbc\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06<br />\\nTest Plan Goal 5th April 2017 01 Note 06</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":1,\"createdDate\":\"2017-06-15T05:54:15Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"7b6e1bb5-5c24-4e4e-a989-761b673d4bb4\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\\n<p>Test Plan Goal 5th April 2017 01 Note 06</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":1,\"createdDate\":\"2017-06-10T05:52:54Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null},{\"id\":\"b235d99e-f5a3-4c69-a388-a1c7f0f583c4\",\"subject\":null,\"clientId\":\"internal\",\"messageBody\":\"<p>Test Plan 5th April 2017 01 Note 01</p>\\n\",\"authorId\":2105,\"author\":{\"id\":2105,\"firstName\":\"Test\",\"lastName\":\"User 10\",\"username\":\"test.user10\",\"terminationDate\":null,\"alias\":null,\"userEnabled\":1,\"title\":null},\"status\":1,\"createdDate\":\"2017-06-10T05:45:26Z\",\"parentMessageId\":\"0\",\"draft\":false,\"managerEngagement\":null,\"indirectEngagement\":null,\"unreadRecipientIds\":[-1],\"archivedRecipientIds\":[-1],\"goalId\":3823,\"goalTitle\":\"Test Plan Goal 5th April 2017 01\",\"goalType\":\"PLAN\",\"planId\":39,\"lastEvalKey\":null,\"goalAssignees\":[2096,2105,2106,2099],\"goalParticipants\":[],\"dueDate\":\"2017-05-30T00:00:00Z\",\"planTitle\":\"My Plan\",\"roleTitle\":null}]";
        List<NotesDO> notesDOList = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(mockJSON);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                NotesDO notesDO = new NotesDO();
                notesDO.setId(jsonObj.getString("id"));
                notesDO.setGoalType(jsonObj.getString("goalType"));
                notesDO.setGoalTitle(jsonObj.getString("goalTitle"));
                notesDO.setDueDate(jsonObj.getString("dueDate"));
                notesDO.setMessageBody(jsonObj.getString("messageBody"));
                notesDO.setStatus(jsonObj.getInt("status"));
                notesDO.setCreatedDate(jsonObj.getString("createdDate"));

                String firstName = jsonObj.getJSONObject("author").getString("firstName");
                String lastName = jsonObj.getJSONObject("author").getString("lastName");

                AuthorDO authorDO = new AuthorDO();
                authorDO.setFirstName(firstName);
                authorDO.setLastName(lastName);
                notesDO.setAuthor(authorDO);

                notesDOList.add(notesDO);

            }


        } catch (Exception ex) {
            Log.d(TAG, "getMockNotesList: ");
        }

        return notesDOList;
    }
*/

    //endregion
}

