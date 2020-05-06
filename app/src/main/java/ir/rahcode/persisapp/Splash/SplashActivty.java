package ir.rahcode.persisapp.Splash;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;

import ir.rahcode.persisapp.R;
import ir.rahcode.persisapp.utils.ConnectivityUtils;

public class SplashActivty extends AppCompatActivity {

    private Boolean ConnectionAvibale = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activty);

        ChekInternetConection();

    }


    void GetLocationPermisison(){
        Permissions.check(this/*context*/, Manifest.permission.ACCESS_COARSE_LOCATION, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                Toast.makeText(SplashActivty.this, "پرمِزن", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                GetLocationPermisison();
            }
        });

    }

    private void ChekInternetConection() {
        ConnectionAvibale = ConnectivityUtils.isConnected(this);
        if (!ConnectionAvibale) {
            showPopupHold(getString(R.string.NO_INTERNET));
        }
    }

    private void OpenInternetPage(String type) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        if (type == "GPRS")
            intent.setClassName(this, "com.android.phone.NetworkSetting");
        else
            intent.setClassName(this, "com.android.settings.wifi.WifiSettings");
        startActivity(intent);
    }


    private void showPopupHold(String message) {
        final AlertDialog popup = new AlertDialog.Builder(this).create();
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View viewPopup = getLayoutInflater().inflate(R.layout.popup, null);
        TextView title = viewPopup.findViewById(R.id.textView48);
        title.setText(message);
        TextView ok = viewPopup.findViewById(R.id.GPRS);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenInternetPage("GPRS");
            }
        });
        popup.setView(viewPopup);
        popup.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ChekInternetConection();
    }
}
