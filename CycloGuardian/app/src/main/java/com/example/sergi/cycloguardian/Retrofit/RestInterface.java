package com.example.sergi.cycloguardian.Retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by sergi on 15/05/2018.
 */

public interface RestInterface {
    /**
     * Metodo que actúan como endPoint para el servicio de login
     * @param email
     * @param password
     * @return
     */
    @GET("/serviceLogin/{email}/{password}")
    Call<LoginResponse> loginUser(@Path("email") String email, @Path("password") String password);

    /**
     * Metodo que actúa como endPoint para el registro de sesiones
     * @param uuidSesion
     * @param idUser
     * @param sesionStart
     * @param sesionEnd
     * @param timeElapsed
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("/api/registro/sesion")
    Call<ServerDataResponse> signUpSesion(@Field("uuidSesion") String uuidSesion, @Field("idUser") int idUser,
                                          @Field("sesionStart") String sesionStart, @Field("sesionEnd") String sesionEnd,
                                          @Field("timeElapsed") long timeElapsed, @Field("token") String token);

    /**
     * Servicio para el registro de una foto en el servidor
     * @param uuidPhoto
     * @param uuidIncidencia
     * @param namePhoto
     * @param rutaAndroid
     * @param token
     * @return
     */
    @POST("/api/registro/foto")
    Call<ServerDataResponse> signUpPhotoPost(@Field("uuidPhoto") String uuidPhoto, @Field("uuidIncidencia") String uuidIncidencia,
                                         @Field("namePhoto") String namePhoto, @Field("rutaAndroid") String rutaAndroid, @Field("token") String token);

    /**
     * Servicio para el registro de una incidencia en el servidor
     * @param uuidIncidencia
     * @param uuidSesion
     * @param latitud
     * @param longitud
     * @param timeIncidence
     * @param distanceSensor
     * @param token
     * @return
     */
    @POST("/api/registro/incidencia")
    Call<ServerDataResponse> signUpIncidencia(@Field("uuidIncidencia") String uuidIncidencia, @Field("uuidSesion") String uuidSesion,
                                              @Field("latitud") double latitud, @Field("longitud") double longitud,
                                              @Field("timeIncidence") String timeIncidence, @Field("distanceSensor") float distanceSensor,
                                              @Field("token") String token);

    /**
     * Servicio POST para el registro de un usuario
     * @param name
     * @param lastname
     * @param phone
     * @param email
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("/api/registro/usuario")
    Call<SignUpResponse> signUpUserPost(@Field("name") String name, @Field("lastname") String lastname, @Field("phone") String phone,
                                    @Field("email") String email, @Field("password") String password);

    /**
     * Multipart Service to up Data from the server
     * @param image
     * @param name
     * @param uuidIncidencia
     * @param uuidSesion
     * @param latitud
     * @param longitud
     * @param timeIncidence
     * @param distance
     * @param uuidPhoto
     * @param namePhoto
     * @param rutaAndroid
     * @param token
     * @return
     */
    @Multipart
    @POST("/api/uploadPhoto")
    Call<UploadPhotoResponse> uploadPhoto(@Part MultipartBody.Part image, @Part("name") RequestBody name,
                                          @Part("uuidIncidencia") RequestBody uuidIncidencia, @Part("uuidSesion") RequestBody uuidSesion,
                                          @Part("latitud") double latitud, @Part("longitud") double longitud,
                                          @Part("timeIncidence") RequestBody timeIncidence, @Part("distanceSensor") float distance,
                                          @Part("uuidPhoto") RequestBody uuidPhoto, @Part("namePhoto") RequestBody namePhoto,
                                          @Part("rutaAndroid") RequestBody rutaAndroid, @Part("token") RequestBody token);
}
