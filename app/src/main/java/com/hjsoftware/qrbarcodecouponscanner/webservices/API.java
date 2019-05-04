package com.hjsoftware.qrbarcodecouponscanner.webservices;


import com.google.gson.JsonObject;
import com.hjsoftware.qrbarcodecouponscanner.model.BarcodehistoryPojo;
import com.hjsoftware.qrbarcodecouponscanner.model.GetBarcodePojo;
import com.hjsoftware.qrbarcodecouponscanner.model.MobilePojo;
import com.hjsoftware.qrbarcodecouponscanner.model.OtpPojo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by hjsoftware on 22/12/17.
 */

public interface API {

    @POST("Barcode/generate_otp")
    Call<MobilePojo>validate(@Body JsonObject v);

    @POST("Barcode/check_generated_otp")
    Call<OtpPojo> otpvalidate(@Body JsonObject v);

    @POST("Barcode/get_barcode_details")
    Call<GetBarcodePojo>barcode(@Body JsonObject v);

    @POST("Barcode/get_barcode_history_details")
    Call<BarcodehistoryPojo> barcodehistory(@Body JsonObject v);


}
