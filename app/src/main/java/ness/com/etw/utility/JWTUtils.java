package ness.com.etw.utility;

import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class JWTUtils {


    private static final String TAG = JWTUtils.class.getSimpleName();

    public static String getUserId() {
        String userId = null;
        try {
            // JWT Ecoder / Decoder Initialization
            String access_token = PreferenceUtility.getAuthToken();
            userId = JWTUtils.decoded(access_token);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;

    }

    private static String decoded(String JWTEncoded) throws Exception {

        String userId = null;
        try {
            String[] split = JWTEncoded.split("\\.");
            JSONObject jsonObject = new JSONObject(getJson(split[1]));
            userId = jsonObject.getString("userId");

        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "decoded: " + e);
        }
        return userId;
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }


}
