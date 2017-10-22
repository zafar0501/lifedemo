package ness.com.etw.notes.views.items;


import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.helpers.AnimatorHelper;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;
import ness.com.etw.R;
import ness.com.etw.common.constant.StringConstant;
import ness.com.etw.common.customView.CustomView;
import ness.com.etw.notes.model.NotesAbstractItem;
import ness.com.etw.utility.HelperUtility;

public class NotesSectionItem extends NotesAbstractItem<NotesSectionItem.NotesSectionItemViewHolder>
        implements ISectionable<NotesSectionItem.NotesSectionItemViewHolder, NotesHeaderItem> {


    private NotesHeaderItem notesHeaderItem;


    private NotesSectionItem(String id) {
        super(id);
        //setSwipeable(true);

    }

    public NotesSectionItem(String id, NotesHeaderItem header) {
        this(id);
        this.notesHeaderItem = header;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.notes_section_item_row;
    }

    @Override
    public NotesSectionItemViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new NotesSectionItemViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, NotesSectionItemViewHolder holder, int position, List payloads) {
        Context context = holder.itemView.getContext();
        //holder.txtGoalType.setText(String.format(Locale.US, "%1$s %2$s", getGoalType().replace("_", " "), context.getString(R.string.goal).toUpperCase()));
        holder.txtGoalType.setText(getGoalType());
        holder.txtGoalTitle.setText(getGoalTitle());
        holder.txtName.setText(getFirstName() + " " + getLastName());
        String status = null;
        int statusColor = 0;
        switch (getStatus()) {
            case 0:
                status = StringConstant.IStatus.NO_STATUS;
                statusColor = ContextCompat.getColor(context, R.color.gunMetal);
                break;
            case 1:
                status = StringConstant.IStatus.FALLING_BEHIND;
                statusColor = ContextCompat.getColor(context, R.color.tomato);
                break;
            case 2:
                status = StringConstant.IStatus.AT_RISK;
                statusColor = ContextCompat.getColor(context, R.color.tangerine);
                break;
            case 3:
                status = StringConstant.IStatus.On_TRACK;
                statusColor = ContextCompat.getColor(context, R.color.midGreen);
                break;
        }
        holder.txtStatus.setText(status);
        CustomView.drawRoundedCorners(context, statusColor, holder.txtStatus);
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            holder.txtDescription.setText(TextUtils.isEmpty(getMessageBody()) ? "" : Html.fromHtml(getMessageBody(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.txtDescription.setText(TextUtils.isEmpty(getMessageBody()) ? "" : Html.fromHtml(getMessageBody()));
        }

        holder.txtDateTime.setText(HelperUtility.formattedCreatedUTCDateTime(getDateTime()));

        //display image
        HelperUtility.loadProfileImage(context, String.valueOf(getAuthorId()), holder.imgProfile);


    }

    @Override
    public NotesHeaderItem getHeader() {
        return notesHeaderItem;
    }

    @Override
    public void setHeader(NotesHeaderItem header) {

        this.notesHeaderItem = header;
    }

    static final class NotesSectionItemViewHolder extends FlexibleViewHolder {

        private Context context;
        private ImageView imgProfile;
        private TextView txtArchive, txtGoalTitle, txtGoalType, txtStatus, txtName, txtDateTime, txtDescription;
        View rearRightView;
        boolean swiped = false;

        NotesSectionItemViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            this.context = view.getContext();
            imgProfile = itemView.findViewById(R.id.img_notes_list_item_row_profile);
            //txtArchive = itemView.findViewById(R.id.txt_notes_list_archive);

            txtGoalType = itemView.findViewById(R.id.txt_notes_list_item_row_goal_type);
            txtGoalTitle = itemView.findViewById(R.id.txt_notes_list_item_row_goal_title);

            txtStatus = itemView.findViewById(R.id.txt_notes_list_item_row_status);

            txtName = itemView.findViewById(R.id.txt_notes_list_item_row_name);
            txtDateTime = itemView.findViewById(R.id.txt_notes_list_item_row_date_time);
            txtDescription = itemView.findViewById(R.id.txt_notes_list_item_row_description);

            rearRightView = itemView.findViewById(R.id.rl_notes_section_item_rear_right_view);


        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context, "Click on " + " position " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            super.onClick(view);
        }

        @Override
        public View getRearRightView() {
            return rearRightView;
        }

        @Override
        public void scrollAnimators(@NonNull List<Animator> animators, int position, boolean isForward) {

            if (mAdapter.isSelected(position)) {
                AnimatorHelper.slideInFromRightAnimator(animators, itemView, mAdapter.getRecyclerView(), 0.5f);
            }
         /*   else
                AnimatorHelper.slideInFromLeftAnimator(animators, itemView, mAdapter.getRecyclerView(), 0.5f);*/

        }

        @Override
        public void onItemReleased(int position) {
            swiped = (mActionState == ItemTouchHelper.ACTION_STATE_SWIPE);
            super.onItemReleased(position);
        }

    }

    @Override
    public String toString() {
        return "NotesSectionItem[" + super.toString() + "]";
    }


}
