package com.example.upi.inappupi;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity /*implements IntentFlowUPIBottonDialogFragment.Listener*/{

    private static final int UPI_REQ_CODE = 1991;
    private static final String UPI_APP_PKG_NAME = "com.google.android.apps.nbu.paisa.user";

    UPIIntentFlowBottomSheetDialogFragment bottomSheetDialog;
    private EditText payDescription;
    private EditText payAmount;
    private EditText payAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button payButton = findViewById(R.id.payButton);

        payAddress = findViewById(R.id.payAddress);
        payAmount = findViewById(R.id.payAmount);
        payDescription = findViewById(R.id.payDescription);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = payAddress.getText().toString().trim();
                address = TextUtils.isEmpty(address) ? "deepaknitb24@okicici" : address;

                String amount = payAmount.getText().toString().trim();
                amount = TextUtils.isEmpty(amount) ? "10" : amount;

                String description = payDescription.getText().toString().trim();
                description = TextUtils.isEmpty(description) ? "Pay for UPI testing" : description;

                Uri uri = new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
                        .appendQueryParameter("pa", address) // payment address
                        .appendQueryParameter("pn", "Deepak Mandhani")
                        //.appendQueryParameter("mc", "1234")
                        //.appendQueryParameter("tr", "123456789")
                        .appendQueryParameter("tn", description) // payment description/note
                        .appendQueryParameter("am", amount) // payment amount
                        .appendQueryParameter("cu", "INR")
                        //.appendQueryParameter("url", "https://test.merchant.website")
                        .build();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);

                /* // for specific app like G-PAy
                intent.setPackage(UPI_APP_PKG_NAME);
                startActivityForResult(intent, UPI_REQ_CODE);*/

                Intent intentChooser = Intent.createChooser(intent, "Pay with below UPI apps-");
                //Intent.parseUri(uri.toString(), Intent.URI_INTENT_SCHEME);
                if(intentChooser.resolveActivity(getPackageManager()) != null) {
                    //startActivityForResult(intentChooser, UPI_REQ_CODE);
                    //startActivityForResult(intentView, UPI_REQ_CODE);
                }

                Intent intentView = new Intent(Intent.ACTION_VIEW, uri); // Uri.parse(uri.toString()

                bottomSheetDialog = UPIIntentFlowBottomSheetDialogFragment.newInstance(fetchUPISupportedAppsFromDevice(intentView));
                bottomSheetDialog.show(getSupportFragmentManager(), "Custom Bottom Sheet");

            }
        });
    }

    public List<UPIAppInfo> fetchUPISupportedAppsFromDevice(Intent intentView) {

        PackageManager packageManager = getPackageManager();

        List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(intentView, PackageManager.MATCH_ALL);
        List<UPIAppInfo> upiAppInfoList = new ArrayList<>();

        for(ResolveInfo resolveInfo : resolveInfoList) {
            try {
                UPIAppInfo upiAppInfo = new UPIAppInfo();
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(resolveInfo.activityInfo.packageName, PackageManager.GET_META_DATA);
                String appName = packageManager.getApplicationLabel(applicationInfo).toString();

                // model class setters
                upiAppInfo.appName = packageManager.getApplicationLabel(applicationInfo).toString();
                upiAppInfo.packageName = applicationInfo.packageName;
                upiAppInfo.orderNo = resolveInfoList.indexOf(resolveInfo);
                upiAppInfoList.add(upiAppInfo);

                Log.d(getLocalClassName(), "app name-" + appName + " or " + applicationInfo.loadLabel(packageManager));
                Log.d(getLocalClassName(), "app icon-" +packageManager.getApplicationIcon(applicationInfo) + " or " + applicationInfo.loadIcon(packageManager));
                Log.d(getLocalClassName(), "app logo-" +packageManager.getApplicationLogo(applicationInfo) + " or " + applicationInfo.loadLogo(packageManager));
                //Log.d(getLocalClassName(), appName + " or " + applicationInfo.loadLabel(packageManager));

                Log.d(getLocalClassName(), "" + applicationInfo.loadIcon(packageManager));
                Log.d(getLocalClassName(), "" + applicationInfo.packageName);
                Log.d(getLocalClassName(), "" + applicationInfo.name);
                Log.d(getLocalClassName(), "" + applicationInfo.sourceDir);
                Log.d(getLocalClassName(), "" + packageManager.getLaunchIntentForPackage(applicationInfo.packageName));

                Log.d(getLocalClassName(), ""+getPackageManager().getApplicationLabel(getPackageManager().getApplicationInfo(resolveInfo.activityInfo.packageName, PackageManager.GET_META_DATA)));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return upiAppInfoList;
    }


    public void redirectToSelectedApp() {
        String address = payAddress.getText().toString().trim();
        address = TextUtils.isEmpty(address) ? "deepaknitb24@okicici" : address;

        String amount = payAmount.getText().toString().trim();
        amount = TextUtils.isEmpty(amount) ? "10" : amount;

        String description = payDescription.getText().toString().trim();
        description = TextUtils.isEmpty(description) ? "Pay for UPI testing" : description;

        Uri uri = new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", address) // payment address
                .appendQueryParameter("pn", "Deepak Mandhani")
                //.appendQueryParameter("mc", "1234")
                //.appendQueryParameter("tr", "123456789")
                .appendQueryParameter("tn", description) // payment description/note
                .appendQueryParameter("am", amount) // payment amount
                .appendQueryParameter("cu", "INR")
                //.appendQueryParameter("url", "https://test.merchant.website")
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);

        // for specific app like G-PAy
        intent.setPackage(UPI_APP_PKG_NAME);
        startActivityForResult(intent, UPI_REQ_CODE);

        //Intent intentChooser = Intent.createChooser(intent, "Pay with below UPI apps-");
        if(intent.resolveActivity(getPackageManager()) != null) {
            //startActivityForResult(intentChooser, UPI_REQ_CODE);
            startActivityForResult(intent, UPI_REQ_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPI_REQ_CODE) {
            Toast.makeText(this, data != null ? data.getStringExtra("Status") : "NoStatusFound :(", Toast.LENGTH_SHORT).show();
            String returnData = data !=null ? data.getDataString() : "NoDataFound :(";
            Log.d(getLocalClassName(), "-" + resultCode + " : " + returnData );
        }
    }

    @Override
    public void onBackPressed() {
        if(bottomSheetDialog.isAdded() && bottomSheetDialog.isVisible()) {
            bottomSheetDialog.dismiss();
            return;
        }
        super.onBackPressed();
    }

    /*    @Override
    public void onItemClicked(int position) {

    }*/
}
