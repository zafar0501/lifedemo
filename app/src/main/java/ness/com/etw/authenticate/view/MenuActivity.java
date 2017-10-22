package ness.com.etw.authenticate.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ness.com.etw.R;
import ness.com.etw.authenticate.presenter.implementer.SignOutImpl;
import ness.com.etw.authenticate.presenter.interactor.ISignOut;
import ness.com.etw.common.base.BaseActivity;
import ness.com.etw.common.presenter.IResultView;
import ness.com.etw.common.validationMessage.ValidationAdapter;
import ness.com.etw.common.validationMessage.ValidationManager;
import ness.com.etw.utility.ErrorMessageUtility;
import ness.com.etw.utility.HelperUtility;
import ness.com.etw.utility.PreferenceUtility;

public class MenuActivity extends BaseActivity implements IResultView, ISignOut, View.OnClickListener {
    //Defining Variables
    private Toolbar toolbar;
    private TextView FullName, Title;
    private TextView Txt_Setting, Txt_Get_Help, Txt_Sign_Out;
    private ImageView imageview_profilepics;
    private SignOutImpl signoutimpl;
    private HelperUtility helperUtility;
    private View viewProgressBar;
    private Context context;
    private RecyclerView rcvValidationMessage;
    private static final String TAG = MenuActivity.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
    }


    @Override
    protected int getLayoutResourceId() {

        return R.layout.activity_menu;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_history, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_home_history) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void InitView() {
        context = this;

        FullName = (TextView) findViewById(R.id.header_fullname);
        Title = (TextView) findViewById(R.id.header_title);
        Txt_Setting = (TextView) findViewById(R.id.menu_setting);
        Txt_Get_Help = (TextView) findViewById(R.id.menu_get_help);
        Txt_Sign_Out = (TextView) findViewById(R.id.menu_sign_out);
        imageview_profilepics = (ImageView) findViewById(R.id.profile_image);
        Txt_Setting.setOnClickListener(this);
        Txt_Get_Help.setOnClickListener(this);
        Txt_Sign_Out.setOnClickListener(this);
        viewProgressBar = findViewById(R.id.vw_login_progressbar);

        signoutimpl = new SignOutImpl(context, this, this);
        HelperUtility.loadProfileImage(context, PreferenceUtility.getUserId(), imageview_profilepics);
        FullName.setText(PreferenceUtility.getUserInfoName());
        Title.setText(PreferenceUtility.getUserInfoTitle());
        rcvValidationMessage = ValidationManager.setValidationRecyclerView(this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // View view = navigationView.getHeaderView(0);
        setSupportActionBar(toolbar);
        if (ValidationManager.isErrorListHasError()) {
            showErrorMessageList(ValidationManager.getErrorMessageList());
        }

        ImageView imgBack = toolbar.findViewById(R.id.menu_back);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                MenuActivity.this.finish();
            }
        });


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.menu_setting:
                Intent intent = new Intent(MenuActivity.this, SettingActivity.class);
                startActivity(intent);
                MenuActivity.this.finish();
                break;
            case R.id.menu_get_help:
                Toast.makeText(MenuActivity.this, "Help", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_sign_out:
                signoutimpl.callSignOutAPI();
                viewProgressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void showResult(Object listDO) {

    }

    @Override
    public void onDisplayMessage(String message) {
        viewProgressBar.setVisibility(View.GONE);
        new ValidationManager().setValidationError(ErrorMessageUtility.getCustomErrorCode(message), this);
    }

    @Override
    public void showResultList(List resultDOList) {

    }

    @Override
    public void displayErrorList(List errorList) {
        showResultList(errorList);
    }

    @Override
    public void showResultSignOut(String result) {
        viewProgressBar.setVisibility(View.GONE);
        Intent homeIntent = new Intent(this, LoginActivity.class);
        startActivity(homeIntent);
        MenuActivity.this.finish();
        PreferenceUtility.clearPreferences();
    }


    public void showErrorMessageList(List errorList) {
        ValidationAdapter validationAdapter = new ValidationAdapter(errorList, this);
        rcvValidationMessage.setAdapter(validationAdapter);
        validationAdapter.notifyDataSetChanged();
    }
}
