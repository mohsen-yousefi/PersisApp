package ir.rahcode.persisapp.layouts.SeperatedScreen;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;

import ir.rahcode.persisapp.R;
import ir.rahcode.persisapp.layouts.FragmentScreens.MainFragment;
import ir.rahcode.persisapp.layouts.FragmentScreens.WelcomFragment;

public class MainViewContainer extends AppCompatActivity {




    private  void ShowMainFragment(){

        MainFragment  wf = new MainFragment ();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        ft.setCustomAnimations(R.animator.fade_in,R.animator.fade_out);
        ft.replace(R.id.main_frame,wf,"MainFragment ");
        ft.commit();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
      //  ShowMainFragment();
    }
}
