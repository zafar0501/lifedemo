package ness.com.etw.goals.model;

import android.view.View;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import ness.com.etw.notes.model.NotesAbstractItem;


public abstract class GoalsAbstractItem<VH extends FlexibleViewHolder>
        extends AbstractFlexibleItem<VH> {

    protected String id, goalTitle, goalType, title, planTitle, dueDate, groupTitle, roleName;
    private Integer status, state, stateStatus;


    public GoalsAbstractItem(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof GoalsAbstractItem) {
            GoalsAbstractItem inItem = (GoalsAbstractItem) inObject;
            return this.id.equals(inItem.id);
        }
        return false;
    }

    public String getGoalTitle() {
        return goalTitle;
    }

    public void setGoalTitle(String goalTitle) {
        this.goalTitle = goalTitle;
    }

    public String getGoalType() {
        return goalType;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public Integer getStateStatus() {
        return stateStatus;
    }

    public void setStateStatus(Integer stateStatus) {
        this.stateStatus = stateStatus;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "id=" + id;
    }
}
