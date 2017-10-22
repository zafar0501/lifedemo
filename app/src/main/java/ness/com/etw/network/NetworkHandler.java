package ness.com.etw.network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import ness.com.etw.common.model.ErrorMessageDO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class NetworkHandler<T> {

    private static final String TAG = NetworkHandler.class.getSimpleName();
    private INetworkResult iNetworkResultListener;

    public NetworkHandler(INetworkResult iNetworkResultListener) {
        this.iNetworkResultListener = iNetworkResultListener;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Callback EnqueueRequest(final int responseType) {

        return new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {

                if (response.isSuccessful()) {
                    iNetworkResultListener.onSuccess(response.body(), responseType);
                } else {
                    iNetworkResultListener.onError(parseError(response).getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure: ");
                iNetworkResultListener.onError(t.getMessage());
            }
        };

    }

    private static ErrorMessageDO parseError(Response<?> response) {

        ErrorMessageDO errorMessageDO = new ErrorMessageDO();

        try {

            JSONObject jsonObject = new JSONObject(response.errorBody().string());
            String errors = jsonObject.getString("error");
            if (jsonObject.has("message")) {
                String message = jsonObject.getString("message");
                errorMessageDO.setMessage(message);
            } else {
                errorMessageDO.setMessage(errors);
            }

            errorMessageDO.setError(errors);
        } catch (Exception ex) {
            Log.d(TAG, "parseError: " + ex);
        }

        return errorMessageDO;
    }
}
