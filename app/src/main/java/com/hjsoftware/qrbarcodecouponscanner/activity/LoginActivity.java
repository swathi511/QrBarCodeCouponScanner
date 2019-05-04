package com.hjsoftware.qrbarcodecouponscanner.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.hjsoftware.qrbarcodecouponscanner.R;
import com.hjsoftware.qrbarcodecouponscanner.model.MobilePojo;
import com.hjsoftware.qrbarcodecouponscanner.webservices.API;
import com.hjsoftware.qrbarcodecouponscanner.webservices.RestClient;
import com.hjsoftware.qrbarcodecouponscanner.webservices.SessionManager;

import java.util.HashMap;

import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etPhone;
    private TextView proceed;
    String stPhone;
    CoordinatorLayout cLayout;
    API REST_CLIENT;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    ProgressDialog progressDialog;
    private static final String PREF_NAME = "SharedPref";
    SessionManager session;
    HashMap<String, String> user;
    String pnumber, profileid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());
        pref = getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        user = session.getUserDetails();
        // profileid = user.get(SessionManager.KEY_PROFILE_ID);
        pnumber=user.get(SessionManager.KEY_MOBILE);
        REST_CLIENT = RestClient.get();

        etPhone=(EditText)findViewById(R.id.et_mobile_number);
        proceed=(TextView)findViewById(R.id.tv_proceed);
        cLayout = (CoordinatorLayout) findViewById(R.id.cl_layout);

        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(intent.getAction())) {
                Log.w("data", "Main Activity is not the root.  Finishing Main Activity instead of launching.");
                finish();
                return;
            }
        }
        if(session.isLoggedIn()) {
//            progressDialog = new ProgressDialog(LoginActivity.this);
//            progressDialog.setIndeterminate(true);
//            progressDialog.setMessage("Please wait ...");
//            progressDialog.show();

            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();

//            JsonObject v=new JsonObject();
//            v.addProperty("mobile_number",pnumber);
//            retrofit2.Call<MobilePojo>call=REST_CLIENT.validate(v);
//            call.enqueue(new Callback<MobilePojo>() {
//                MobilePojo mobilestatus;
//                @Override
//                public void onResponse(retrofit2.Call<MobilePojo> call, Response<MobilePojo> response) {
//
//
//                    if(response.isSuccessful()){
//                        mobilestatus=response.body();
//                        session.createLoginSession(pnumber);
//                        editor.putString("mobile_number",pnumber);
//                        editor.commit();
//                        progressDialog.dismiss();
//
//
//                        Intent intent=new Intent(LoginActivity.this,OTPActivity.class);
//                        startActivity(intent);
//                        finish();
//
//                    }
//                    else{
//                        String msg=response.message();
//                    }
//                }
//
//                @Override
//                public void onFailure(retrofit2.Call<MobilePojo> call, Throwable t) {
//
//                    progressDialog.dismiss();
//                    Toast.makeText(LoginActivity.this, "Please check Internet connection!", Toast.LENGTH_SHORT).show();
//
//                }
//            });

        }

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stPhone=etPhone.getText().toString();
                if(stPhone.length()==0||stPhone.length()!=10){
                    Snackbar snackbar = Snackbar.make(cLayout,"Enter valid Mobile Number", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.parseColor("#ffffff"));
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(LoginActivity.this,R.color.colorPrimaryDark));
                    snackbar.show();

                }
                else{

                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Please wait ...");
                    progressDialog.show();

                    JsonObject v=new JsonObject();
                    v.addProperty("mobile_number",stPhone);

                    System.out.print(stPhone);

                    retrofit2.Call<MobilePojo>call=REST_CLIENT.validate(v);
                    call.enqueue(new Callback<MobilePojo>() {
                        MobilePojo mobilestatus;
                        @Override
                        public void onResponse(retrofit2.Call<MobilePojo> call, Response<MobilePojo> response) {


                            if(response.isSuccessful()){
                                mobilestatus=response.body();
                                editor.putString("mobile_number",stPhone);
                                editor.commit();
                                progressDialog.dismiss();

                                System.out.print(mobilestatus);

                                Intent intent=new Intent(LoginActivity.this,OTPActivity.class);
                                startActivity(intent);
                                finish();

                            }
                            else{
                                String msg=response.message();
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<MobilePojo> call, Throwable t) {

                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Please check Internet connection!", Toast.LENGTH_SHORT).show();

                        }
                    });



                }
            }
        });


    }
}