package ness.com.etw.notes.views.fragments;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IHeader;
import ness.com.etw.R;
import ness.com.etw.authenticate.view.HomeActivity;
import ness.com.etw.authenticate.view.MenuActivity;
import ness.com.etw.common.adapter.ListAdapter;
import ness.com.etw.common.base.BaseFragment;
import ness.com.etw.common.constant.IntegerConstant;
import ness.com.etw.common.constant.StringConstant;
import ness.com.etw.common.model.ProgressItem;
import ness.com.etw.common.presenter.IResultView;
import ness.com.etw.common.validationMessage.ValidationAdapter;
import ness.com.etw.common.validationMessage.ValidationManager;
import ness.com.etw.notes.model.NotesDO;
import ness.com.etw.notes.presenter.NotesListViewImpl;
import ness.com.etw.notes.views.items.NotesHeaderItem;
import ness.com.etw.notes.views.items.NotesSectionItem;
import ness.com.etw.utility.ErrorMessageUtility;
import ness.com.etw.utility.HelperUtility;

import static ness.com.etw.R.id.menu_home_history;


public class NotesListFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, IResultView.ILoadMoreResult, FlexibleAdapter.EndlessScrollListener, View.OnClickListener {

    private static final String TAG = NotesListFragment.class.getSimpleName();
    private Context context;
    private RecyclerView rcvNotesList;
    private NotesListViewImpl notesListViewImpl;
    private SwipeRefreshLayout srlRefresh;
    private ProgressBar pbLoader;

    private ProgressItem progressItem;
    private ListAdapter listAdapter;
    private int lastItemPosition = 0;

    private List<AbstractFlexibleItem> notesDOArrayList;
    private boolean noDataFound = true;
    private int IS_ARCHIVE = 0; // false
    private ImageView notesEmpty;
    private TextView tool_title;
    private ImageView imageView_back;
    private Toolbar homeToolbar;
    private RecyclerView rcvValidationMessage;
    private boolean isValidationListClear = true;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            IS_ARCHIVE = getArguments().getInt("IS_ARCHIVE");
        }
        InitView(view);
    }

    @Override
    public void InitView(View view) {
        setHasOptionsMenu(true);
        progressItem = new ProgressItem();
        context = getContext();

        homeToolbar = (getActivity()).findViewById(R.id.tbl_home_toolbar);
        imageView_back = homeToolbar.findViewById(R.id.img_menu_back);
        imageView_back.setImageResource((R.drawable.check));
        imageView_back.setOnClickListener(this);
        rcvValidationMessage = ValidationManager.setValidationRecyclerView(getActivity());

        if (IS_ARCHIVE == IntegerConstant.INotesArchive.NOTES_DISPLAY) {
            HelperUtility.setToolbarTitle(getActivity(), context.getString(R.string.notes));
            imageView_back.setImageResource((R.drawable.check));

        } else {
            HelperUtility.setToolbarTitle(getActivity(), context.getString(R.string.archive));
            homeToolbar.setNavigationIcon(view.getResources().getDrawable(R.drawable.backarrow));
            imageView_back.setImageResource((R.drawable.backarrow));
            homeToolbar.setNavigationIcon(null);
            imageView_back.setVisibility(View.VISIBLE);

        }


        srlRefresh = view.findViewById(R.id.srl_notes_list_refresh);
        srlRefresh.setOnRefreshListener(this);
        pbLoader = view.findViewById(R.id.pb_notes_list_loader);
        rcvNotesList = view.findViewById(R.id.rcv_notes_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rcvNotesList.setLayoutManager(linearLayoutManager);
        //rcvNotesList.addItemDecoration(new DividerItemDecoration(context, linearLayoutManager.getOrientation()));
        notesEmpty = view.findViewById(R.id.img_notes_list_empty);

        notesListViewImpl = new NotesListViewImpl(context, this);

        notesListViewImpl.retrieveNotesFromDB(IS_ARCHIVE); //0=list of unArchive notes

        //showResultList(notesListViewImpl.getMockNotesList());
        //displayNotesSectionWithStickyHeader(notesListViewImpl.getMockNotesList());


        tool_title = homeToolbar.findViewById(R.id.toolbar_title);
        if (ValidationManager.isErrorListHasError()) {
            displayErrorList(ValidationManager.getErrorMessageList());
        }

    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_notes_list;
    }

    @Override
    public void onRefresh() {
        lastItemPosition = 0;
        notesDOArrayList = null;
        notesListViewImpl.retrieveNotesListAPI(true, null, IS_ARCHIVE);
    }

    @Override
    public void showResult(Object listDO) {

    }

    @Override
    public void onDisplayMessage(String message) {

        srlRefresh.setRefreshing(false);
        pbLoader.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(message)) {
            new ValidationManager().setValidationError(ErrorMessageUtility.getCustomErrorCode(message), this);
            isValidationListClear = false;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void showResultList(List resultDOList) {


        if (resultDOList != null && resultDOList.size() > 0) {
            displayNotesSectionWithStickyHeader(resultDOList);
        } else {
            //if no data found, make API call
            notesEmpty.setVisibility(View.VISIBLE);
            rcvNotesList.setVisibility(View.GONE);

            if (listAdapter != null) {
                listAdapter.clear();
                listAdapter.notifyDataSetChanged();
            }

            if (noDataFound) {
                notesListViewImpl.retrieveNotesListAPI(true, null, IS_ARCHIVE);

            } else {
                srlRefresh.setRefreshing(false);
                pbLoader.setVisibility(View.GONE);
            }
            noDataFound = false;

        }
    }


    @Override
    public void showMoreResultList(List resultDOList) {

        notesEmpty.setVisibility(View.GONE);
        rcvNotesList.setVisibility(View.VISIBLE);

        srlRefresh.setRefreshing(false);
        pbLoader.setVisibility(View.GONE);

        if (resultDOList != null && resultDOList.size() > 0) {
            displayNotesSectionWithStickyHeader(resultDOList);
        }

    }

    @Override
    public void noMoreLoad(int newItemsSize) {

        Log.d(TAG, "noMoreLoad: " + newItemsSize);
        Log.d(TAG, "noMoreLoad: " + listAdapter.getMainItemCount());
    }


    @Override
    public void onLoadMore(int lastPosition, int currentPage) {

        lastItemPosition = lastPosition - 1;
        String lastEvalKey = (((listAdapter).getItem(lastPosition - 1)) != null ?
                ((NotesSectionItem) (listAdapter).getItem(lastPosition - 1)).getLastEvalKey() : null);
        if (!TextUtils.isEmpty(lastEvalKey)) {
            String lastDateTime = lastEvalKey.substring(0, lastEvalKey.lastIndexOf("."));
            notesListViewImpl.retrieveNotesListAPI(false, lastDateTime, IS_ARCHIVE);
            progressItem.setStatus(ProgressItem.StatusEnum.MORE_TO_LOAD);
        } else {
            progressItem.setStatus(ProgressItem.StatusEnum.NO_MORE_LOAD);
        }


    }


    @SuppressWarnings("unchecked")
    private void displayNotesSectionWithStickyHeader(List<NotesDO> notesDOList) {

        boolean nextAttempt = false;

        notesEmpty.setVisibility(View.GONE);
        rcvNotesList.setVisibility(View.VISIBLE);

        srlRefresh.setRefreshing(false);
        pbLoader.setVisibility(View.GONE);


        NotesHeaderItem notesHeaderItem = null;
        List<String> uniqueCreatedDate = new ArrayList<>();

        if (notesDOArrayList == null) {
            nextAttempt = true;
        } else {
            notesDOArrayList.clear();
            listAdapter.notifyDataSetChanged();
        }
        notesDOArrayList = new ArrayList<>();

        for (int i = 0; i < notesDOList.size(); i++) {

            NotesDO notesDO = notesDOList.get(i);
            String createdDate = HelperUtility.formattedCreatedDate(notesDO.getCreatedDate());
            if (!uniqueCreatedDate.contains(createdDate)) {

                if (IS_ARCHIVE == IntegerConstant.INotesArchive.NOTES_DISPLAY) {
                    notesHeaderItem = addNotesHeaderItem(notesDO.getId(), notesDO.getCreatedDate(), 1);
                } else {
                    notesHeaderItem = addNotesHeaderItem(notesDO.getId(), notesDO.getCreatedDate(), 0);
                }


                notesDOArrayList.add(addNotesSectionItem(notesDO, notesHeaderItem));
            } else {
                notesDOArrayList.add(addNotesSectionItem(notesDO, notesHeaderItem));
            }
            uniqueCreatedDate.add(createdDate);
        }

        listAdapter = new ListAdapter(notesDOArrayList, getActivity());
        rcvNotesList.setAdapter(listAdapter);
        listAdapter
                .setSwipeEnabled(true)
                .setStickyHeaders(true)
                .setDisplayHeadersAtStartUp(true)
                .showAllHeaders()
                .setEndlessScrollListener(this, progressItem);

        if (!nextAttempt) {
            listAdapter.notifyItemInserted(lastItemPosition);
            rcvNotesList.scrollToPosition(lastItemPosition + 1);
        }

    }


    private static NotesHeaderItem addNotesHeaderItem(String id, String createdDate, int archiveVisibility) {


        NotesHeaderItem notesHeaderItem = new NotesHeaderItem(id);
        notesHeaderItem.setStrCreateDate(HelperUtility.formattedFullCreatedDate(createdDate).toUpperCase());
        notesHeaderItem.setNotesArchiveVisibility(archiveVisibility);
        return notesHeaderItem;
    }

    private static NotesSectionItem addNotesSectionItem(NotesDO notesDO, IHeader notesHeaderItem) {

        NotesSectionItem notesSectionItem = new NotesSectionItem(notesDO.getId(), (NotesHeaderItem) notesHeaderItem);
        notesSectionItem.setCreatedDate(notesDO.getCreatedDate());
        notesSectionItem.setAuthorId(notesDO.getAuthorId());
        notesSectionItem.setGoalType(notesDO.getGoalType());
        notesSectionItem.setGoalTitle(notesDO.getGoalTitle());
        notesSectionItem.setStatus(notesDO.getStatus());
        notesSectionItem.setFirstName(TextUtils.isEmpty(notesDO.getFirstName()) ? notesDO.getAuthor().getFirstName() : notesDO.getFirstName());
        notesSectionItem.setLastName(TextUtils.isEmpty(notesDO.getLastName()) ? notesDO.getAuthor().getLastName() : notesDO.getLastName());
        notesSectionItem.setMessageBody(notesDO.getMessageBody());
        notesSectionItem.setDateTime(notesDO.getCreatedDate());
        notesSectionItem.setLastEvalKey(notesDO.getLastEvalKey());
        return notesSectionItem;

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem menuHistoryItem = menu.findItem(menu_home_history);

        if (IS_ARCHIVE == IntegerConstant.INotesArchive.ARCHIEVE_DISPLAY) {
            menuHistoryItem.setVisible(false);
        } else {
            menuHistoryItem.setVisible(true);
        }


    }

    @Override
    public void displayErrorList(List errorList) {
        showErrorMessageList(errorList);
    }


    public void showErrorMessageList(List errorList) {

        ValidationAdapter validationAdapter = new ValidationAdapter(errorList, this);
        rcvValidationMessage.setAdapter(validationAdapter);
        validationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_menu_back:
                Log.d("Core", "Home Toolbar in Notes");
                if (IS_ARCHIVE == IntegerConstant.INotesArchive.NOTES_DISPLAY) {
                    Intent intent = new Intent(getActivity(), MenuActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
                    getActivity().finish();

                } else {
                    ((HomeActivity) getActivity()).displayNotesFragment();
                }
        }

    }

}


     /*  homeToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IS_ARCHIVE == IntegerConstant.INotesArchive.NOTES_DISPLAY) {
                    Intent intent = new Intent(getActivity(), MenuActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                } else {
                    HelperUtility.ReplaceFragmentWithoutBackStack(getActivity().getSupportFragmentManager(), R.id.fl_home_container, new NotesListFragment(),
                            StringConstant.FragmentTag.NOTES_LIST_TAG);
                }
            }
        });*/
