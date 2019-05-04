package com.hjsoftware.qrbarcodecouponscanner.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.hjsoftware.qrbarcodecouponscanner.R;
import com.hjsoftware.qrbarcodecouponscanner.model.OtpPojo;
import com.hjsoftware.qrbarcodecouponscanner.model.OtpResponcePojo;
import com.hjsoftware.qrbarcodecouponscanner.webservices.API;
import com.hjsoftware.qrbarcodecouponscanner.webservices.RestClient;
import com.hjsoftware.qrbarcodecouponscanner.webservices.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjsoftware on 5/2/18.
 */

public class OTPActivity extends AppCompatActivity {
    private EditText etOtp1,etOtp2,etOtp3,etOtp4;
    private TextView login,edit_mobile;
    CoordinatorLayout cLayout;
    API REST_CLIENT;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    ProgressDialog progressDialog;
    private static final String PREF_NAME = "SharedPref";
    SessionManager session;
    HashMap<String, String> user;
    String pnumber, pwd;
    String stotp1,stotp2,stotp3,stotp4,st_edit_mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_validate);

       login=(TextView)findViewById(R.id.tv_login);

        etOtp1=(EditText)findViewById(R.id.et_otp_one);
        etOtp2=(EditText)findViewById(R.id.et_otp_two);
        etOtp3=(EditText)findViewById(R.id.et_otp_three);
        etOtp4=(EditText)findViewById(R.id.et_otp_four);

        edit_mobile=(TextView)findViewById(R.id.tv_edit_mobile);


        session = new SessionManager(getApplicationContext());
        pref = getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        user = session.getUserDetails();
        final String mobilenumber=pref.getString("mobile_number",null);
        final String dealerid=pref.getString("id",null);
        edit_mobile.setText(mobilenumber);

        System.out.println(mobilenumber);
        REST_CLIENT = RestClient.get();
        etOtp1.addTextChangedListener(new GenericTextWatcher(etOtp1));
        etOtp2.addTextChangedListener(new GenericTextWatcher(etOtp2));
        etOtp3.addTextChangedListener(new GenericTextWatcher(etOtp3));
        etOtp4.addTextChangedListener(new GenericTextWatcher(etOtp4));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stotp1=etOtp1.getText().toString().trim();
                stotp2=etOtp2.getText().toString().trim();
                stotp3=etOtp3.getText().toString().trim();
                stotp4=etOtp4.getText().toString().trim();
                if(stotp1.length()==0||stotp2.length()==0||stotp3.length()==0||stotp4.length()==0){
                    Toast.makeText(OTPActivity.this, "Enter valid OTP", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog = new ProgressDialog(OTPActivity.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Please wait ...");
                    progressDialog.show();

                    System.out.println(mobilenumber);
                    System.out.println(stotp1+stotp2+stotp3+stotp4);

                    JsonObject v=new JsonObject();
                    v.addProperty("mobile_number",mobilenumber);
                    v.addProperty("otp",stotp1+stotp2+stotp3+stotp4);
                    //v.addProperty("id",pref.getString("id",null));

                    Call<OtpPojo> call=REST_CLIENT.otpvalidate(v);
                    call.enqueue(new Callback<OtpPojo>() {
                        OtpPojo otpstatus;

                        List<OtpResponcePojo> otpresponcestatus;
                        @Override
                        public void onResponse(Call<OtpPojo> call, Response<OtpPojo> response) {
                            otpstatus=response.body();

                            if(response.isSuccessful()){
                                progressDialog.dismiss();
                                otpstatus=response.body();

                                if(otpstatus.getStatus())
                                {
                                    otpresponcestatus=(List<OtpResponcePojo>)otpstatus.getOtpResponcePojo();


                                    System.out.println("***"+otpresponcestatus.size());

                                    OtpResponcePojo o=otpresponcestatus.get(0);

                                    String id=o.getId();
                                    String company_id=o.getCompanyId();
                                    session.createLoginSession(mobilenumber);

                                    editor.putString("id",id);
                                    editor.putString("company_id",company_id);
                                    editor.commit();



                                    Toast.makeText(OTPActivity.this,"OTP successfully validated!",Toast.LENGTH_SHORT).show();

                                    Intent k=new Intent(OTPActivity.this,HomeActivity.class);
                                    startActivity(k);
                                    finish();
                                }
                                else {

                                    progressDialog.dismiss();
                                    Toast.makeText(OTPActivity.this, "OTP Authentication Failed!", Toast.LENGTH_SHORT).show();
                                }


                            }
                            else{

                                progressDialog.dismiss();
                                Toast.makeText(OTPActivity.this, "OTP Authentication Failed!", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<OtpPojo> call, Throwable t) {

                            progressDialog.dismiss();
                            t.printStackTrace();
                            System.out.println("message:::::::::::::"+t);

                            Toast.makeText(OTPActivity.this, "Please check internet Connection!", Toast.LENGTH_SHORT).show();


                        }
                    });


                }

            }
        });
    }
    class GenericTextWatcher implements TextWatcher
    {
        private View view;
        private GenericTextWatcher(View view)
        {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch(view.getId())
            {

                case R.id.et_otp_one:
                    if(text.length()==1)
                        etOtp2.requestFocus();
                    break;
                case R.id.et_otp_two:
                    if(text.length()==1)
                        etOtp3.requestFocus();
                    break;
                case R.id.et_otp_three:
                    if(text.length()==1)
                        etOtp4.requestFocus();
                    break;
                case R.id.et_otp_four:
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            String text = arg0.toString();

            switch (view.getId()) {
                case R.id.et_otp_one:
                    if (text.length() == 1) {

                        etOtp1.requestFocus();
                        etOtp1.setSelection(etOtp1.getText().length());

                    }
                    else
                        etOtp1.requestFocus();

                    break;
                case R.id.et_otp_two:
                    if (text.length()==1) {

                        etOtp1.requestFocus();
                        etOtp1.setSelection(etOtp1.getText().length());
                    }

                    break;
                case R.id.et_otp_three:
                    if (text.length() == 1) {

                        etOtp2.requestFocus();
                        etOtp2.setSelection(etOtp2.getText().length());

                    }

                    break;
                case R.id.et_otp_four:
                    if (text.length() == 1) {
                        etOtp3.requestFocus();

                        etOtp3.setSelection(etOtp3.getText().length());
                    }
                    break;
                default:
                    break;
            }

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

    }


    }

