package ness.com.etw.notes.views.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.List;
import java.util.Locale;

import ness.com.etw.R;
import ness.com.etw.notes.model.NotesDO;
import ness.com.etw.notes.presenter.NotesListViewImpl;
import ness.com.etw.utility.HelperUtility;

import static ness.com.etw.utility.HelperUtility.convertStringToDate;

public class NoteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = NotesListViewImpl.class.getSimpleName();
    private Context context;
    private List<NotesDO> notesDOList;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    private final int SHOW_SECTION_HEADER = 0;
    private final int SHOW_ITEM_VIEW_ROW = 1;


    public NoteListAdapter(Context context, List<NotesDO> notesDOList) {
        this.context = context;
        this.notesDOList = notesDOList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        View itemView = LayoutInflater.from(context).inflate(R.layout.notes_section_item_row, parent, false);
        viewHolder = new NotesListAdapterViewHolder(itemView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        NotesDO notesDO = notesDOList.get(position);
        NotesListAdapterViewHolder notesListAdapterViewHolder = (NotesListAdapterViewHolder) holder;

        switch (holder.getItemViewType()) {

            case SHOW_SECTION_HEADER:
                notesListAdapterViewHolder.rlSectionHeader.setVisibility(View.VISIBLE);
                notesListAdapterViewHolder.txtCreatedDate.setText(HelperUtility.formattedCreatedDate(notesDO.getCreatedDate()));
                break;
            case SHOW_ITEM_VIEW_ROW:
                notesListAdapterViewHolder.rlSectionHeader.setVisibility(View.GONE);
                break;
        }


        notesListAdapterViewHolder.txtGoalType.setText(notesDO.getGoalType());
        notesListAdapterViewHolder.txtGoalTitle.setText(notesDO.getGoalTitle());

        notesListAdapterViewHolder.txtName.setText(String.format(Locale.US, "%1$s %2$s", notesDO.getAuthor().getFirstName(), notesDO.getAuthor().getLastName()));
        notesListAdapterViewHolder.txtDateTime.setText(notesDO.getDueDate());

        notesListAdapterViewHolder.txtDescription.setText(Html.fromHtml(notesDO.getMessageBody()));

        notesListAdapterViewHolder.txtStatus.setText(notesDO.getStatus().toString());
        binderHelper.bind(notesListAdapterViewHolder.swipeRevealLayout, "swipeRelativeLayout");


    }

    @Override
    public int getItemCount() {
        return notesDOList.size();
    }


    @Override
    public int getItemViewType(int position) {

        long prevCreatedDate, nextCreatedDate;

        if (position == 0) {
            return SHOW_SECTION_HEADER;
        } else {

            prevCreatedDate = convertStringDateToMilli(notesDOList.get(position - 1).getCreatedDate());
            nextCreatedDate = convertStringDateToMilli(notesDOList.get(position).getCreatedDate());

            if (prevCreatedDate == nextCreatedDate) {
                return SHOW_ITEM_VIEW_ROW;
            } else {
                return SHOW_SECTION_HEADER;
            }
        }
    }

    private class NotesListAdapterViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rlSectionHeader;

        private ImageView imgCheck;
        private TextView txtCreatedDate;
        private SwipeRevealLayout swipeRevealLayout;
        private ImageView imgProfile;
        private TextView txtArchive, txtGoalTitle, txtGoalType, txtStatus, txtName, txtDateTime, txtDescription;

        NotesListAdapterViewHolder(View itemView) {
            super(itemView);
            //swipeRevealLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout_notes_list_item_row);
            imgProfile = (ImageView) itemView.findViewById(R.id.img_notes_list_item_row_profile);
            //txtArchive = (TextView) itemView.findViewById(R.id.txt_notes_list_archive);

            txtGoalType = (TextView) itemView.findViewById(R.id.txt_notes_list_item_row_goal_type);
            txtGoalTitle = (TextView) itemView.findViewById(R.id.txt_notes_list_item_row_goal_title);


            txtStatus = (TextView) itemView.findViewById(R.id.txt_notes_list_item_row_status);

            txtName = (TextView) itemView.findViewById(R.id.txt_notes_list_item_row_name);
            txtDateTime = (TextView) itemView.findViewById(R.id.txt_notes_list_item_row_date_time);
            txtDescription = (TextView) itemView.findViewById(R.id.txt_notes_list_item_row_description);


        }
    }


    private class SectionViewHolder extends RecyclerView.ViewHolder {


        private ImageView imgCheck;
        private TextView txtCreatedDate;

        SectionViewHolder(View itemView) {
            super(itemView);

        }
    }

    private static long convertStringDateToMilli(String createDate) {

        try {
            /*Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).parse(createDate);
            return date.getTime();*/
            return convertStringToDate(createDate).getTime();

        } catch (Exception ex) {

            Log.d(TAG, "stringDateToMilli: ");
        }
        return 0;
    }


}
