package ness.com.etw.goals.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlanDO {

    @SerializedName("id")
    @Expose
    private Integer planId;
    @SerializedName("title")
    @Expose
    private String title;

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
