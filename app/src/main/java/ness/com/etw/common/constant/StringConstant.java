package ness.com.etw.common.constant;


import ness.com.etw.utility.PreferenceUtility;

public class StringConstant {


    public interface IPreference {
        String ETW_PREF = "ETWPref";
        String ETW_PREF_LOGINCRENEDTIAL = "ETWPrefLoginCredential";
        String AUTH_TOKEN_PREF = "AuthTokenPref";
        String USER_UD = "User_ID";
        String USER_FIRST_NAME = "FirstName";
        String USER_LAST_NAME = "LastName";
        String USER_TITLE = "Title";

        String USER_COMPANY_ID = "CompanyID";
        String USER_NAME = "UserName";
    }


    public interface ICommon {
        String AUTHORIZATION = "Authorization=";
        String CONTENT_TYPE = "application/json";
    }

    public interface IGoalConstants {
        String PERFORMANCE_GOAL = "Performance Goals";
        String ROLE_GOAL = "Role Goals";
        String GOAL = "Goal";
        String PLAN = "Plan";

    }

    public interface FragmentTag {

        String HOME_DETAIL_TAG = "homeDetailTag";
        String NOTES_LIST_TAG = "notesListTag";
        String GOALS_LIST_TAG = "goalsListTag";
    }

    public interface IStatus {

        String NO_STATUS = "No Status";
        String FALLING_BEHIND = "Falling Behind";
        String AT_RISK = "At Risk";
        String On_TRACK = "On Track";

        String DRAFT = "Draft";
        String SUSPEND = "Suspended";
        String COMPLETED = "Completed";
    }


}
