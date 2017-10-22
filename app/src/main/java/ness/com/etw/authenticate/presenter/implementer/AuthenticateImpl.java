package ness.com.etw.authenticate.presenter.implementer;


import android.content.Context;
import android.util.Base64;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import ness.com.etw.authenticate.model.AuthenticateDO;
import ness.com.etw.common.constant.StringConstant;
import ness.com.etw.common.presenter.IResultView;
import ness.com.etw.common.validationMessage.AppsValidationMessage;
import ness.com.etw.network.IEndPointsAPI;
import ness.com.etw.network.INetworkResult;
import ness.com.etw.network.NetworkClient;
import ness.com.etw.network.NetworkHandler;
import ness.com.etw.utility.HelperUtility;
import ness.com.etw.utility.PreferenceUtility;
import retrofit2.Call;

import static ness.com.etw.network.NetworkConstants.RESPONSE_TYPE;

public class AuthenticateImpl implements INetworkResult {

    //private static final String TAG = AuthenticateImpl.class.getSimpleName();
    private IEndPointsAPI endPointsAPI;
    private Context context;
    private NetworkHandler networkHandler;
    private IResultView iResultViewListener;
    private String SaveCompanyId,SaveUsername;

    public AuthenticateImpl(Context context, IResultView iResultView) {
        this.context = context;
        this.iResultViewListener = iResultView;
        endPointsAPI = NetworkClient.getClient().create(IEndPointsAPI.class);
        networkHandler = new NetworkHandler(this);

    }

    //region API

    @SuppressWarnings("unchecked")
    public void callLoginAPI(String companyId, String username, String password) {
        // Save Login Data
        SaveCompanyId=companyId;
        SaveUsername=username;
        String loginString = String.format(Locale.US, "%1$s:%2$s", username, password);

        byte[] data = loginString.trim().getBytes(Charset.forName("UTF-8"));
        String base64String = "Basic " + Base64.encodeToString(data, Base64.DEFAULT);

        if (HelperUtility.InternetCheck(context)) {
            Call<AuthenticateDO> authenticateResponseCall = endPointsAPI.loginAPI(companyId, base64String.trim());
            authenticateResponseCall.enqueue(networkHandler.EnqueueRequest(RESPONSE_TYPE.RETRIEVE_TYPE));
        } else {
            iResultViewListener.onDisplayMessage(AppsValidationMessage.ICommonError.NO_INTERNET);
        }

    }

    //endregion

    @SuppressWarnings("unchecked")
    @Override
    public void onSuccess(Object responseBody, int responseType) {

        AuthenticateDO authenticateDO = (AuthenticateDO) responseBody;

        switch (responseType) {

            case RESPONSE_TYPE.RETRIEVE_TYPE:
                iResultViewListener.showResult(authenticateDO);
                PreferenceUtility.setLoginData(SaveCompanyId,SaveUsername, context);
                break;
        }
    }

    @Override
    public void onError(String message) {

        iResultViewListener.onDisplayMessage(message);
    }
}
