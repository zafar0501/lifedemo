package ness.com.etw.authenticate.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import ness.com.etw.R;
import ness.com.etw.common.base.BaseActivity;
import ness.com.etw.common.validationMessage.ValidationManager;
import ness.com.etw.GlobalState;
import ness.com.etw.localDatabase.DatabaseManager;
import ness.com.etw.utility.PreferenceUtility;

public class SplashActivity extends BaseActivity {

    private static final int SPLASH_DELAY = 1000;
    private Context context = SplashActivity.this;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
    }

    @Override
    public void InitView() {

        PreferenceUtility.getPreferenceInstance(context);
        ValidationManager.getValidationMessageInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent;
                if (PreferenceUtility.getAuthToken() == null) {

                    intent = new Intent(context, LoginActivity.class);

                } else {
                    intent = new Intent(context, HomeActivity.class);
                }
                GlobalState.CurrentScreenID= GlobalState.Home;
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DELAY);

        DatabaseManager.getDatabaseManager(context);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_splash;
    }
}
