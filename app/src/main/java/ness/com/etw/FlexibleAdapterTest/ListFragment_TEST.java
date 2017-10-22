package ness.com.etw.FlexibleAdapterTest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IHeader;
import ness.com.etw.R;
import ness.com.etw.common.base.BaseFragment;
import ness.com.etw.common.customView.CustomView;
import ness.com.etw.common.presenter.IResultView;
import ness.com.etw.notes.presenter.NotesListViewImpl;
import ness.com.etw.notes.views.adapter.NoteListAdapter;


public class ListFragment_TEST extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, IResultView {

    private Context context;
    private RecyclerView rcvNotesList;
    private SwipeRefreshLayout srlRefresh;
    private TextView txtEmptyList;
    private NotesListViewImpl notesListViewImpl;
    private ExampleAdapter mAdapter;
    List<AbstractFlexibleItem> arrayList = new ArrayList();
    int lastHeaderId = 0;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitView(view);
    }

    @Override
    public void InitView(View view) {

        context = getContext();
        srlRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl_notes_list_refresh);
        srlRefresh.setOnRefreshListener(this);

        rcvNotesList = (RecyclerView) view.findViewById(R.id.rcv_notes_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rcvNotesList.setLayoutManager(linearLayoutManager);
        rcvNotesList.addItemDecoration(new DividerItemDecoration(context, linearLayoutManager.getOrientation()));
        //txtEmptyList = (TextView) view.findViewById(R.id.txt_notes_list_empty);

        createHeadersSectionsDatabase(8, 2);

        mAdapter = new ExampleAdapter(arrayList, getActivity());
        rcvNotesList.setAdapter(mAdapter);

        mAdapter
                .setSwipeEnabled(true)
                .setStickyHeaders(true)
                .setDisplayHeadersAtStartUp(true)
                .showAllHeaders();
        //notesListViewImpl = new NotesListViewImpl(context, this);
        //notesListViewImpl.retrieveNotesFromDB();

        //showResultList(notesListViewImpl.getMockNotesList());

    }

    public void createHeadersSectionsDatabase(int size, int headers) {
        HeaderItem header = null;

        //SimpleItem holds HeaderItem
        arrayList.clear();
        for (int i = 0; i < size; i++) {
            header = i % Math.round(size / headers) == 0 ? newHeader(++lastHeaderId) : header;
            //header = newHeader(++lastHeaderId);
            arrayList.add(newSimpleItem(i + 1, header));
        }
    }

    public static HeaderItem newHeader(int i) {
        HeaderItem header = new HeaderItem("H" + i);
        header.setTitle("Header " + i);
        //header is hidden and un-selectable by default!
        return header;
    }

    public static SimpleItem newSimpleItem(int i, IHeader header) {
        SimpleItem item = new SimpleItem("I" + i, (HeaderItem) header);
        item.setTitle("Simple Item SIMPLE ITEM " + i);
        return item;
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_notes_list;
    }

    @Override
    public void onRefresh() {
        notesListViewImpl.retrieveNotesListAPI(true, null, 0);
    }

    @Override
    public void showResult(Object listDO) {

    }

    @Override
    public void onDisplayMessage(String message) {

        if (!TextUtils.isEmpty(message)) {
            CustomView.showToast(context, message).show();
        }
    }

    @Override
    public void showResultList(List resultDOList) {

        if (resultDOList != null && resultDOList.size() > 0) {

            NoteListAdapter noteListAdapter = new NoteListAdapter(context, resultDOList);
            rcvNotesList.setAdapter(noteListAdapter);

        } else {

            //if no data found, make API call
            notesListViewImpl.retrieveNotesListAPI(true, null, 0);
        }
    }

    @Override
    public void displayErrorList(List errorList) {
    }

}
