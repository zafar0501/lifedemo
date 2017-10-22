package ness.com.etw.authenticate.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ness.com.etw.GlobalState;
import ness.com.etw.R;
import ness.com.etw.common.base.BaseActivity;
import ness.com.etw.common.base.BaseFragment;
import ness.com.etw.common.constant.IntegerConstant;
import ness.com.etw.common.constant.StringConstant;
import ness.com.etw.goals.views.GoalsListFragment;
import ness.com.etw.home.view.HomeDetailFragment;
import ness.com.etw.notes.views.fragments.NotesListFragment;
import ness.com.etw.utility.HelperUtility;


public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView tool_title;
    private BottomNavigationView bnvFooter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
    }

    @Override
    public void InitView() {

        Toolbar homeToolbar = (Toolbar) findViewById(R.id.tbl_home_toolbar);
        setSupportActionBar(homeToolbar);
        tool_title = homeToolbar.findViewById(R.id.toolbar_title);

        bnvFooter = (BottomNavigationView) findViewById(R.id.bnv_home_navigation);
        bnvFooter.setOnNavigationItemSelectedListener(this);

        switch (GlobalState.CurrentScreenID) {
            case GlobalState.Home:
                displayHomeFragment();
                break;
            case GlobalState.Notes:
                displayNotesFragment();
                break;
            case GlobalState.Goals:
                displayGoalsFragment();
                break;
            case GlobalState.Archive:
                break;
        }

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_home_detail:
                displayHomeFragment();
                break;

            case R.id.menu_detail_notes:
                //displayNotesFragment();
                displayNotesFragment(IntegerConstant.INotesArchive.NOTES_DISPLAY);
                break;
            case R.id.menu_detail_goal:
                displayGoalsFragment();
                break;
        }
        return true;
    }


    public void displayHomeFragment() {
        GlobalState.CurrentScreenID = GlobalState.Home;
        tool_title.setText(R.string.home);
        bnvFooter.getMenu().getItem(0).setChecked(true);
        HelperUtility.ReplaceFragmentWithoutBackStack(getSupportFragmentManager(), R.id.fl_home_container,
                new HomeDetailFragment(), StringConstant.FragmentTag.HOME_DETAIL_TAG);


    }

    public void displayNotesFragment() {
        GlobalState.CurrentScreenID = GlobalState.Notes;
        tool_title.setText(R.string.notes);
        bnvFooter.getMenu().getItem(1).setChecked(true);
        HelperUtility.ReplaceFragmentWithoutBackStack(getSupportFragmentManager(), R.id.fl_home_container, new NotesListFragment(),
                StringConstant.FragmentTag.NOTES_LIST_TAG);

    }

    public void displayGoalsFragment() {
        GlobalState.CurrentScreenID = GlobalState.Goals;
        bnvFooter.getMenu().getItem(2).setChecked(true);
        tool_title.setText(R.string.goals);
        HelperUtility.ReplaceFragmentWithoutBackStack(getSupportFragmentManager(), R.id.fl_home_container, new GoalsListFragment(),
                StringConstant.FragmentTag.GOALS_LIST_TAG);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_history, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        displayNotesFragment(IntegerConstant.INotesArchive.ARCHIEVE_DISPLAY);
        return true;

    }

    private void displayNotesFragment(int Is_Archieve) {
        Bundle bundle = new Bundle();
        bundle.putInt("IS_ARCHIVE", Is_Archieve);
        NotesListFragment notesListFragment = new NotesListFragment();
        notesListFragment.setArguments(bundle);
        GlobalState.CurrentScreenID = GlobalState.Notes;
        tool_title.setText(R.string.notes);
        HelperUtility.ReplaceFragmentWithoutBackStack(getSupportFragmentManager(), R.id.fl_home_container, notesListFragment,
                StringConstant.FragmentTag.NOTES_LIST_TAG);


    }

}
