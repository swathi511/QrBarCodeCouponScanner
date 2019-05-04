package com.hjsoftware.qrbarcodecouponscanner.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.hjsoftware.qrbarcodecouponscanner.R;
import com.hjsoftware.qrbarcodecouponscanner.model.BarcodehistoryPojo;
import com.hjsoftware.qrbarcodecouponscanner.model.BarqrcodeRespncePojo;
import com.hjsoftware.qrbarcodecouponscanner.model.CouponHistory;
import com.hjsoftware.qrbarcodecouponscanner.webservices.API;
import com.hjsoftware.qrbarcodecouponscanner.Adapters.RecyclerAdapter;
import com.hjsoftware.qrbarcodecouponscanner.webservices.RestClient;
import com.hjsoftware.qrbarcodecouponscanner.webservices.SessionManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CouponHistoryFragment extends Fragment {

        View v;
        EditText user_fromdate,user_todate;
        ImageButton user_calfromdate,user_caltodate;
        TextView load;
        RecyclerAdapter bAdapter;
        DatePickerDialog datePickerDialog;
        RecyclerView rView;
        SessionManager session;
        ArrayList<CouponHistory>couponList=new ArrayList<>();
        API REST_CLIENT;
        String fromdate;
        String todate;
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        int PRIVATE_MODE = 0;
        private static final String PREF_NAME = "SharedPref";
        @Override
        public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        REST_CLIENT= RestClient.get();
        pref = getActivity().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.coupon_history, container, false);

       user_fromdate=(EditText)v.findViewById(R.id.edit_text_fromdate);
       user_todate=(EditText)v.findViewById(R.id.edit_text_todate);
       user_calfromdate=(ImageButton) v.findViewById(R.id.button_select_fromdate);
       user_caltodate=(ImageButton)v.findViewById(R.id.buton_select_todate);
       rView=(RecyclerView)v.findViewById(R.id.recycler_view);
       rView.setVisibility(View.GONE);
        load=(TextView)v.findViewById(R.id.textview_view);

        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        //final String mobilenumber=pref.getString("mobile_number",null);


        fromdate=mYear+"-"+(mMonth+1)+"-"+mDay;
        todate=mYear+"-"+(mMonth+1)+"-"+mDay;
        SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" );

        user_fromdate.setText(mDay+"/"+(mMonth+1)+"/"+mYear);
        user_todate.setText(mDay+"/"+(mMonth+1)+"/"+mYear);


        user_calfromdate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               datePickerDialog = new DatePickerDialog(getContext(),
                       new DatePickerDialog.OnDateSetListener() {

                           @Override
                           public void onDateSet(DatePicker view, int year,
                                                 int monthOfYear, int dayOfMonth) {
                               user_fromdate.setText(dayOfMonth + "/"
                                       + (monthOfYear + 1) + "/" + year);
                               fromdate=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;

                           }
                       }, mYear, mMonth, mDay);
               datePickerDialog.show();

           }
       });

        user_caltodate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                user_todate.setText(dayOfMonth + "/"
                                        +(monthOfYear + 1)+ "/" + year);

                                todate=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please Wait ...");
                progressDialog.show();

            couponList.clear();

                System.out.println("Hello"  + "  "+ couponList.size());


                JsonObject v=new JsonObject();
                v.addProperty("from_date",fromdate);
                v.addProperty("to_date",todate);
                v.addProperty("dealer_id",pref.getString("id",null));
                v.addProperty("company_id",pref.getString("company_id",null));


                Log.i("Msg",fromdate);
                Log.i("Msg",todate);

            Call<BarcodehistoryPojo>call=REST_CLIENT.barcodehistory(v);
            call.enqueue(new Callback<BarcodehistoryPojo>() {
                BarcodehistoryPojo barcodehistorystaus;
                List<LinkedTreeMap> dList=new ArrayList<>();
                BarqrcodeRespncePojo d;

                Response r;
                @Override
                public void onResponse(Call<BarcodehistoryPojo> call, Response<BarcodehistoryPojo> response) {
                    barcodehistorystaus=response.body();

                    if(response.isSuccessful()){
                    progressDialog.dismiss();
                     rView.setVisibility(View.GONE);
                    //dList.clear();
                        couponList.clear();
                       // System.out.println("Haiiii"+ "  "+ couponList.size());


                        if(barcodehistorystaus.getStatus()) {

                        dList = (List<LinkedTreeMap>) barcodehistorystaus.getResponse();

                        System.out.println("******************"+ dList.size());
                        System.out.println(barcodehistorystaus.getResponse());

                        ArrayList<BarqrcodeRespncePojo> reviews = new ArrayList<>();




                            for(LinkedTreeMap map : dList){

                            BarqrcodeRespncePojo review = new BarqrcodeRespncePojo();
                            review.setBarcode(map.get("barcode").toString());
                            review.setCustomerMobile(map.get( "customer_mobile").toString());
                            review.setCustomerName(map.get( "customer_name").toString());
                            review.setPromotionValue(map.get("promotion_value").toString());
                            reviews.add(review);

                        }


                        System.out.println("&&&&&&&&&&&&"+reviews.size());

                        for (int i = 0; i <reviews.size(); i++) {

                                    d = (BarqrcodeRespncePojo)reviews.get(i);

                                String barcode = d.getBarcode();
                                String Amount = d.getPromotionValue();
                                String cname = d.getCustomerName();
                                String cmobile = d.getCustomerMobile();

                                System.out.println(barcode + "" + Amount + "" + cname + "" + cmobile);

                                couponList.add(new CouponHistory(d.getBarcode(), d.getPromotionValue(), d.getCustomerName(), d.getCustomerMobile()));

                            }
                    }
                    else {
                            couponList.clear();
                            rView.setVisibility(View.GONE);
                            String v=(String) barcodehistorystaus.getResponse();
                           Toast.makeText(getActivity(), "No History!", Toast.LENGTH_SHORT).show();

                    }



                    }
                    else {
                        couponList.clear();
                        rView.setVisibility(View.GONE);
                        Toast.makeText(getActivity(),"No History!",Toast.LENGTH_SHORT).show();

                    }


                    if (couponList.size() != 0) {

                        Log.i("data", "OO::::" + couponList.size());
                        rView.setVisibility(View.VISIBLE);
                        bAdapter = new RecyclerAdapter(getActivity(), couponList, rView);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        rView.setLayoutManager(mLayoutManager);
                        rView.setItemAnimator(new DefaultItemAnimator());
                        rView.setAdapter(bAdapter);
                        bAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();


                    } else {
                        couponList.clear();

                        bAdapter = new RecyclerAdapter(getActivity(), couponList, rView);
                        bAdapter.notifyDataSetChanged();

                        Toast.makeText(getActivity(), "No History!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<BarcodehistoryPojo> call, Throwable t) {
                    rView.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    t.printStackTrace();
                    System.out.println("msg"+t.getMessage());
                    Toast.makeText(getActivity(),"Please check Internet connection!",Toast.LENGTH_SHORT).show();

                }
            });



            }
        });

        return v;
    }







}




