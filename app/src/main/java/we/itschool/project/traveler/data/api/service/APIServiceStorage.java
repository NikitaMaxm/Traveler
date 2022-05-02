package we.itschool.project.traveler.data.api.service;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIServiceStorage {

    @GET("storage/image-card/")
    Call<ResponseBody> getOneCardById(
            @Query("cid") int id
    );

    @Multipart
    @POST("storage/upload-file-card")
    Call<String> uploadPhotoToCard(
        @Query("file") MultipartBody file,
        @Query("cid") long id
    );

    @POST("storage/upload-file-card")
    Call<String> uploadPhotoToCard2(
            @Part("file") MultipartBody.Part file,
            @Query("cid") long id
    );

}