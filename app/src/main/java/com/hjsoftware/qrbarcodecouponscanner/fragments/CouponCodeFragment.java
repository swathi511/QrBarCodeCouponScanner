package com.hjsoftware.qrbarcodecouponscanner.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.hjsoftware.qrbarcodecouponscanner.R;
import com.hjsoftware.qrbarcodecouponscanner.activity.BarcodeActivity;
import com.hjsoftware.qrbarcodecouponscanner.model.GetBarcodePojo;
import com.hjsoftware.qrbarcodecouponscanner.webservices.API;
import com.hjsoftware.qrbarcodecouponscanner.webservices.RestClient;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Callback;
import retrofit2.Response;
import static android.app.Activity.RESULT_OK;


public class CouponCodeFragment extends Fragment {

    View v;
    String st_cname,st_cmobile,st_couponcode;
    Button btscan,bt_Redeem;
    EditText et_Cname,et_cNumber,couponcode;
    API REST_CLIENT;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    ProgressDialog progressDialog;
    private static final String PREF_NAME = "SharedPref";

    private ZXingScannerView scannerView;
    final static int REQUEST_LOCATION = 199;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v= inflater.inflate(R.layout.qr_bar_code, container, false);

        btscan=(Button)v.findViewById(R.id.bt_scan);
        couponcode=(EditText)v.findViewById(R.id.et_couponcode);
        et_Cname=(EditText)v.findViewById(R.id.al_et_custr_name);
        et_cNumber=(EditText)v.findViewById(R.id.al_et_custr_mobile_number);
        bt_Redeem=(Button)v.findViewById(R.id.al_bt_redeem_code);

        pref = getActivity().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        REST_CLIENT = RestClient.get();

        bt_Redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                st_cname=et_Cname.getText().toString().trim();
                st_cmobile=et_cNumber.getText().toString().trim();
                st_couponcode=couponcode.getText().toString().trim();

                if(st_cname.length()==0){
                    et_Cname.setError("Enter CustomerName");
                }
                else if(st_cmobile.length()==0||st_cmobile.length()!=10){
                    et_cNumber.setError("Enter CustomerMobile Number");
                }
                else if(st_couponcode.length()==0){
                    couponcode.setError("Enter valid Couponcode");
                }
                else{

                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Please wait ...");
                    progressDialog.show();

                    JsonObject v=new JsonObject();
                    v.addProperty("customer_name",st_cname);
                    v.addProperty("customer_mobile",st_cmobile);
                    v.addProperty("dealer_id",pref.getString("id",null));
                    v.addProperty("company_id",pref.getString("company_id",null));
                    v.addProperty("barcde_id",couponcode.getText().toString().trim());

                    Log.i("CName",st_cname);
                    Log.i("CMobile",st_cmobile);
                    Log.i("barcode id",couponcode.getText().toString().trim());
                    // Log.i("company_id",pref.getString("company_id",null));
                    // Log.i("dealer_id",pref.getString("id",null));
                    // System.out.print(pref.getString("id",null));

                    retrofit2.Call<GetBarcodePojo>call=REST_CLIENT.barcode(v);
                    call.enqueue(new Callback<GetBarcodePojo>() {
                        GetBarcodePojo barcodestatus;

                        @Override
                        public void onResponse(retrofit2.Call<GetBarcodePojo> call, Response<GetBarcodePojo> response) {

                            if(response.isSuccessful()){

                                progressDialog.dismiss();
                                barcodestatus=response.body();

                                Log.i("Customer_Name",st_cname);
                                Log.i("Customer_Mobile",st_cmobile);
                                Log.i("barcode_id",couponcode.getText().toString().trim());
                                Log.i("company_id",pref.getString("company_id",null));
                                Log.i("dealer_id",pref.getString("id",null));

                                if(barcodestatus.getStatus()==1){
                                    couponcode.setText("");
                                    Toast.makeText(getContext(),"Coupon has been successfully redeemed", Toast.LENGTH_SHORT).show();

                                    //System.out.println("@@@@@@@@@@@@@@@@@@@@@"+barcodestatus.getResponse());
                                }
                                else if(barcodestatus.getStatus()==2){

                                    couponcode.setText("");
                                    Toast.makeText(getContext(),  (String) barcodestatus.getResponse(), Toast.LENGTH_SHORT).show();

                                }
                                else if(barcodestatus.getStatus()==3){

                                    couponcode.setText("");
                                    Toast.makeText(getActivity(),  (String) barcodestatus.getResponse(), Toast.LENGTH_SHORT).show();

                                }
                                else if(barcodestatus.getStatus()==4){
                                    couponcode.setText("");

                                    Toast.makeText(getActivity(),  (String) barcodestatus.getResponse(), Toast.LENGTH_SHORT).show();

                                }
                                else {

                                }
                            }
                            else{

                                String msg=response.message();

                                System.out.print(msg);
                                //progressDialog.dismiss();
                                //couponcode.setText("");
                                // Toast.makeText(getContext(), "Coupon has been Successfully redeemed", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(retrofit2.Call<GetBarcodePojo> call, Throwable t) {

                            progressDialog.dismiss();
                            t.printStackTrace();
                            System.out.println("msg"+t.getMessage());
                            Toast.makeText(getActivity(), "Please check Internet connection!", Toast.LENGTH_SHORT).show();


                        }
                    });
                }
            }
        });


        btscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_Redeem.setVisibility(View.VISIBLE);

                if(Build.VERSION.SDK_INT<23)
                {
                    Intent i=new Intent(getActivity(),BarcodeActivity.class);
                    startActivityForResult(i, 1);
                    // startActivity(i);


                    // scanCode();
                }
                else
                {
                    if(getActivity().checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
                    {
                        Intent i=new Intent(getActivity(),BarcodeActivity.class);
                        startActivityForResult(i, 1);
                        //  startActivity(i);


                    }
                    else
                    {
                        if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
                        {
                            Toast.makeText(getActivity()," Permission is required to scan the Barcode!",Toast.LENGTH_LONG).show();
                        }
                        requestPermissions(new String[]{Manifest.permission.CAMERA},REQUEST_LOCATION);
                    }
                }



            }
        });


        return v;



    }
    //    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        if(requestCode==REQUEST_LOCATION) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//            } else {
//                Toast.makeText(getActivity(), "Permission not granted", Toast.LENGTH_LONG).show();
//            }
//        }
//        else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//
//    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(getActivity(), "Permission not granted", Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        if (scannerView != null) {
            scannerView.stopCamera();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String strEditText = data.getStringExtra("scannerCode");
                couponcode.setText(strEditText);
            }
        }
    }
}









