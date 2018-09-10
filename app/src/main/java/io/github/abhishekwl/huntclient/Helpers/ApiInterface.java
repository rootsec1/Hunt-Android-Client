package io.github.abhishekwl.huntclient.Helpers;

import io.github.abhishekwl.huntclient.Models.Customer;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    //Customers
    @GET("customers")
    Call<Customer> getCustomer(@Query("uid") String uid);
    @FormUrlEncoded
    @POST("customers")
    Call<Customer> createCustomer(
            @Field("uid") String uid,
            @Field("name") String name,
            @Field("email") String email,
            @Field("image") String image
    );
}
