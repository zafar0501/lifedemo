package ness.com.etw.authenticate.view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import java.util.List;

import ness.com.etw.GlobalState;
import ness.com.etw.R;
import ness.com.etw.authenticate.model.AuthenticateDO;
import ness.com.etw.authenticate.model.UserAccountInfo;
import ness.com.etw.authenticate.presenter.implementer.AuthenticateImpl;
import ness.com.etw.authenticate.presenter.implementer.UserAccountInfoImpl;
import ness.com.etw.authenticate.presenter.interactor.IUserInfo;
import ness.com.etw.common.base.BaseActivity;
import ness.com.etw.common.constant.StringConstant;
import ness.com.etw.common.customView.CustomView;
import ness.com.etw.common.presenter.IResultView;
import ness.com.etw.common.validationMessage.AppsValidationMessage;
import ness.com.etw.common.validationMessage.ValidationAdapter;
import ness.com.etw.common.validationMessage.ValidationManager;
import ness.com.etw.utility.ErrorMessageUtility;
import ness.com.etw.utility.HelperUtility;
import ness.com.etw.utility.JWTUtils;
import ness.com.etw.utility.PreferenceUtility;

public class LoginActivity extends BaseActivity implements IResultView, IUserInfo, View.OnClickListener, TextWatcher {


    private Context context;
    private EditText edtCompany, edtUsername, edtPassword;
    private AuthenticateImpl authenticateImpl;
    private UserAccountInfoImpl userAccountInfo;
    private String strCompanyId, strUsername, strPassword;
    private View viewProgressBar;
    private Button btnSignIn;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private RecyclerView rcvValidationMessage;
    private ScrollView scvloginScroll;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
    }

    @Override
    public void InitView() {
        context = this;

        edtCompany = (EditText) findViewById(R.id.edt_login_companyId);
        edtCompany.addTextChangedListener(this);
        edtUsername = (EditText) findViewById(R.id.edt_login_username);
        edtUsername.addTextChangedListener(this);
        edtPassword = (EditText) findViewById(R.id.edt_login_password);
        edtPassword.addTextChangedListener(this);
        viewProgressBar = findViewById(R.id.vw_login_progressbar);
        btnSignIn = (Button) findViewById(R.id.btn_login_signin);
        CustomView.drawRoundedCorners(context, ContextCompat.getColor(context, R.color.cornflowerBlue), btnSignIn);
        btnSignIn.setOnClickListener(this);
        scvloginScroll = (ScrollView) findViewById(R.id.scv_login_scroll);

        authenticateImpl = new AuthenticateImpl(context, this);
        userAccountInfo = new UserAccountInfoImpl(context, this, this);
        rcvValidationMessage = ValidationManager.setValidationRecyclerView(this);

        //On basis of server message, get will pass the custom code in the method
        new ValidationManager().setValidationError(ErrorMessageUtility.getCustomErrorCode(AppsValidationMessage.ICommonError.NO_INTERNET), this);

        //Load CompanyId & UserName from SharedPrefernece
        try {
            edtCompany.setText(PreferenceUtility.getCompanyID().trim());
            edtUsername.setText(PreferenceUtility.getUserName().trim());
        } catch (Exception e) {
            Log.d(TAG, "ShowError Msg :" + e);
        }


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    @Override
    public void showResult(Object listDO) {

        try {

            ValidationManager.clearValidationMessageList(true);
            String accessToken = ((AuthenticateDO) listDO).getAccessToken();
            PreferenceUtility.clearPreferences();

            String pref = PreferenceUtility.getAuthToken();
            PreferenceUtility.setAuthToken(accessToken, context);
            String pref1 = PreferenceUtility.getAuthToken();
            String cookieAuth = StringConstant.ICommon.AUTHORIZATION + PreferenceUtility.getAuthToken();

            GlobalState.CurrentScreenID = GlobalState.Home;
            Intent homeIntent = new Intent(this, HomeActivity.class);
            GlobalState.CurrentScreenID = GlobalState.Home;

            viewProgressBar.setVisibility(View.GONE);
            String userId = JWTUtils.getUserId();
            PreferenceUtility.setUserID(userId);
            userAccountInfo.callUserInfoAPI(userId);
            startActivity(homeIntent);
            finish();

        } catch (Exception e) {
            Log.d(TAG, "showResult: " + e);
        }

    }

    @Override
    public void onDisplayMessage(String message) {

        if (!TextUtils.isEmpty(message)) {
            //On basis of server message, get will pass the custom code in the method
            new ValidationManager().setValidationError(ErrorMessageUtility.getCustomErrorCode(message), this);
        }

        viewProgressBar.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_login_signin:


                if (isValid()) {

                    viewProgressBar.setVisibility(View.VISIBLE);
                    //"internal", "Amrita.ghosh", "Pwd_12345"
                    authenticateImpl.callLoginAPI(strCompanyId, strUsername, strPassword);
                    HelperUtility.hideKeyBoard(context, edtCompany);
                    HelperUtility.hideKeyBoard(context, edtUsername);
                    HelperUtility.hideKeyBoard(context, edtPassword);
                }

                break;
            case R.id.txt_forgot_login:
                // TODO: 07/07/17 implement forgot password
                break;
        }
    }

    @Override
    public void showResultList(List resultDOList) {

    }

    @Override
    public void showResultUserInfo(Object listDO) {
        PreferenceUtility.setUserInfo(((UserAccountInfo) listDO).getFirstName(), ((UserAccountInfo) listDO).getLastName(),
                ((UserAccountInfo) listDO).getTitle(), context);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        if (isValid()) {
            CustomView.drawRoundedCorners(context, ContextCompat.getColor(context, R.color.darkSeaBlue), btnSignIn);
        } else {
            CustomView.drawRoundedCorners(context, ContextCompat.getColor(context, R.color.cornflowerBlue), btnSignIn);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private boolean isValid() {

        strCompanyId = edtCompany.getText().toString().trim();
        strUsername = edtUsername.getText().toString().trim();
        strPassword = edtPassword.getText().toString().trim();

        return !TextUtils.isEmpty(strCompanyId) && !TextUtils.isEmpty(strUsername) && strPassword.length() >= 8;

    }

    @SuppressWarnings("unchecked")
    @Override
    public void displayErrorList(List errorList) {

        ValidationAdapter validationAdapter = new ValidationAdapter(errorList, this);
        rcvValidationMessage.setAdapter(validationAdapter);
        scvloginScroll.scrollTo(0, 0);
        validationAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onStop() {
        super.onStop();
        ValidationManager.clearValidationMessageList(true);
    }
}
