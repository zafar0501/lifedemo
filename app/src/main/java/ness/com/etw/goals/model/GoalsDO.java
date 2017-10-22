package ness.com.etw.goals.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class GoalsDO {

    @SerializedName("id")
    @Expose
    private Integer id;

    private Integer goalTypeHeaderId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("state")
    @Expose
    private Integer state;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("dueDate")
    @Expose
    private String dueDate;
    @SerializedName("alignmentGoalType")
    @Expose
    private String alignmentGoalType;
    @SerializedName("performanceGoalType")
    @Expose
    private String performanceGoalType;
    @SerializedName("plan")
    @Expose
    private PlanDO plan;
    @SerializedName("groupTitle")
    @Expose
    private String groupTitle;
    @SerializedName("roleName")
    @Expose
    private String roleName;
    @SerializedName("groupSortId")
    @Expose
    private Integer groupSortId;
    private String planTitle;
    private Integer stateStatus;
    private HashMap<String, Integer> totalGoalSection;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoalTypeHeaderId() {
        return goalTypeHeaderId;
    }

    public void setGoalTypeHeaderId(Integer goalTypeHeaderId) {
        this.goalTypeHeaderId = goalTypeHeaderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getAlignmentGoalType() {
        return alignmentGoalType;
    }

    public void setAlignmentGoalType(String alignmentGoalType) {
        this.alignmentGoalType = alignmentGoalType;
    }

    public String getPerformanceGoalType() {
        return performanceGoalType;
    }

    public void setPerformanceGoalType(String performanceGoalType) {
        this.performanceGoalType = performanceGoalType;
    }

    public PlanDO getPlan() {
        return plan;
    }

    public void setPlan(PlanDO plan) {
        this.plan = plan;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public Integer getStateStatus() {
        return stateStatus;
    }

    public void setStateStatus(Integer stateStatus) {
        this.stateStatus = stateStatus;
    }

    public HashMap<String, Integer> getTotalGoalSection() {
        return totalGoalSection;
    }

    public void setTotalGoalSection(HashMap<String, Integer> totalGoalSection) {
        this.totalGoalSection = totalGoalSection;
    }

    public Integer getGroupSortId() {
        return groupSortId;
    }

    public void setGroupSortId(Integer groupSortId) {
        this.groupSortId = groupSortId;
    }

    private void mockPraseJSON() {
        String mockJson = "{\"userId\":2096,\"alignmentGoals\":[{\"id\":7212,\"clientId\":1,\"createdById\":1,\"plan\":null,\"title\":\"Test Cultural Goal 24th March 2017\",\"state\":2,\"status\":0,\"privateGoal\":null,\"grouped\":null,\"enableEvaluation\":null,\"weight\":null,\"dueDate\":\"2017-03-30\",\"createdDate\":\"2017-05-25T06:04:10.000+0000\",\"scoringCriteria\":null,\"goalGroup\":57,\"groupTitle\":\"Mixed Goal 24th March 2017\",\"groupSortId\":1,\"updateInterval\":null,\"team\":null,\"supportedPerformanceGoals\":[],\"kpis\":[],\"assignees\":[2096],\"participants\":[],\"evaluatedParticipants\":[],\"viewers\":[],\"s3Resources\":[],\"goalLinks\":[],\"masterGoal\":{\"id\":6257,\"sortId\":null,\"s3Resources\":[],\"goalLinks\":[],\"isAdmin\":false},\"primaryKpiId\":null,\"messageCount\":27,\"alignmentGoalType\":\"CULTURE\",\"performanceGoalType\":null,\"performanceGoalRoles\":[],\"roleId\":2,\"roleName\":\"internal User\",\"sortId\":null,\"userEditable\":null}],\"performanceGoals\":[{\"id\":809,\"clientId\":1,\"createdById\":2096,\"plan\":null,\"title\":\"Test Personal Supporting Goal\",\"state\":1,\"status\":0,\"privateGoal\":null,\"grouped\":null,\"enableEvaluation\":null,\"weight\":null,\"dueDate\":null,\"createdDate\":\"2017-02-22T12:08:16.000+0000\",\"scoringCriteria\":null,\"goalGroup\":null,\"groupTitle\":null,\"groupSortId\":null,\"updateInterval\":null,\"team\":null,\"supportedPerformanceGoals\":[{\"id\":621,\"clientId\":\"1\",\"plan\":{\"id\":50,\"title\":\"Test 16th Feb Plan Test 16th Feb Plan Test 16th Feb Plan Test 16th Feb Plan\",\"ownerSet\":null,\"participantSet\":null},\"title\":\"W Test Goal\",\"state\":2,\"status\":1,\"privateGoal\":null,\"grouped\":null,\"enableEvaluation\":null,\"weight\":null,\"dueDate\":null,\"scoringCriteria\":null,\"goalGroup\":null,\"groupTitle\":null,\"groupSortId\":null,\"updateInterval\":null,\"team\":null,\"performanceGoalType\":\"PLAN\"}],\"kpis\":[],\"assignees\":[2096],\"participants\":[],\"evaluatedParticipants\":[],\"viewers\":[],\"s3Resources\":[],\"goalLinks\":[],\"masterGoal\":null,\"primaryKpiId\":null,\"messageCount\":0,\"alignmentGoalType\":null,\"performanceGoalType\":\"PERSONAL\",\"performanceGoalRoles\":[],\"roleId\":null,\"roleName\":null,\"sortId\":null,\"userEditable\":null},{\"id\":4056,\"clientId\":1,\"createdById\":1,\"plan\":null,\"title\":\"Test Role Goal 24th March 2017\",\"state\":2,\"status\":1,\"privateGoal\":null,\"grouped\":null,\"enableEvaluation\":null,\"weight\":null,\"dueDate\":null,\"createdDate\":\"2017-05-25T06:04:09.000+0000\",\"scoringCriteria\":null,\"goalGroup\":57,\"groupTitle\":\"Mixed Goal 24th March 2017\",\"groupSortId\":1,\"updateInterval\":null,\"team\":null,\"supportedPerformanceGoals\":[],\"kpis\":[],\"assignees\":[2096],\"participants\":[],\"evaluatedParticipants\":[],\"viewers\":[],\"s3Resources\":[],\"goalLinks\":[],\"masterGoal\":{\"id\":3385,\"sortId\":null,\"s3Resources\":[],\"goalLinks\":[],\"isAdmin\":false},\"primaryKpiId\":null,\"messageCount\":13,\"alignmentGoalType\":null,\"performanceGoalType\":\"ROLE\",\"performanceGoalRoles\":[],\"roleId\":2,\"roleName\":\"internal User\",\"sortId\":null,\"userEditable\":null}],\"allGoals\":[{\"id\":622,\"clientId\":1,\"createdById\":1,\"plan\":{\"id\":50,\"title\":\"Test 16th Feb Plan Test 16th Feb Plan Test 16th Feb Plan Test 16th Feb Plan\",\"ownerSet\":null,\"participantSet\":null},\"title\":\"X Test Goal\",\"state\":2,\"status\":0,\"privateGoal\":null,\"grouped\":null,\"enableEvaluation\":null,\"weight\":null,\"dueDate\":null,\"createdDate\":\"2017-02-22T08:16:39.000+0000\",\"scoringCriteria\":null,\"goalGroup\":null,\"groupTitle\":null,\"groupSortId\":null,\"updateInterval\":null,\"team\":null,\"supportedPerformanceGoals\":[],\"kpis\":[],\"assignees\":[2096],\"participants\":[],\"evaluatedParticipants\":[],\"viewers\":[],\"s3Resources\":[],\"goalLinks\":[],\"masterGoal\":null,\"primaryKpiId\":null,\"messageCount\":17,\"alignmentGoalType\":null,\"performanceGoalType\":\"PLAN\",\"performanceGoalRoles\":[],\"roleId\":null,\"roleName\":null,\"sortId\":null,\"userEditable\":null},{\"id\":624,\"clientId\":1,\"createdById\":1,\"plan\":{\"id\":50,\"title\":\"Test 16th Feb Plan Test 16th Feb Plan Test 16th Feb Plan Test 16th Feb Plan\",\"ownerSet\":null,\"participantSet\":null},\"title\":\"Z Test Goal\",\"state\":2,\"status\":1,\"privateGoal\":null,\"grouped\":null,\"enableEvaluation\":null,\"weight\":null,\"dueDate\":null,\"createdDate\":\"2017-02-22T08:17:21.000+0000\",\"scoringCriteria\":null,\"goalGroup\":null,\"groupTitle\":null,\"groupSortId\":null,\"updateInterval\":null,\"team\":null,\"supportedPerformanceGoals\":[],\"kpis\":[],\"assignees\":[],\"participants\":[],\"evaluatedParticipants\":[],\"viewers\":[2096],\"s3Resources\":[],\"goalLinks\":[],\"masterGoal\":null,\"primaryKpiId\":null,\"messageCount\":0,\"alignmentGoalType\":null,\"performanceGoalType\":\"PLAN\",\"performanceGoalRoles\":[],\"roleId\":null,\"roleName\":null,\"sortId\":null,\"userEditable\":null},{\"id\":620,\"clientId\":1,\"createdById\":1,\"plan\":{\"id\":50,\"title\":\"Test 16th Feb Plan Test 16th Feb Plan Test 16th Feb Plan Test 16th Feb Plan\",\"ownerSet\":null,\"participantSet\":null},\"title\":\"Test Goal 01\",\"state\":3,\"status\":1,\"privateGoal\":null,\"grouped\":null,\"enableEvaluation\":null,\"weight\":null,\"dueDate\":null,\"createdDate\":\"2017-02-22T08:13:43.000+0000\",\"scoringCriteria\":null,\"goalGroup\":null,\"groupTitle\":null,\"groupSortId\":null,\"updateInterval\":null,\"team\":null,\"supportedPerformanceGoals\":[],\"kpis\":[],\"assignees\":[2096],\"participants\":[],\"evaluatedParticipants\":[],\"viewers\":[],\"s3Resources\":[],\"goalLinks\":[],\"masterGoal\":null,\"primaryKpiId\":null,\"messageCount\":5,\"alignmentGoalType\":null,\"performanceGoalType\":\"PLAN\",\"performanceGoalRoles\":[],\"roleId\":null,\"roleName\":null,\"sortId\":null,\"userEditable\":null}]}";
    }
}
