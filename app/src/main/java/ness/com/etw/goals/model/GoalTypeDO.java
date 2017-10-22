package ness.com.etw.goals.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class GoalTypeDO {

    @SerializedName("alignmentGoals")
    @Expose
    private List<AlignmentGoalsDO> alignmentGoals = null;
    @SerializedName("performanceGoals")
    @Expose
    private List<PerformanceGoalsDO> performanceGoals = null;
    @SerializedName("allGoals")
    @Expose
    private List<AllGoalsDO> allGoals = null;

    public List<AlignmentGoalsDO> getAlignmentGoals() {
        return alignmentGoals;
    }

    public void setAlignmentGoals(List<AlignmentGoalsDO> alignmentGoals) {
        this.alignmentGoals = alignmentGoals;
    }

    public List<PerformanceGoalsDO> getPerformanceGoals() {
        return performanceGoals;
    }

    public void setPerformanceGoals(List<PerformanceGoalsDO> performanceGoals) {
        this.performanceGoals = performanceGoals;
    }

    public List<AllGoalsDO> getAllGoals() {
        return allGoals;
    }

    public void setAllGoals(List<AllGoalsDO> allGoals) {
        this.allGoals = allGoals;
    }
}
