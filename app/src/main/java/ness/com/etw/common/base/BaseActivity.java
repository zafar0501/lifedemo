package ness.com.etw.common.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ness.com.etw.common.presenter.IBaseActivityView;

public abstract class BaseActivity extends AppCompatActivity implements IBaseActivityView {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
    }


    protected abstract int getLayoutResourceId();
}
