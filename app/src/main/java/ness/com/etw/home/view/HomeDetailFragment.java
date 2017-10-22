package ness.com.etw.home.view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import ness.com.etw.R;
import ness.com.etw.authenticate.view.HomeActivity;
import ness.com.etw.authenticate.view.MenuActivity;
import ness.com.etw.common.adapter.TitleRecyclerAdapter;
import ness.com.etw.common.base.BaseFragment;
import ness.com.etw.common.model.TitleDO;
import ness.com.etw.common.presenter.IItemViewAction;
import ness.com.etw.common.presenter.IResultView;
import ness.com.etw.common.validationMessage.ValidationAdapter;
import ness.com.etw.common.validationMessage.ValidationManager;

public class HomeDetailFragment extends BaseFragment implements IItemViewAction, View.OnClickListener, IResultView {

    private Context context;
    private List<TitleDO> titleDOList = new ArrayList<>();
    private BottomNavigationView bnvFooter;
    private Toolbar homeToolbar;
    private ImageView imageView_back;
    private RecyclerView rcvValidationMessage;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitView(view);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void InitView(View view) {

        context = getContext();
        homeToolbar = (getActivity()).findViewById(R.id.tbl_home_toolbar);
        imageView_back = homeToolbar.findViewById(R.id.img_menu_back);
        imageView_back.setImageResource((R.drawable.check));
        imageView_back.setOnClickListener(this);
        bnvFooter = getActivity().findViewById(R.id.bnv_home_navigation);
        RecyclerView rcvHomeDetails = view.findViewById(R.id.rcv_home_details);
        rcvHomeDetails.setLayoutManager(new LinearLayoutManager(context));

        rcvValidationMessage = ValidationManager.setValidationRecyclerView(getActivity());


        TitleRecyclerAdapter titleRecyclerAdapter = new TitleRecyclerAdapter(context, displayHomeDetails(), this);
        rcvHomeDetails.setAdapter(titleRecyclerAdapter);
        if (ValidationManager.isErrorListHasError()) {
            showErrorMessageList(ValidationManager.getErrorMessageList());
        }

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_home_details;
    }

    private List<TitleDO> displayHomeDetails() {

        TitleDO notes = new TitleDO();
        notes.setTitle(context.getString(R.string.notes).toUpperCase());
        notes.setSubTitle(context.getString(R.string.notesSubTitle));
        notes.setImgThumbnail(ContextCompat.getDrawable(context, R.drawable.notes));
        titleDOList.add(notes);


        TitleDO goals = new TitleDO();
        goals.setTitle(context.getString(R.string.goals).toUpperCase());
        goals.setSubTitle(context.getString(R.string.goalSubTitle));
        goals.setImgThumbnail(ContextCompat.getDrawable(context, R.drawable.goals));
        titleDOList.add(goals);

        return titleDOList;

    }

    @Override
    public void onItemRowClick(Object itemDoInfo, int position) {

        switch (position) {
            //notes
            case 0:

                ((HomeActivity) getActivity()).displayNotesFragment();
                bnvFooter.setSelectedItemId(R.id.menu_detail_notes);
                break;

            case 1:
                ((HomeActivity) getActivity()).displayGoalsFragment();
                bnvFooter.setSelectedItemId(R.id.menu_detail_goal);
                break;
        }


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

    @Override
    public void displayErrorList(List errorList) {
        ValidationAdapter validationAdapter = new ValidationAdapter(errorList, this);
        rcvValidationMessage.setAdapter(validationAdapter);
        validationAdapter.notifyDataSetChanged();
    }

    @Override
    public void showResult(Object listDO) {

    }

    @Override
    public void onDisplayMessage(String message) {

    }

    @Override
    public void showResultList(List resultDOList) {

    }

    public void showErrorMessageList(List errorList) {
        ValidationAdapter validationAdapter = new ValidationAdapter(errorList, this);
        rcvValidationMessage.setAdapter(validationAdapter);
        validationAdapter.notifyDataSetChanged();
    }
}
