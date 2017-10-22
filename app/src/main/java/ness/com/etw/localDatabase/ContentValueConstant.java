package ness.com.etw.localDatabase;


import android.content.ContentValues;
import android.text.TextUtils;

import ness.com.etw.common.constant.StringConstant;
import ness.com.etw.goals.model.AllGoalsDO;
import ness.com.etw.goals.model.PlanDO;
import ness.com.etw.notes.model.NotesDO;
import ness.com.etw.utility.HelperUtility;

class ContentValueConstant {

    private static final String ROLE = "role";
    private static final String PLAN = "plan";
    private static final String MANGER_ASSIGNED = "manager_assigned";
    private static final String PERSONAL = "personal";
    private static final String UNTITLED_GOAL = "Untitled Goal";

    ContentValues insertNotesCV(NotesDO notesDO, int isArchive) {

        ContentValues cv = new ContentValues();
        cv.put(DBConstants.Columns.ID, notesDO.getId());
        cv.put(DBConstants.Columns.CLIENT_ID, notesDO.getClientId());
        cv.put(DBConstants.Columns.GOAL_ID, notesDO.getGoalId());
        cv.put(DBConstants.Columns.GOAL_TITLE, notesDO.getGoalTitle());
        cv.put(DBConstants.Columns.GOAL_TYPE, HelperUtility.displayCamelCaseWords(notesDO.getGoalType().replace("_", " ")) + "Goal");

        cv.put(DBConstants.Columns.AUTHOR_ID, notesDO.getAuthorId());
        cv.put(DBConstants.Columns.FIRST_NAME, notesDO.getAuthor().getFirstName());
        cv.put(DBConstants.Columns.LAST_NAME, notesDO.getAuthor().getLastName());


        cv.put(DBConstants.Columns.STATUS, notesDO.getStatus());
        cv.put(DBConstants.Columns.CREATED_DATE, notesDO.getCreatedDate());
        cv.put(DBConstants.Columns.PLAN_ID, notesDO.getPlanId());
        cv.put(DBConstants.Columns.PLAN_TITLE, notesDO.getPlanTitle());
        cv.put(DBConstants.Columns.PROFILE_IMAGE_URL, notesDO.getProfileImageUrl());
        cv.put(DBConstants.Columns.MESSAGE_BODY, notesDO.getMessageBody());
        cv.put(DBConstants.Columns.LAST_EVAL_KEY, notesDO.getLastEvalKey());
        cv.put(DBConstants.Columns.IS_ARCHIVE, isArchive);

        return cv;
    }

    ContentValues insertGoalCV(AllGoalsDO goalsDO) {

        ContentValues cv = new ContentValues();
        cv.put(DBConstants.Columns.ID, goalsDO.getId());
        String strGoalTitle = !TextUtils.isEmpty(goalsDO.getTitle()) ? goalsDO.getTitle() : UNTITLED_GOAL;
        cv.put(DBConstants.Columns.TITLE, strGoalTitle);

        String goalType = !TextUtils.isEmpty(goalsDO.getPerformanceGoalType()) ? goalsDO.getPerformanceGoalType().toLowerCase() : null;
        if (!TextUtils.isEmpty(goalType)) {

            //Plan, Manager_assign, Personal
            if (TextUtils.equals(goalType, ROLE)) {
                //Role Goal
                cv.put(DBConstants.Columns.GOAL_TYPE_HEADER_ID, 12);
                cv.put(DBConstants.Columns.GOAL_TYPE_ID, 4);
                cv.put(DBConstants.Columns.GROUP_TITLE, StringConstant.IGoalConstants.ROLE_GOAL);
            } else {
                //Performance Goal
                cv.put(DBConstants.Columns.GOAL_TYPE_HEADER_ID, 11);
                cv.put(DBConstants.Columns.GROUP_TITLE, StringConstant.IGoalConstants.PERFORMANCE_GOAL);
                if (TextUtils.equals(goalType, PLAN)) {
                    cv.put(DBConstants.Columns.GOAL_TYPE_ID, 1);
                } else if (TextUtils.equals(goalType, MANGER_ASSIGNED)) {
                    cv.put(DBConstants.Columns.GOAL_TYPE_ID, 2);
                } else if (TextUtils.equals(goalType, PERSONAL)) {
                    cv.put(DBConstants.Columns.GOAL_TYPE_ID, 3);
                }
            }
            cv.put(DBConstants.Columns.PERFORMANCE_GOAL_TYPE, goalsDO.getPerformanceGoalType().replace("_", " "));
        } else {

            if (!TextUtils.isEmpty(goalsDO.getAlignmentGoalType())) {
                goalType = goalsDO.getAlignmentGoalType().toLowerCase();
            } else {
                goalType = null;
            }
            if (TextUtils.equals(goalType, ROLE)) {
                //Role Goal
                cv.put(DBConstants.Columns.GOAL_TYPE_HEADER_ID, 12);
                cv.put(DBConstants.Columns.GOAL_TYPE_ID, 4);
                cv.put(DBConstants.Columns.GROUP_TITLE, StringConstant.IGoalConstants.ROLE_GOAL);
            } else {
                // Show Different header as per the group title
                cv.put(DBConstants.Columns.GOAL_TYPE_HEADER_ID, 13);
                cv.put(DBConstants.Columns.GOAL_TYPE_ID, 5);
                cv.put(DBConstants.Columns.GROUP_TITLE, goalsDO.getGroupTitle());
            }
            cv.put(DBConstants.Columns.ALIGNMENT_GOAL_TYPE, goalsDO.getAlignmentGoalType().replace("_", " "));
        }

        cv.put(DBConstants.Columns.GROUP_SORT_ID, goalsDO.getGroupSortId());
        cv.put(DBConstants.Columns.ROLE_NAME, goalsDO.getRoleName());
        int state, status, stateStatus = -1;

        state = goalsDO.getState();
        status = goalsDO.getStatus();

        cv.put(DBConstants.Columns.STATE, state);
        cv.put(DBConstants.Columns.STATUS, status);

        if (state == 2) {
            switch (status) {
                case 0://No status
                    stateStatus = 0;
                    break;
                case 1://Falling behind
                    stateStatus = 1;
                    break;
                case 2://At rish
                    stateStatus = 2;
                    break;
                case 3://On Track
                    stateStatus = 3;
                    break;
            }
        } else {

            switch (state) {
                case 1://Draft
                    stateStatus = 4;
                    break;
                //2 = Active
                case 3://Suspended
                    stateStatus = 6;
                    break;
                case 4://Completed
                    stateStatus = 7;
                    break;
            }
        }

        cv.put(DBConstants.Columns.STATE_STATUS, stateStatus);
        cv.put(DBConstants.Columns.DUE_DATE, goalsDO.getDueDate());
        return cv;
    }

    ContentValues insertGoalPlanCV(PlanDO planDO, String id) {

        ContentValues cv = new ContentValues();
        cv.put(DBConstants.Columns.ID, id);
        cv.put(DBConstants.Columns.PLAN_ID, planDO.getPlanId());
        cv.put(DBConstants.Columns.PLAN_TITLE, planDO.getTitle());
        return cv;
    }
}


/*


        String goalType = goalsDO.getPerformanceGoalType();
        if (!TextUtils.isEmpty(goalType)) {

            //Plan, Manager_assign, Personal
            if (TextUtils.equals(goalType.toLowerCase(), "role")) {
                cv.put(DBConstants.Columns.GOAL_TYPE_HEADER_ID, 12);
            } else {
                cv.put(DBConstants.Columns.GOAL_TYPE_HEADER_ID, 11);
            }

            cv.put(DBConstants.Columns.PERFORMANCE_GOAL_TYPE, goalsDO.getPerformanceGoalType().replace("_", " "));
        } else {

            goalType = goalsDO.getAlignmentGoalType();
            if (TextUtils.equals(goalType.toLowerCase(), "role")) {
                cv.put(DBConstants.Columns.GOAL_TYPE_HEADER_ID, 12);
            } else {
                // Show Different header as per the group title
                cv.put(DBConstants.Columns.GOAL_TYPE_HEADER_ID, 13);
            }
            cv.put(DBConstants.Columns.ALIGNMENT_GOAL_TYPE, goalsDO.getAlignmentGoalType().replace("_", " "));
        }

        cv.put(DBConstants.Columns.ROLE_NAME, goalsDO.getRoleName());
        int state, status, stateStatus = -1;

        state = goalsDO.getState();
        status = goalsDO.getStatus();

        cv.put(DBConstants.Columns.STATE, state);
        cv.put(DBConstants.Columns.STATUS, status);


        if (state == 2) {
            switch (status) {
                case 0:
                    stateStatus = 0;
                    break;
                case 1:
                    stateStatus = 1;
                    break;
                case 2:
                    stateStatus = 2;
                    break;
                case 3:
                    stateStatus = 3;
                    break;
            }
        } else {

            switch (state) {
                case 1:
                    stateStatus = 4;
                    break;
                //2 = Active
                case 3:
                    stateStatus = 6;
                    break;
                case 4:
                    stateStatus = 7;
                    break;
            }
        }

        cv.put(DBConstants.Columns.STATE_STATUS, stateStatus);

 */
