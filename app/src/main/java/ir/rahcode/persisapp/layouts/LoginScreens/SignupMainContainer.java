package ir.rahcode.persisapp.layouts.LoginScreens;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ir.rahcode.persisapp.R;
import ir.rahcode.persisapp.layouts.FragmentScreens.WelcomFragment;

public class SignupMainContainer extends AppCompatActivity {


int current_page = 0;

    private  void WelcomPage(){
        current_page = 0;
        WelcomFragment wf = new WelcomFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        ft.setCustomAnimations(R.animator.fade_in,R.animator.fade_out);
        ft.replace(R.id.main_frame,wf,"welcompage");
        ft.commit();

    }
    private  void InputNumberPage(){
current_page = 1;
    }
    private  void InputCodePage(){
current_page =2;
    }
    private  void NameAndFamilyPage(){
current_page = 3;

    }


    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if(fm.getBackStackEntryCount() > 0){
            fm.popBackStack();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_root);
        WelcomPage();



    }
}
