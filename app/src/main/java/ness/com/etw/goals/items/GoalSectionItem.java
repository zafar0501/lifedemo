package ness.com.etw.goals.items;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;
import ness.com.etw.R;
import ness.com.etw.common.constant.StringConstant;
import ness.com.etw.common.customView.CustomView;
import ness.com.etw.goals.model.GoalsAbstractItem;
import ness.com.etw.utility.HelperUtility;

import static ness.com.etw.utility.HelperUtility.formatHyphenToBackSlashDate;


public class GoalSectionItem extends GoalsAbstractItem<GoalSectionItem.GaolsSectionItemViewHolder>
        implements ISectionable<GoalSectionItem.GaolsSectionItemViewHolder, GoalHeaderItem> {


    private GoalHeaderItem goalHeaderItem;
    private static final String UNTITLED_PLAN = "Untitled Plan";

    private GoalSectionItem(String id) {
        super(id);
    }

    public GoalSectionItem(String id, GoalHeaderItem header) {
        this(id);
        this.goalHeaderItem = header;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.goal_section_item_row;
    }

    @Override
    public GaolsSectionItemViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new GaolsSectionItemViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, GaolsSectionItemViewHolder holder, int position, List payloads) {

        Context context = holder.itemView.getContext();
        holder.txtGoalTitle.setText(getGoalTitle());

        String goalType = HelperUtility.displayCamelCaseWords(getGoalType().trim()) + " "
                + StringConstant.IGoalConstants.GOAL;
        if (goalType.contains(StringConstant.IGoalConstants.PLAN)) {
            if (!TextUtils.isEmpty(getPlanTitle())) {
                goalType += ": " + getPlanTitle();
            } else {
                goalType += ": " + UNTITLED_PLAN;
            }
        } else {
            if (!TextUtils.isEmpty(getRoleName())) {
                goalType += ": " + getRoleName();
            }
        }
        holder.txtGoalType.setText(goalType);
        String status = null;
        int stateBlockColor = 0, statusBarColor = 0, state = getState();
        int stateStatus = getStateStatus();
        switch (stateStatus) {
            case 0:
                status = StringConstant.IStatus.NO_STATUS;
                stateBlockColor = ContextCompat.getColor(context, R.color.gunMetal);
                break;
            case 1:
                status = StringConstant.IStatus.FALLING_BEHIND;
                stateBlockColor = ContextCompat.getColor(context, R.color.tomato);
                break;
            case 2:
                status = StringConstant.IStatus.AT_RISK;
                stateBlockColor = ContextCompat.getColor(context, R.color.tangerine);
                break;
            case 3:
                status = StringConstant.IStatus.On_TRACK;
                stateBlockColor = ContextCompat.getColor(context, R.color.midGreen);
                break;
            case 4:
                status = StringConstant.IStatus.DRAFT;
                stateBlockColor = ContextCompat.getColor(context, R.color.draftColor);
                break;
            case 6:
                status = StringConstant.IStatus.SUSPEND;
                stateBlockColor = ContextCompat.getColor(context, R.color.suspendedColor);
                //status color
                break;
            case 7:
                status = StringConstant.IStatus.COMPLETED;
                stateBlockColor = ContextCompat.getColor(context, R.color.completedColor);
                //status color
                break;
        }


        holder.txtStateBlock.setText(status);
        holder.txtStateBlock.setBackgroundColor(stateBlockColor);

        if (stateStatus != 4) {
            holder.txtStatusBar.setBackgroundColor(getStatusBarColor(context));
        } else {
            holder.txtStatusBar.setBackgroundColor(stateBlockColor);
        }

        CustomView.drawRoundedCorners(context, stateBlockColor, holder.txtStateBlock);
        String dueDate = getDueDate();
        if (!TextUtils.isEmpty(dueDate)) {
            holder.txtDueDate.setText(String.format(Locale.US, "%1$s : %2$s", "DUE", formatHyphenToBackSlashDate(dueDate)));
            holder.txtDueDate.setVisibility(View.VISIBLE);
        } else {
            holder.txtDueDate.setVisibility(View.GONE);
        }


    }

    @Override
    public GoalHeaderItem getHeader() {
        return goalHeaderItem;
    }

    @Override
    public void setHeader(GoalHeaderItem header) {
        this.goalHeaderItem = header;
    }

    static final class GaolsSectionItemViewHolder extends FlexibleViewHolder {

        private Context context;
        private TextView txtGoalTitle, txtGoalType, txtStatusBar, txtStateBlock, txtDueDate;


        GaolsSectionItemViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            this.context = view.getContext();

            txtGoalType = itemView.findViewById(R.id.txt_goal_section_item_row_goal_type);
            txtGoalTitle = itemView.findViewById(R.id.txt_goal_section_item_row_goal_title);

            txtStatusBar = itemView.findViewById(R.id.txt_goal_section_item_row_status);
            txtStateBlock = itemView.findViewById(R.id.txt_goal_section_item_row_state);

            txtDueDate = itemView.findViewById(R.id.txt_goal_section_item_row_due_date);

        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context, "Click on " + " position " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            super.onClick(view);
        }

    }

    @Override
    public String toString() {
        return "GoalsSectionItem[" + super.toString() + "]";
    }

    private int getStatusBarColor(Context context) {

        switch (getStatus()) {
            case 0:
                return ContextCompat.getColor(context, R.color.gunMetal);
            case 1:
                return ContextCompat.getColor(context, R.color.tomato);
            case 2:
                return ContextCompat.getColor(context, R.color.tangerine);
            case 3:
                return ContextCompat.getColor(context, R.color.midGreen);
        }
        return 4;
    }
}


//region
/*        //state=2 Active
        if (state == 2) {
            switch (getStatus()) {
                //used same for bar/block both
                case 0:
                    status = StringConstant.IStatus.NO_STATUS;
                    stateBlockColor = ContextCompat.getColor(context, R.color.gunMetal);
                    break;
                case 1:
                    status = StringConstant.IStatus.FALLING_BEHIND;
                    stateBlockColor = ContextCompat.getColor(context, R.color.tomato);
                    break;
                case 2:
                    status = StringConstant.IStatus.AT_RISK;
                    stateBlockColor = ContextCompat.getColor(context, R.color.tangerine);
                    break;
                case 3:
                    status = StringConstant.IStatus.On_TRACK;
                    stateBlockColor = ContextCompat.getColor(context, R.color.midGreen);
                    break;

            }

        } else {

            switch (state) {

                case 1:
                    status = StringConstant.IStatus.DRAFT;
                    stateBlockColor = ContextCompat.getColor(context, R.color.draftColor);
                    break;
                case 3:
                    status = StringConstant.IStatus.SUSPEND;
                    stateBlockColor = ContextCompat.getColor(context, R.color.suspendedColor);
                    //status color
                    break;
                case 4:
                    status = StringConstant.IStatus.COMPLETED;
                    stateBlockColor = ContextCompat.getColor(context, R.color.completedColor);
                    //status color
                    break;
            }

        }*/

//endregion