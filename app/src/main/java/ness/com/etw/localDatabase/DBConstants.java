package ness.com.etw.localDatabase;


public interface DBConstants {

    String DB_NAME = "etwapp.db";
    int DB_VERSION = 1;

    interface Constants {

        String ASCENDING = " ASC";
        String DESCENDING = " DESC";
    }

    /**
     * Define tables
     */
    interface Tables {

        //tblNotes
        String TBL_NOTES = "tblNotes";
        //tblGoals
        String TBL_GOALS = "tblGoals";
        //tblPlan
        String TBL_PLAN = "tblPlan";

    }

    /**
     * Define Columns Name
     */
    interface Columns {

        String ID = "id"; //authorId
        String CLIENT_ID = "clientId";
        String GOAL_ID = "goalId";
        String GOAL_TITLE = "goalTitle";
        String GOAL_TYPE = "goalType";

        String AUTHOR_ID = "authorId";
        String FIRST_NAME = "firstName";
        String LAST_NAME = "lastName";

        String STATUS = "status";
        String CREATED_DATE = "createdDate";

        String PLAN_ID = "planId";
        String PLAN_TITLE = "planTitle";
        String PROFILE_IMAGE_URL = "profileImageUrl";

        String MESSAGE_BODY = "messageBody";
        String LAST_EVAL_KEY = "lastEvalKey";
        String IS_ARCHIVE = "isArchive";

        String TITLE = "title";
        String GOAL_TYPE_HEADER_ID = "goalTypeHeaderId";
        String ALIGNMENT_GOAL_TYPE = "alignmentGoalType";
        String PERFORMANCE_GOAL_TYPE = "performanceGoalType";
        String DUE_DATE = "dueDate";
        String STATE = "state";
        String GROUP_TITLE = "groupTitle";
        String ROLE_NAME = "roleName";
        String STATE_STATUS = "stateStatus";
        String GOAL_TYPE_ID = "goalTypeId";
        String GROUP_SORT_ID = "groupSortId";

    }

    /**
     * Create Table
     */
    interface CREATE_SQL_TABLE {

        String CREATE_NOTES_TABLE = "CREATE TABLE IF NOT EXISTS "
                + Tables.TBL_NOTES
                + "( " + Columns.ID + " TEXT NOT NULL, "
                + Columns.CLIENT_ID + " TEXT NOT NULL, "
                + Columns.GOAL_ID + " TEXT NOT NULL, "
                + Columns.GOAL_TITLE + " TEXT, "
                + Columns.GOAL_TYPE + " TEXT, "
                + Columns.AUTHOR_ID + " TEXT, "
                + Columns.FIRST_NAME + " TEXT, "
                + Columns.LAST_NAME + " TEXT, "
                + Columns.STATUS + " TEXT, "
                + Columns.CREATED_DATE + " TEXT, "
                + Columns.PLAN_ID + " TEXT, "
                + Columns.PLAN_TITLE + " TEXT, "
                + Columns.PROFILE_IMAGE_URL + " TEXT, "
                + Columns.MESSAGE_BODY + " TEXT, "
                + Columns.LAST_EVAL_KEY + " TEXT, "
                + Columns.IS_ARCHIVE + " INTEGER, "
                + "PRIMARY KEY(" + Columns.ID + "))";


        String CREATE_GOALS_TABLE = "CREATE TABLE IF NOT EXISTS "
                + Tables.TBL_GOALS
                + "( " + Columns.ID + " TEXT NOT NULL, "
                + Columns.GOAL_TYPE_HEADER_ID + " TEXT NULL, "
                + Columns.ALIGNMENT_GOAL_TYPE + " TEXT NULL, "
                + Columns.PERFORMANCE_GOAL_TYPE + " TEXT NULL, "
                + Columns.TITLE + " TEXT NULL, "
                + Columns.GROUP_TITLE + " TEXT NULL, "
                + Columns.ROLE_NAME + " TEXT NULL, "
                + Columns.STATE + " TEXT NULL, "
                + Columns.STATUS + " TEXT NULL, "
                + Columns.DUE_DATE + " TEXT NULL, "
                + Columns.STATE_STATUS + " TEXT NULL, "
                + Columns.GOAL_TYPE_ID + " TEXT NULL, "
                + Columns.GROUP_SORT_ID + " TEXT INTEGER, "
                + "PRIMARY KEY(" + Columns.ID + "))";

        String CREATE_PLAN_TABLE = "CREATE TABLE IF NOT EXISTS "
                + Tables.TBL_PLAN
                + "( " + Columns.ID + " TEXT NOT NULL, "
                + Columns.PLAN_ID + " TEXT NULL, "
                + Columns.PLAN_TITLE + " TEXT NULL, "
                + "PRIMARY KEY(" + Columns.ID + "))";
    }


}
