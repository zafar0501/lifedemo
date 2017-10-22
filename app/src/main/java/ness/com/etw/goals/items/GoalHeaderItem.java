package ness.com.etw.goals.items;


import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import ness.com.etw.R;

public class GoalHeaderItem extends AbstractHeaderItem<GoalHeaderItem.HeaderViewHolder> {

    private String strGoalTypeGroupTitle;
    private int goalTypeHeaderId, totalGoalSectionItem;
    private String strGoalTypeHeader;

    public GoalHeaderItem(String goalTypeTitle, int totalGoalSectionItem) {
        this.strGoalTypeHeader = goalTypeTitle;
        this.totalGoalSectionItem = totalGoalSectionItem;
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof GoalHeaderItem) {
            GoalHeaderItem inItem = (GoalHeaderItem) inObject;
            return TextUtils.equals(this.strGoalTypeHeader.toLowerCase(),
                    inItem.getStrGoalTypeHeader().toLowerCase());
        }
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.goal_header_item_row;
    }

    @Override
    public HeaderViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new GoalHeaderItem.HeaderViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, HeaderViewHolder holder, int position, List payloads) {
        String strGoalHeader = getStrGoalTypeHeader().length() >= 35 ? getStrGoalTypeHeader().substring(0, 35) + "..." : getStrGoalTypeHeader();
        String goalHeader = String.format(Locale.US, "%1$s (%2$s)", strGoalHeader.toUpperCase(), getTotalGoalSectionItem());
        holder.txtGoalHeader.setText(goalHeader);
    }

    public String getStrGoalTypeGroupTitle() {
        return strGoalTypeGroupTitle;
    }

    public void setStrGoalTypeGroupTitle(String strGoalTypeGroupTitle) {
        this.strGoalTypeGroupTitle = strGoalTypeGroupTitle;
    }

    public int getGoalTypeHeaderId() {
        return goalTypeHeaderId;
    }

    public void setGoalTypeHeaderId(int goalTypeHeaderId) {
        this.goalTypeHeaderId = goalTypeHeaderId;
    }

    public String getStrGoalTypeHeader() {
        return strGoalTypeHeader;
    }

    public void setStrGoalTypeHeader(String strGoalTypeHeader) {
        this.strGoalTypeHeader = strGoalTypeHeader;
    }

    static class HeaderViewHolder extends FlexibleViewHolder {

        TextView txtGoalHeader;

        HeaderViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter, true);
            txtGoalHeader = view.findViewById(R.id.txt_goal_header_item_row_goal_type);

        }
    }

    public int getTotalGoalSectionItem() {
        return totalGoalSectionItem;
    }

    public void setTotalGoalSectionItem(int totalGoalSectionItem) {
        this.totalGoalSectionItem = totalGoalSectionItem;
    }

    @Override
    public String toString() {
        return "GoalHeaderItem[" + goalTypeHeaderId + "]";
    }

}
