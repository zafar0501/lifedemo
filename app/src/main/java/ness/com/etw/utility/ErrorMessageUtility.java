package ness.com.etw.utility;

import java.util.HashMap;

import ness.com.etw.common.validationMessage.AppsValidationMessage;

public class ErrorMessageUtility {

    public static int getCustomErrorCode(String message) {
        return ErrorMessageUtility.mapServerMessageWithCustomCode().get(message.toLowerCase());
    }

    public static String getUserDefinedMessage(int customErrorCode) {
        return ErrorMessageUtility.mapCustomCodeWithCustomMessage().get(customErrorCode);
    }

    private static HashMap<String, Integer> mapServerMessageWithCustomCode() {

        HashMap<String, Integer> mapMessageCode = new HashMap<>();

        mapMessageCode.put(AppsValidationMessage.ICommonError.ERROR_ACCESSING_SERVER, 1000);

        mapMessageCode.put(AppsValidationMessage.ILoginError.NO_MESSAGE_AVAILABLE, 1001);
        mapMessageCode.put(AppsValidationMessage.ILoginError.BAD_CREDENTIALS, 1002);
        mapMessageCode.put(AppsValidationMessage.ILoginError.ERROR_ACCESSING_TO_LOGIN, 1003);

        mapMessageCode.put(AppsValidationMessage.IUserInfoError.USER_ACCOUNT_NOT_FOUND, 1004);
        mapMessageCode.put(AppsValidationMessage.INotesError.NOTES_COULD_NOT_BE_RETRIEVED, 1005);
        mapMessageCode.put(AppsValidationMessage.IArchiveNotesError.ARCHIVE_NOTE_GOAL_MESSAGE_NOT_FOUND, 1006);

        mapMessageCode.put(AppsValidationMessage.IArchiveAllNotesError.ARCHIVE_ALL_NOTE_GOAL_MESSAGE_NOT_FOUND, 1007);
        mapMessageCode.put(AppsValidationMessage.IUnArchiveNotesError.UNARCHIVE_NOTE_GOAL_MESSAGE_NOT_FOUND, 1008);
        mapMessageCode.put(AppsValidationMessage.IGoalError.GOAL_COULD_NOT_RETRIEVED, 1009);

        mapMessageCode.put(AppsValidationMessage.ICommonError.NO_INTERNET, 1010);
        mapMessageCode.put(AppsValidationMessage.ICommonError.TIMEOUT, 1011);

        return mapMessageCode;
    }

    private static HashMap<Integer, String> mapCustomCodeWithCustomMessage() {

        HashMap<Integer, String> mapCodeMessage = new HashMap<>();

        mapCodeMessage.put(1000, AppsValidationMessage.ICommonError.ERROR_ACCESSING_SERVER_CAMEL);

        mapCodeMessage.put(1001, AppsValidationMessage.ILoginError.PLEASE_CHECK_LOGIN_CREDENTIAL);
        mapCodeMessage.put(1002, AppsValidationMessage.ILoginError.PLEASE_CHECK_LOGIN_CREDENTIAL);
        mapCodeMessage.put(1003, AppsValidationMessage.ILoginError.ERROR_ACCESSING_TO_LOGIN_CAMEL);

        mapCodeMessage.put(1004, AppsValidationMessage.IUserInfoError.USER_ACCOUNT_NOT_FOUND_CAMEL);

        mapCodeMessage.put(1005, AppsValidationMessage.INotesError.NOTES_COULD_NOT_BE_RETRIEVED_CAMEL);
        mapCodeMessage.put(1006, AppsValidationMessage.IArchiveNotesError.NOTE_COULD_NOT_ARCHIVED);
        mapCodeMessage.put(1007, AppsValidationMessage.IArchiveAllNotesError.NOTES_COULD_NOT_ARCHIVED);

        mapCodeMessage.put(1008, AppsValidationMessage.IUnArchiveNotesError.NOTES_COULD_NOT_UNARCHIVED);
        mapCodeMessage.put(1009, AppsValidationMessage.IGoalError.GOAL_COULD_NOT_RETRIEVED_CAMEL);

        mapCodeMessage.put(1010, AppsValidationMessage.ICommonError.INTERNET_NOT_AVAILABLE);
        mapCodeMessage.put(1011, AppsValidationMessage.ICommonError.ERROR_ACCESSING_SERVER_CAMEL);

        return mapCodeMessage;
    }


}
