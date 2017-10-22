package ness.com.etw.common.validationMessage;

import android.app.Activity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ness.com.etw.R;
import ness.com.etw.common.presenter.IResultView;
import ness.com.etw.utility.ErrorMessageUtility;


public class ValidationManager {

    private static List<String> validationModelList = null;
    private static ValidationManager validationManager = null;

    public static ValidationManager getValidationMessageInstance() {

        if (validationManager == null) {
            validationManager = new ValidationManager();
            validationModelList = new ArrayList<>();
        }
        return validationManager;
    }

    public static RecyclerView setValidationRecyclerView(Activity activity) {

        RecyclerView recyclerView = activity.findViewById(R.id.recyclerViewValidationMessage);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation()));
        recyclerView.setLayoutManager(linearLayoutManager);
        return recyclerView;
    }

    public void setValidationError(int customErrorCode, IResultView showErrorMessageListener) {

        //on basis of custom code, we will fetch the user defined message in the list
        String message = ErrorMessageUtility.getUserDefinedMessage(customErrorCode);
        validationModelList.remove(message);
        validationModelList.add(message);
        if (validationModelList.size() > 3) {
            //remove the 3rd position
            validationModelList.remove(0);
        }

        showErrorMessageListener.displayErrorList(validationModelList);
    }

    public static void clearValidationMessageList(boolean isClear) {
        if (isClear) {
            validationModelList.clear();
        }
    }

    public static boolean isErrorListHasError() {

        return validationModelList.size() > 0;
    }

    public static List<String> getErrorMessageList() {
        return validationModelList;
    }

}
