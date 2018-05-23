package com.example.sergi.cycloguardian.Retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by sergi on 15/05/2018.
 */

public interface RestInterface {
    @GET("/serviceLogin/{email}/{password}")
    Call<LoginResponse> loginUser(@Path("email") String email, @Path("password") String password);

    @GET("/serviceRegistro/{name}/{lastname}/{phone}/{email}/{password}")
    Call<SignUpResponse> signUpUser(@Path("name") String name, @Path("lastname") String lastname,
                                    @Path("phone") String phone, @Path("email") String email,
                                    @Path("password") String password);

    @Multipart
    @POST("/api/uploadPhoto")
    Call<UploadPhotoResponse> uploadPhoto(@Part MultipartBody.Part image, @Part("name") RequestBody name);
}
