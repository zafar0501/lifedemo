package ness.com.etw.goals.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IHeader;
import ness.com.etw.R;
import ness.com.etw.authenticate.view.MenuActivity;
import ness.com.etw.common.adapter.ListAdapter;
import ness.com.etw.common.base.BaseFragment;
import ness.com.etw.common.customView.CustomView;
import ness.com.etw.common.presenter.IResultView;
import ness.com.etw.common.validationMessage.ValidationAdapter;
import ness.com.etw.common.validationMessage.ValidationManager;
import ness.com.etw.goals.items.GoalHeaderItem;
import ness.com.etw.goals.items.GoalSectionItem;
import ness.com.etw.goals.model.AllGoalsDO;
import ness.com.etw.goals.model.GoalsDO;
import ness.com.etw.goals.presenter.GoalsListViewImpl;
import ness.com.etw.notes.views.fragments.NotesListFragment;
import ness.com.etw.utility.ErrorMessageUtility;
import ness.com.etw.utility.HelperUtility;
import ness.com.etw.utility.JWTUtils;
import ness.com.etw.utility.PreferenceUtility;


public class GoalsListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, IResultView.ILoadMoreResult, View.OnClickListener {


    private static final String TAG = NotesListFragment.class.getSimpleName();
    private Context context;
    private RecyclerView rcvGoalsList;
    private GoalsListViewImpl goalsListViewImpl;
    private SwipeRefreshLayout srlRefresh;
    private ProgressBar pbLoader;
    private ImageView goalsEmpty;
    private List<AbstractFlexibleItem> gaolsDOArrayList;
    private boolean noDataFound = true;
    private ListAdapter listAdapter;
    private Toolbar homeToolbar;
    private ImageView imageView_back;
    private RecyclerView rcvValidationMessage;
    private boolean isValidationListClear = true;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitView(view);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_goals_list;
    }

    @Override
    public void InitView(View view) {
        context = getActivity();

        HelperUtility.setToolbarTitle(getActivity(), context.getString(R.string.goals));
        homeToolbar = (getActivity()).findViewById(R.id.tbl_home_toolbar);
        imageView_back = homeToolbar.findViewById(R.id.img_menu_back);
        imageView_back.setImageResource((R.drawable.check));
        imageView_back.setOnClickListener(this);

        srlRefresh = view.findViewById(R.id.srl_goal_list_refresh);
        srlRefresh.setOnRefreshListener(this);
        pbLoader = view.findViewById(R.id.pb_goals_list_loader);
        rcvValidationMessage = ValidationManager.setValidationRecyclerView(getActivity());

        rcvGoalsList = view.findViewById(R.id.rcv_goal_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rcvGoalsList.setLayoutManager(linearLayoutManager);
        goalsEmpty = view.findViewById(R.id.img_goals_list_empty);
        goalsListViewImpl = new GoalsListViewImpl(context, this);

        //retrieve Data from Sqlite
        goalsListViewImpl.retrieveNonCustomGoalsListFromDB();
        //onRefresh();
        if (ValidationManager.isErrorListHasError()) {
            displayErrorList(ValidationManager.getErrorMessageList());
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        setHasOptionsMenu(true);


    }

    @Override
    public void onRefresh() {

        //get authorId from JWT
        goalsListViewImpl.retrieveGoalListAPI(true, PreferenceUtility.getUserId());
    }

    @Override
    public void showResult(Object listDO) {

    }

    @Override
    public void onDisplayMessage(String message) {
        srlRefresh.setRefreshing(false);
        pbLoader.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(message)) {
            //CustomView.showToast(context, message).show();
            new ValidationManager().setValidationError(ErrorMessageUtility.getCustomErrorCode(message), this);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void showResultList(List resultDOList) {

        gaolsDOArrayList = null;
        if (resultDOList != null && resultDOList.size() > 0) {
            displayGoalSectionWithStickyHeader(resultDOList);
        } else {
            //if no data found, make API call
            goalsEmpty.setVisibility(View.VISIBLE);
            rcvGoalsList.setVisibility(View.GONE);

            if (listAdapter != null) {
                listAdapter.clear();
                listAdapter.notifyDataSetChanged();
            }

            if (noDataFound) {
                goalsListViewImpl.retrieveGoalListAPI(true, JWTUtils.getUserId());
            } else {
                srlRefresh.setRefreshing(false);
                pbLoader.setVisibility(View.GONE);
            }
            noDataFound = false;

        }
    }

    @Override
    public void showMoreResultList(List resultDOList) {
        Log.d(TAG, "showMoreResultList: " + resultDOList);
    }

    private void displayGoalSectionWithStickyHeader(List<AllGoalsDO> goalsDOList) {

        goalsEmpty.setVisibility(View.GONE);
        rcvGoalsList.setVisibility(View.VISIBLE);

        srlRefresh.setRefreshing(false);
        pbLoader.setVisibility(View.GONE);

        GoalHeaderItem goalHeaderItem = null;
        List<String> uniqueGoalType = new ArrayList<>();
        gaolsDOArrayList = new ArrayList<>();

        for (int i = 0; i < goalsDOList.size(); i++) {

            GoalsDO goalsDO = goalsDOList.get(i);
            String goalTypeHeader = goalsDO.getGroupTitle();
            if (!uniqueGoalType.contains(goalTypeHeader)) {
                goalHeaderItem = addGoalHeaderItem(goalTypeHeader, goalsDO.getTotalGoalSection().get(goalTypeHeader));
                gaolsDOArrayList.add(addGoalsSectionItem(goalsDO, goalHeaderItem));
            } else {
                gaolsDOArrayList.add(addGoalsSectionItem(goalsDO, goalHeaderItem));
            }
            uniqueGoalType.add(goalTypeHeader);
        }

        listAdapter = new ListAdapter(gaolsDOArrayList, getActivity());
        rcvGoalsList.setAdapter(listAdapter);
        listAdapter
                .setStickyHeaders(true)
                .setDisplayHeadersAtStartUp(true)
                .showAllHeaders();


    }

    private static GoalSectionItem addGoalsSectionItem(GoalsDO goalsDO, IHeader goalsHeaderItem) {

        GoalSectionItem goalSectionItem = new GoalSectionItem(String.valueOf(goalsDO.getId()),
                (GoalHeaderItem) goalsHeaderItem);
        goalSectionItem.setGoalTitle(goalsDO.getTitle());
        goalSectionItem.setGoalType(TextUtils.isEmpty(goalsDO.getAlignmentGoalType()) ? goalsDO.getPerformanceGoalType() : goalsDO.getAlignmentGoalType());
        goalSectionItem.setStatus(goalsDO.getStatus());
        goalSectionItem.setStateStatus(goalsDO.getStateStatus());
        goalSectionItem.setState(goalsDO.getState());
        goalSectionItem.setDueDate(goalsDO.getDueDate());
        goalSectionItem.setPlanTitle(goalsDO.getPlanTitle());
        goalSectionItem.setRoleName(goalsDO.getRoleName());
        return goalSectionItem;

    }

    private static GoalHeaderItem addGoalHeaderItem(String goalTypeHeader, int totalGoalSectionItem) {

        GoalHeaderItem goalHeaderItem = new GoalHeaderItem(goalTypeHeader, totalGoalSectionItem);
        goalHeaderItem.setStrGoalTypeHeader(goalTypeHeader);

        goalHeaderItem.setStrGoalTypeGroupTitle(goalTypeHeader);
        return goalHeaderItem;
    }

    @Override
    public void displayErrorList(List errorList) {
        showErrorMessageList(errorList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_menu_back:

                Intent intent = new Intent(getContext(), MenuActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
                getActivity().finish();
                break;

        }
    }


    public void showErrorMessageList(List errorList) {

        ValidationAdapter validationAdapter = new ValidationAdapter(errorList, this);
        rcvValidationMessage.setAdapter(validationAdapter);
        isValidationListClear = false;
        validationAdapter.notifyDataSetChanged();
    }
}
