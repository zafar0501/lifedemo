package ness.com.etw.authenticate.presenter.implementer;

import android.content.Context;

import ness.com.etw.authenticate.model.UserAccountInfo;
import ness.com.etw.authenticate.presenter.interactor.IUserInfo;
import ness.com.etw.common.constant.StringConstant;
import ness.com.etw.common.presenter.IResultView;
import ness.com.etw.common.validationMessage.AppsValidationMessage;
import ness.com.etw.network.IEndPointsAPI;
import ness.com.etw.network.INetworkResult;
import ness.com.etw.network.NetworkClient;
import ness.com.etw.network.NetworkConstants;
import ness.com.etw.network.NetworkHandler;
import ness.com.etw.utility.HelperUtility;
import ness.com.etw.utility.PreferenceUtility;
import retrofit2.Call;

public class UserAccountInfoImpl implements INetworkResult {

    private static final String TAG = UserAccountInfoImpl.class.getSimpleName();
    private IEndPointsAPI endPointsAPI;
    private Context context;
    private NetworkHandler networkHandler;
    private IResultView iResultViewListener;
    private IUserInfo showResultUserInfoListener;

    public UserAccountInfoImpl(Context context, IUserInfo showResultUserInfo, IResultView iResultView) {
        this.context = context;
        this.iResultViewListener = iResultView;
        this.showResultUserInfoListener = showResultUserInfo;
        endPointsAPI = NetworkClient.getClient().create(IEndPointsAPI.class);
        networkHandler = new NetworkHandler(this);

    }

    //region API

    @SuppressWarnings("unchecked")
    public void callUserInfoAPI(String userId) {
        String cookieAuth = StringConstant.ICommon.AUTHORIZATION + PreferenceUtility.getAuthToken();
        String contentType = StringConstant.ICommon.CONTENT_TYPE;
        if (HelperUtility.InternetCheck(context)) {
            Call<UserAccountInfo> authenticateResponseCall = endPointsAPI.callUserInfoAPI(userId, contentType, cookieAuth);
            authenticateResponseCall.enqueue(networkHandler.EnqueueRequest(NetworkConstants.RESPONSE_TYPE.RETRIEVE_TYPE));
        } else {
            iResultViewListener.onDisplayMessage(AppsValidationMessage.ICommonError.NO_INTERNET);

        }

    }

    //endregion
    @SuppressWarnings("unchecked")
    @Override
    public void onSuccess(Object responseBody, int responseType) {


        UserAccountInfo userAccountInfo = (UserAccountInfo) responseBody;
        switch (responseType) {

            case NetworkConstants.RESPONSE_TYPE.RETRIEVE_TYPE:
                showResultUserInfoListener.showResultUserInfo(userAccountInfo);
                break;
        }
    }

    @Override
    public void onError(String message) {

        iResultViewListener.onDisplayMessage(message);
    }


}
