package ness.com.etw.network;


import org.json.JSONObject;

import java.util.List;

public interface INetworkResult<T> {

    void onSuccess(T responseBody, int responseType);

    void onError(String message);


}
