package ness.com.etw.notes.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by P7110158 ic_goal_unhighlight 6/15/2017.
 */

public class NotesDO {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("subject")
    @Expose
    private Object subject;
    @SerializedName("clientId")
    @Expose
    private String clientId;
    @SerializedName("messageBody")
    @Expose
    private String messageBody;
    @SerializedName("authorId")
    @Expose
    private Integer authorId;
    @SerializedName("author")
    @Expose
    private AuthorDO author;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("parentMessageId")
    @Expose
    private String parentMessageId;
    @SerializedName("draft")
    @Expose
    private Boolean draft;
    @SerializedName("managerEngagement")
    @Expose
    private Object managerEngagement;
    @SerializedName("indirectEngagement")
    @Expose
    private Object indirectEngagement;
    @SerializedName("unreadRecipientIds")
    @Expose
    private List<Integer> unreadRecipientIds = null;
    @SerializedName("archivedRecipientIds")
    @Expose
    private List<Integer> archivedRecipientIds = null;
    @SerializedName("goalId")
    @Expose
    private Integer goalId;
    @SerializedName("goalTitle")
    @Expose
    private String goalTitle;
    @SerializedName("goalType")
    @Expose
    private String goalType;
    @SerializedName("planId")
    @Expose
    private Integer planId;
    @SerializedName("lastEvalKey")
    @Expose
    private String lastEvalKey;
    @SerializedName("goalAssignees")
    @Expose
    private List<Integer> goalAssignees = null;
    @SerializedName("goalParticipants")
    @Expose
    private List<Object> goalParticipants = null;
    @SerializedName("dueDate")
    @Expose
    private String dueDate;
    @SerializedName("planTitle")
    @Expose
    private String planTitle;
    @SerializedName("roleTitle")
    @Expose
    private Object roleTitle;

    private String profileImageUrl;
    private String firstName;
    private String lastName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public AuthorDO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDO author) {
        this.author = author;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getParentMessageId() {
        return parentMessageId;
    }

    public void setParentMessageId(String parentMessageId) {
        this.parentMessageId = parentMessageId;
    }

    public Boolean getDraft() {
        return draft;
    }

    public void setDraft(Boolean draft) {
        this.draft = draft;
    }

    public Object getManagerEngagement() {
        return managerEngagement;
    }

    public void setManagerEngagement(Object managerEngagement) {
        this.managerEngagement = managerEngagement;
    }

    public Object getIndirectEngagement() {
        return indirectEngagement;
    }

    public void setIndirectEngagement(Object indirectEngagement) {
        this.indirectEngagement = indirectEngagement;
    }

    public List<Integer> getUnreadRecipientIds() {
        return unreadRecipientIds;
    }

    public void setUnreadRecipientIds(List<Integer> unreadRecipientIds) {
        this.unreadRecipientIds = unreadRecipientIds;
    }

    public List<Integer> getArchivedRecipientIds() {
        return archivedRecipientIds;
    }

    public void setArchivedRecipientIds(List<Integer> archivedRecipientIds) {
        this.archivedRecipientIds = archivedRecipientIds;
    }

    public Integer getGoalId() {
        return goalId;
    }

    public void setGoalId(Integer goalId) {
        this.goalId = goalId;
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

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public String getLastEvalKey() {
        return lastEvalKey;
    }

    public void setLastEvalKey(String lastEvalKey) {
        this.lastEvalKey = lastEvalKey;
    }

    public List<Integer> getGoalAssignees() {
        return goalAssignees;
    }

    public void setGoalAssignees(List<Integer> goalAssignees) {
        this.goalAssignees = goalAssignees;
    }

    public List<Object> getGoalParticipants() {
        return goalParticipants;
    }

    public void setGoalParticipants(List<Object> goalParticipants) {
        this.goalParticipants = goalParticipants;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public Object getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(Object roleTitle) {
        this.roleTitle = roleTitle;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
