package ir.rahcode.persisapp.layouts.SplashScreens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.shreyaspatil.MaterialDialog.AbstractDialog;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

import java.util.ArrayList;

import ir.rahcode.persisapp.R;
import ir.rahcode.persisapp.api.ServiceGenerator;
import ir.rahcode.persisapp.api.services.UserService;
import ir.rahcode.persisapp.layouts.LoginScreens.SignupMainContainer;
import ir.rahcode.persisapp.models.UserData;
import ir.rahcode.persisapp.models.json.LoginRequestJson;
import ir.rahcode.persisapp.models.json.LoginResponseJson;
import ir.rahcode.persisapp.utils.ConnectivityUtils;
import ir.rahcode.persisapp.utils.PrefranceHelper;
import ir.rahcode.persisapp.utils.StaticCenteral;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private Boolean ConnectionAvibale = false;
private  Handler mainHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_splash);
        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
mainHandler = new Handler(Looper.getMainLooper());
        if(ChekInternetConection() == false){


            showPopupHold(getString(R.string.NO_INTERNET),"GPRS");


        }else{


            RequestFCMToken();
            checkLocationPermission();


        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 99) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                if(PrefranceHelper.getPhoneNumber().isEmpty())
                {
                    startActivity(new Intent(SplashActivity.this, SignupMainContainer.class));
                    SplashActivity.this.finish();

                }else{

                    ChekUserStatus();
                }


            } else {
                showPopupHold("برای اجرای این نرم افزار نیازمند لوکیشن هستید  !","GPS");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case 0:
            //    Toast.makeText(this, getResources().getString(R.string.you_need_location_string), Toast.LENGTH_SHORT).show();
                getCurentLocation();
                break;
            case -1:

                checkLocationPermission();
                break;
        }
    }



    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public void checkLocationPermission() {
        ///if don have permission for ACCESS_COARSE_LOCATION
        if (!isLocationEnabled(this)) {

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);


            Task<LocationSettingsResponse> result =
                    LocationServices.getSettingsClient(SplashActivity.this).checkLocationSettings(builder.build());
             result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                    } catch (ApiException exception) {
                        switch (exception.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the
                                // user a dialog.
                                try {
                                    // Cast to a resolvable exception.
                                    ResolvableApiException resolvable = (ResolvableApiException) exception;
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    resolvable.startResolutionForResult(
                                            SplashActivity.this,
                                            LocationRequest.PRIORITY_HIGH_ACCURACY);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                } catch (ClassCastException e) {
                                    // Ignore, should be an impossible error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                break;
                        }
                    }
                }
            });




            // getCurentLocation();
        } else {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                // No explanation needed, we can request the permission.

                String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE};
                String rationale = "لطفا دسترسی مکان را بدهید!";
                Permissions.Options options = new Permissions.Options()
                        .setRationaleDialogTitle("اطلاعات")
                        .setSettingsDialogTitle(getString(R.string.error_string));

                Permissions.check(this/*context*/, permissions, rationale, options, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        // do your task.

                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                        // permission denied, block the feature.
                        Toast.makeText(SplashActivity.this, "برای استفاده از نرم افزار  باید لوکیشن شما فعال باشد", Toast.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2000);

                    }
                });


                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_LOCATION);
                ///////////////

            } else {

                getCurentLocation();


            }
        }

    }

    LocationEngine locationEngine;

    private void getCurentLocation() {

        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        LocationEngineRequest request = new LocationEngineRequest.Builder(1000).setPriority(LocationEngineRequest.PRIORITY_BALANCED_POWER_ACCURACY).build();


        locationEngine.requestLocationUpdates(request, locationEngineCallback, getMainLooper());


        Intent intent = new Intent(SplashActivity.this, this.getClass());
        PendingIntent pi = PendingIntent.getBroadcast(SplashActivity.this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        locationEngine.requestLocationUpdates(request, pi);


    }


    public LocationEngineCallback<LocationEngineResult> locationEngineCallback = new LocationEngineCallback<LocationEngineResult>() {
        @Override
        public void onSuccess(LocationEngineResult result) {
            Location lastKnowLocation = result.getLastLocation();
            PrefranceHelper.SetLatLng(lastKnowLocation.getLatitude() + "," + lastKnowLocation.getLongitude());

            if(PrefranceHelper.getPhoneNumber().isEmpty())
            {

                startActivity(new Intent(SplashActivity.this, SignupMainContainer.class));
                SplashActivity.this.finish();

            }else{

                ChekUserStatus();
            }
            locationEngine.removeLocationUpdates(locationEngineCallback);
        }

        @Override
        public void onFailure(@NonNull Exception exception) {
            android.util.Log.i("kchal", "onFailure: " + exception.getMessage());
        }
    };



    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            return !TextUtils.isEmpty(locationProviders);
        }
    }





    void GetLocationPermisison(){
        Permissions.check(this/*context*/, Manifest.permission.ACCESS_COARSE_LOCATION, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                Toast.makeText(SplashActivity.this, "پرمِزن", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                GetLocationPermisison();
            }
        });

    }

    private boolean ChekInternetConection() {
      return ConnectivityUtils.isConnected(this);

    }
//dxcxc
    private void OpenInternetPage(String type) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        if (type == "GPRS")
            intent.setClassName(this, Settings.ACTION_WIRELESS_SETTINGS);
        else
            intent.setClassName(this, Settings.ACTION_WIFI_SETTINGS);
        startActivity(intent);
    }

//
    private void showPopupHold(String message,String type) {


MaterialDialog mDialog = new MaterialDialog.Builder(this).setTitle(type).setMessage(message).setCancelable(false).setPositiveButton("تنظیمات", new AbstractDialog.OnClickListener() {
    @Override
    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
        OpenInternetPage(type);
    }
}).setNegativeButton("خروج", new AbstractDialog.OnClickListener() {
    @Override
    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
        SplashActivity.this.finish();
    }
}).build();
mDialog.show();







    }


    private void RequestFCMToken(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
           String newToken = instanceIdResult.getToken();

                PrefranceHelper.SetFCMToken(newToken);

            }
        });
    }
    public void ChekUserStatus() {
        LoginRequestJson request = new LoginRequestJson();

        request.setphone(PrefranceHelper.getPhoneNumber());
        request.setRegId(null);
        final UserService service = ServiceGenerator.createService(UserService.class);
        service.login(request).enqueue(new Callback<LoginResponseJson>() {
            @Override
            public void onResponse(Call<LoginResponseJson> call, Response<LoginResponseJson> response) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (response.isSuccessful()) {
                        if (response.body().getMessage().equalsIgnoreCase("found")) {




                        } else {
if(PrefranceHelper.getPhoneNumber().isEmpty()){
    startActivity(new Intent(SplashActivity.this, SignupMainContainer.class));
    SplashActivity.this.finish();
}else{





    MaterialDialog mDialog =
            new MaterialDialog.Builder(SplashActivity.this).setTitle("دستیار").setMessage(getString(R.string.name_and_family_helper)).setCancelable(false).setPositiveButton("ثبت مشخصات", new AbstractDialog.OnClickListener() {
                @Override
                public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {

                    Intent i = new Intent(new Intent(SplashActivity.this, SignupMainContainer.class));
                    i.putExtra("has_number",true);
                    startActivity(i);
                    SplashActivity.this.finish();
                }
            }).setNegativeButton("از ابتدا", new AbstractDialog.OnClickListener() {
                @Override
                public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                    startActivity(new Intent(SplashActivity.this, SignupMainContainer.class));
                    SplashActivity.this.finish();
                }
            }).build();
    mDialog.show();













}

                        }
                        }
                }
            });


            }

            @Override
            public void onFailure(Call<LoginResponseJson> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(SplashActivity.this, "مشکلی  پیش آمده با پشتیبانی در ارتباط باشید !", Toast.LENGTH_SHORT).show();
            }
        });
    }




    @Override
    protected void onResume() {
        super.onResume();
        ChekInternetConection();
    }
}
