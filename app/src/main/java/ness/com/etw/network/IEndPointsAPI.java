package ness.com.etw.network;


import java.util.List;

import ness.com.etw.authenticate.model.AuthenticateDO;
import ness.com.etw.authenticate.model.UserAccountInfo;
import ness.com.etw.goals.model.GoalTypeDO;
import ness.com.etw.notes.model.NotesDO;
import ness.com.etw.utility.HelperUtility;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IEndPointsAPI {

    //Login
    @Headers({
            "Content-Type: application/json"
    })
    @POST("useraccount/login")
    Call<AuthenticateDO> loginAPI(@Header("ETWClientId") String companyId, @Header("Authorization") String authorization);


    //UsaerInfo
    @GET("useraccount/v1/users/" + "{fullUrl}" + "?details=true")
    Call<UserAccountInfo> callUserInfoAPI(@Path(value = "fullUrl") String fullUrl, @Header("Accept") String contentType, @Header("Cookie") String cookie);


    //UsaerProfilePics
    @GET("useraccount/v1/users/" + "{fullUrl}" + "?details=true")
    Call<UserAccountInfo> callUserProfilePicsAPI(@Path(value = "fullUrl") String fullUrl, @Header("Accept") String contentType, @Header("Cookie") String cookie);


    //Retrieve UnRead Notes
    @GET("communication/v1/messages/unread")
    Call<List<NotesDO>> unreadNotesAPI(@Header("Accept") String contentType, @Header("Cookie") String cookie,
                                       @Query("startKey") String lastEvalKey);

    //Retrieve Archive Notes
    @GET("communication/v1/messages/archived")
    Call<List<NotesDO>> archiveNotesAPI(@Header("Accept") String contentType, @Header("Cookie") String cookie,@Query("startKey") String lastEvalKey);


    @GET("goal/v1/users/{author_id}/goals")
    Call<GoalTypeDO> goalTypeListAPI(@Header("Accept") String contentType, @Header("Cookie") String cookie,
                                     @Path("author_id") String authorId);


    // User SignOut
    @GET("useraccount/logout")
    Call<Void> callUserSignOut(@Header("Accept") String contentType, @Header("Cookie") String cookie);


}
