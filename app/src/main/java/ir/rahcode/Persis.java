package ir.rahcode;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.mapbox.mapboxsdk.Mapbox;

import ir.rahcode.persisapp.utils.PrefranceHelper;

public class Persis extends Application {


    @Override
    public void onCreate() {

        super.onCreate();
        new PrefranceHelper(this);
        Mapbox.getInstance(this, "pk.eyJ1Ijoic2lsc2luIiwiYSI6ImNqaThpcTNqMzBrYzIza282Zm4zamwyc2UifQ.MmYKt4LHHM0-VRdQoMeYcg");


    }
}
