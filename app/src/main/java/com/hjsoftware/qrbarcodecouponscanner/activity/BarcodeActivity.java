package com.hjsoftware.qrbarcodecouponscanner.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.zxing.Result;
import com.hjsoftware.qrbarcodecouponscanner.R;


import me.dm7.barcodescanner.zxing.ZXingScannerView;
public class BarcodeActivity extends AppCompatActivity{

    private ZXingScannerView scannerView;
    final static int REQUEST_LOCATION = 199;
    //EditText scannervalue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner_value);
        scanCode();
        //scannervalue=(EditText)findViewById(R.id.et_couponcode);

//
//        if(Build.VERSION.SDK_INT<23)
//        {
//
//            scanCode();
//        }
//        else
//        {
//            if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
//            {
//                scanCode();
//            }
//            else
//            {
//                if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
//                {
//                    Toast.makeText(this," Permission is required to scan the Barcode!",Toast.LENGTH_LONG).show();
//                }
//                requestPermissions(new String[]{Manifest.permission.CAMERA},REQUEST_LOCATION);
//            }
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        if(requestCode==REQUEST_LOCATION) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//            } else {
//                Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG).show();
//            }
//        }
//        else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }


    }
    public void scanCode() {
        scannerView=new ZXingScannerView(this);
        scannerView.setResultHandler(new ZXingScannerResultHandler());
        setContentView(scannerView);
        scannerView.startCamera();
    }


    class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler{
        @Override
        public void handleResult(Result result) {

            String resultCode=result.getText();
            System.out.println(resultCode);
         //Toast.makeText(BarcodeActivity.this,"Coupon has been successfully redeemed",Toast.LENGTH_SHORT).show();
          //scannervalue.setText(resultCode);
        // setContentView(R.layout.scanner_value);
            scannerView.stopCamera();
            Intent intent = new Intent();
            intent.putExtra("scannerCode", resultCode);
            setResult(RESULT_OK, intent);

            finish();


        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(scannerView!=null) {
            scannerView.stopCamera();
        }
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }





}
