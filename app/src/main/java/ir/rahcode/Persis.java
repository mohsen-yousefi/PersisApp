package ir.rahcode;

import android.app.Application;

import ir.rahcode.persisapp.utils.PrefranceHelper;

public class Persis extends Application {


    @Override
    public void onCreate() {

        super.onCreate();
        new PrefranceHelper(this);




    }
}
