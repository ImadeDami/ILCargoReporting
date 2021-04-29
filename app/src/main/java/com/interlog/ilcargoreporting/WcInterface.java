package com.interlog.ilcargoreporting;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface WcInterface {
    @FormUrlEncoded
    @POST("interlogmobile/wc.php")
    Call<ResponseBody> submitResponse(
            //@Field("userID") String userID,
            //@Field("randomNumber") String randomNumber,
            @Field("customerName") String customerName,
            @Field("locationAddr") String locationAddr,
            @Field("productName") String productName,
            @Field("commDate") String commDate,
            @Field("reportingDate") String reportingDate,
            @Field("vesselName") String vesselName,
            @Field("openingBal") String openingBal,
            @Field("takeOn") String takeOn,
            @Field("releaz1") String releaz1,
            @Field("releaz2") String releaz2,
            @Field("releaz3") String releaz3,
            @Field("closingBal") String closingBal,
            @Field("bankRelease") String bankRelease,
            @Field("bankBalance") String bankBalance);

}
