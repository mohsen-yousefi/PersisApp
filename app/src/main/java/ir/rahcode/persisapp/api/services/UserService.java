package ir.rahcode.persisapp.api.services;

import ir.rahcode.persisapp.models.Status;
import ir.rahcode.persisapp.models.json.LoginRequestJson;
import ir.rahcode.persisapp.models.json.LoginResponseJson;
import ir.rahcode.persisapp.models.json.RegisterResponseJson;
import ir.rahcode.persisapp.models.json.UserDataResponseJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {


    @POST("customer/login")
    Call<LoginResponseJson> login(@Body LoginRequestJson param);
    @FormUrlEncoded
    @POST("Activation/add")
    Call<Status> activationAdd(@Field("user_phone") String user_phone);


    @FormUrlEncoded
    @POST("Activation/check")
    Call<UserDataResponseJson> activationCheck(@Field("user_phone") String user_phone, @Field("active_code") String active_code, @Field("reg_id") String reg_id);
    @FormUrlEncoded
    @POST("customer/register_user")
    Call<RegisterResponseJson> register(@Field("first_name") String first_name,
                                        @Field("last_name") String last_name,
                                        @Field("email") String email,
                                        @Field("reg_id") String reg_id,
                                        @Field("phone") String phone);
}
