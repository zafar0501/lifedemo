package ness.com.etw.common.validationMessage;


public class AppsValidationMessage {

    //Error Message From Server
   /* public static final String serverError = "Error accessing server.";
    public static final String unknownGoalType = "This message could not be saved.";
    public static final String addNoteError = "Unknown goal type";
    public static final String unexpectedEndOfInput = "Unexpected end-of-input: was expecting closing quote for a string value";
*/
    //Error Message From Server
    public interface ILoginError {
        String NO_MESSAGE_AVAILABLE = "no message available";
        String BAD_CREDENTIALS = "bad credentials";
        String ERROR_ACCESSING_TO_LOGIN = "error accessing server to login";
        String ERROR_ACCESSING_TO_LOGIN_CAMEL = "Error Accessing Server to Login";
        String PLEASE_CHECK_LOGIN_CREDENTIAL = "Please check your login credentials";
    }

    public interface IUserInfoError {
        String USER_ACCOUNT_NOT_FOUND = "user account not found";
        String USER_ACCOUNT_NOT_FOUND_CAMEL = "User Account Not Found";
    }

    public interface INotesError {
        String NOTES_COULD_NOT_BE_RETRIEVED = "notes could not be retrieved";
        String NOTES_COULD_NOT_BE_RETRIEVED_CAMEL = "Notes Could Not Be Retrieved";
    }


    public interface IArchiveNotesError {
        String ARCHIVE_NOTE_GOAL_MESSAGE_NOT_FOUND = "goal message not found";
        String NOTE_COULD_NOT_ARCHIVED = "Note Could Not be Archived";

    }

    public interface IArchiveAllNotesError {
        String ARCHIVE_ALL_NOTE_GOAL_MESSAGE_NOT_FOUND = "goal message not found";
        String NOTES_COULD_NOT_ARCHIVED = "Notes Could Not Be Archived";
    }

    public interface IUnArchiveNotesError {
        String UNARCHIVE_NOTE_GOAL_MESSAGE_NOT_FOUND = "goal message not found";
        String NOTES_COULD_NOT_UNARCHIVED = "Note Could Not Be Unarchived";
    }

    public interface IGoalError {
        String GOAL_COULD_NOT_RETRIEVED = "goals could not be retrieved";
        String GOAL_COULD_NOT_RETRIEVED_CAMEL = "Goals Could Not Be Retrieved";
    }


    public interface ICommonError {
        String ERROR_ACCESSING_SERVER = "error accessing server";
        String ERROR_ACCESSING_SERVER_CAMEL = "Error Accessing Server";
        String NO_INTERNET = "internet connection not available";
        String INTERNET_NOT_AVAILABLE = "Internet Connection not available";
        String TIMEOUT = "timeout";

    }

   /* // Error Message struct UserInfo
    public static final String userNotFound = "User account not found";

    //Error Message ForgotPassword
    public static final String notFoundEmailId = "The email address you entered does not match any user accounts, please see your system admin for further help.";

    // Error Message Notes
    public static final String notesNotRetrieve = "Notes could not be retrieved.";
    public static final String archiveNote = "Goal message not found";
    public static final String archiveAllNotes = "Goal message not found";
    public static final String unarchiveNote = "Goal message not found";

    // Error Message Notes

    public static final String goalNotRetrieve = "Goals could not be retrieved.";
    public static final String goalNotesNotRetrieve = "Goal notes could not be retrieved.";*/


    // Custom Message to show to user

    //Error Message Authentication
    public static final String loginNoService = "Error accessing server to login.";
    public static final String badCredentials = "Please check your login credentials.";
    public static final String touchIdDisabled = "Touch ID disabled.";
    public static final String touchIdEnabled = "Touch ID enabled.";

    public static final String touchIdPasscodeNotSetTitle = "Touch ID Isn't Set Up on This Device";
    public static final String touchIdPasscodeNotSetMessage = "To set up Touch ID on this device, go to Settings > Touch ID & Passcode and add a valid fingerprint.";


    // Error Message PasswordReset
    public static final String successTitle = "Your Request Has Been Sent!";
    public static final String successMessage = "Please check your email inbox";
    public static final String error = "There was a problem resetting your password. Please try again.";
    public static final String invalidEmail = "Please enter a valid email address";


    // Error Message EnableTouchID
    public static final String Enable_title = "Verify with Password";
    public static final String Enable_message = "To enable Touch ID please revalidate your password.";
    public static final String Enable_success = "Touch ID enabled!";

    // Error Message DisableTouchID
    public static final String Disable_title = "Alert";
    public static final String Disable_message = "Touch ID is not available";
    public static final String Disable_success = "Touch ID disabled.";

    //Error Messgae Reachability
    public static final String noInternet = "Internet Connection not available.";
    public static final String offline = "Internet connection lost. Please reconnect.";

    //Error Messgae Notification
    public static final String noSubscription = "You are not subscribed to receive any Notifications.";

    //Error Messgae RequestTimedOut
    public static final String title = "Request Timed Out.";
    public static final String message = "Internet connection lost. Please reconnect.";

    //Error Messgae ServerError
    public static final String ServerError_message = "Server error. Please reconnect.";
    public static final String ServerError_login = "Error accessing server to login.";
    public static final String ServerError_access = "Error accessing server.";

    //Error Messgae ServerError
    public static final String ServerError_retrieve = "Goals could not be retrieved.";
    public static final String retrieveNotes = "Goal notes could not be retrieved.";
    public static final String nolongerAvailable = "The goal is no longer available";

    //Error Messgae StatusUpdate
    public static final String StatusUpdate_inactive = "Goal must be in \"Active\" to update status.";
    public static final String StatusUpdate_permission = "You do not have permission to update.";

    //Error Messgae AddNote
    public static final String AddNote_inactive = "Goal must be in \"Active\" to add note.";
    public static final String AddNote_permission = "You do not have permission to add.";

    //Error Messgae logout
    public static final String confirmation = "Are you sure you want to delete the note and logout ?";

    //Error Messgae NotesError
    public static final String retrieve = "Notes could not be retrieved.";
    public static final String archive = "Note could not be archived.";
    public static final String archiveAll = "Notes could not be archived.";
    public static final String unarchive = "Note could not be unarchived.";
    public static final String corrupted = "Note could not be converted to HTML.";

    //Error Messgae GenericError
    public static final String retry = "Something went wrong. Please retry";


}
