package ness.com.etw.authenticate.presenter.implementer;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

import ness.com.etw.authenticate.model.Signout;
import ness.com.etw.authenticate.presenter.interactor.ISignOut;
import ness.com.etw.common.constant.StringConstant;
import ness.com.etw.common.presenter.IResultView;
import ness.com.etw.common.validationMessage.AppsValidationMessage;
import ness.com.etw.localDatabase.DBConstants;
import ness.com.etw.localDatabase.DeleteQueryAsyncTask;
import ness.com.etw.localDatabase.IDBCallback;
import ness.com.etw.localDatabase.SqliteUtility;
import ness.com.etw.network.IEndPointsAPI;
import ness.com.etw.network.INetworkResult;
import ness.com.etw.network.NetworkClient;
import ness.com.etw.network.NetworkHandler;
import ness.com.etw.utility.HelperUtility;
import ness.com.etw.utility.PreferenceUtility;
import retrofit2.Call;

import static ness.com.etw.network.NetworkConstants.RESPONSE_TYPE;

public class SignOutImpl implements INetworkResult, IDBCallback {
    private IEndPointsAPI endPointsAPI;
    private Context context;
    private NetworkHandler networkHandler;
    private ISignOut showResultSignOutListener;
    private IResultView iResultViewListener;
    private static final String TAG = SignOutImpl.class.getSimpleName();
    String HEADER_AUTHORIZATION = "Authorization=" + PreferenceUtility.getAuthToken();

    public SignOutImpl(Context context, ISignOut ISignOut, IResultView iResultViewListener) {
        this.context = context;
        this.iResultViewListener = iResultViewListener;
        this.showResultSignOutListener = ISignOut;
        endPointsAPI = NetworkClient.getClient().create(IEndPointsAPI.class);
        networkHandler = new NetworkHandler(this);

    }

    //region Sqlite


    private void deleteLoggedUserData() {

        SqliteUtility.deleteRecordInSqlite(context, DBConstants.Tables.TBL_GOALS, null, null, this, RESPONSE_TYPE.DELETE_TYPE);
        SqliteUtility.deleteRecordInSqlite(context, DBConstants.Tables.TBL_NOTES, null, null, this, RESPONSE_TYPE.DELETE_TYPE);
        SqliteUtility.deleteRecordInSqlite(context, DBConstants.Tables.TBL_PLAN, null, null, this, RESPONSE_TYPE.DELETE_TYPE);
    }

    //endregion

    //region API
    @SuppressWarnings("unchecked")
    public void callSignOutAPI() {
        String cookieAuth = HEADER_AUTHORIZATION;
        String contentType = StringConstant.ICommon.CONTENT_TYPE;


        if (HelperUtility.InternetCheck(context)) {
            Call<Void> authenticateResponseCall = endPointsAPI.callUserSignOut(contentType, cookieAuth);
            authenticateResponseCall.enqueue(networkHandler.EnqueueRequest(RESPONSE_TYPE.RETRIEVE_TYPE));

        } else {
            iResultViewListener.onDisplayMessage(AppsValidationMessage.ICommonError.NO_INTERNET);
        }

    }

    //endregion
    @SuppressWarnings("unchecked")
    @Override
    public void onSuccess(Object responseBody, int responseType) {
        Signout authenticateDO = (Signout) responseBody;
        switch (responseType) {
            case RESPONSE_TYPE.RETRIEVE_TYPE:
                // Clear loggedIn user data
                PreferenceUtility.clearPreferences();
                deleteLoggedUserData();
                showResultSignOutListener.showResultSignOut("");
                break;
        }


    }

    @Override
    public void onError(String message) {
        iResultViewListener.onDisplayMessage(message);


    }

    @Override
    public void OnRetrieveQuerySuccess(Cursor cursor, int responseType) {

    }

    @Override
    public void onCreateQuerySuccess(long id, int responseType) {

    }

    @Override
    public void onUpdateQuerySuccess(int responseType) {

    }

    @Override
    public void onDeleteQuerySuccess(boolean isDelete, int responseType) {

    }
}
