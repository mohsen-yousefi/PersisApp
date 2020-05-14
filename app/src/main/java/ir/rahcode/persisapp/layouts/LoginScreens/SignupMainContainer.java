package ir.rahcode.persisapp.layouts.LoginScreens;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ir.rahcode.persisapp.R;
import ir.rahcode.persisapp.layouts.FragmentScreens.InputCodeFragment;
import ir.rahcode.persisapp.layouts.FragmentScreens.InputNumberFragment;
import ir.rahcode.persisapp.layouts.FragmentScreens.NameAndFamilyFragment;
import ir.rahcode.persisapp.layouts.FragmentScreens.WelcomFragment;

public class SignupMainContainer extends AppCompatActivity {


int current_page = 0;
public static SignupMainContainer inc;


public String current_phone_number = "";




    private  void WelcomPage(){
        current_page = 0;
        WelcomFragment wf = new WelcomFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        ft.setCustomAnimations(R.animator.fade_in,R.animator.fade_out);
        ft.replace(R.id.main_frame,wf,"welcompage");
        ft.commit();

    }
    public  void InputNumberPage(){
current_page = 1;

      InputNumberFragment wf = new InputNumberFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

ft.addToBackStack(null) ;
        ft.setCustomAnimations(R.animator.fade_in,R.animator.fade_out);
        ft.replace(R.id.main_frame,wf," InputNumberFragment");
        ft.commit();


    }
   public   void InputCodePage(){
current_page =2;

       InputCodeFragment wf = new InputCodeFragment();
       FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

       ft.addToBackStack(null) ;
       ft.setCustomAnimations(R.animator.fade_in,R.animator.fade_out);
       ft.replace(R.id.main_frame,wf,"InputCodeFragment");
       ft.commit();

    }
   public   void NameAndFamilyPage(){
current_page = 3;
      NameAndFamilyFragment wf = new   NameAndFamilyFragment();
       FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

       ft.addToBackStack(null) ;
       ft.setCustomAnimations(R.animator.fade_in,R.animator.fade_out);
       ft.replace(R.id.main_frame,wf,"NameAndFamilyFragment");
       ft.commit();
    }
    public   void NameAndFamilyPageWithoutBack(){
        current_page = 3;
        NameAndFamilyFragment wf = new   NameAndFamilyFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        ft.setCustomAnimations(R.animator.fade_in,R.animator.fade_out);
        ft.replace(R.id.main_frame,wf,"NameAndFamilyFragment");
        ft.commit();
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
//shir - maie -sos
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_root);
        inc = this;
boolean is_number = getIntent().getBooleanExtra("has_number",false);
if(is_number == true){
    NameAndFamilyPageWithoutBack();
}else {
    WelcomPage();
}


    }
}
