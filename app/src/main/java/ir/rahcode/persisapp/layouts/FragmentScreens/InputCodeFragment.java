package ir.rahcode.persisapp.layouts.FragmentScreens;
import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.shreyaspatil.MaterialDialog.AbstractDialog;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

import ir.rahcode.persisapp.R;
import ir.rahcode.persisapp.api.ServiceGenerator;
import ir.rahcode.persisapp.api.services.UserService;
import ir.rahcode.persisapp.layouts.LoginScreens.SignupMainContainer;
import ir.rahcode.persisapp.layouts.SeperatedScreen.MainViewContainer;
import ir.rahcode.persisapp.layouts.SplashScreens.SplashActivity;
import ir.rahcode.persisapp.models.Status;
import ir.rahcode.persisapp.models.UserData;
import ir.rahcode.persisapp.models.json.UserDataResponseJson;
import ir.rahcode.persisapp.utils.PrefranceHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class InputCodeFragment extends  Fragment{
EditText view_signup_revamp_input_otp_edit_text;
AppCompatTextView view_signup_revamp_change_phone_textview,view_signup_revamp_subtitle_textview,view_signup_revamp_timer_otp_text;
MaterialButton view_signup_revamp_resend_button;
   Handler mainHandler;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_signup_otp, container, false);
        view_signup_revamp_change_phone_textview = view.findViewById(R.id.view_signup_revamp_change_phone_textview);
        view_signup_revamp_subtitle_textview = view.findViewById(R.id.view_signup_revamp_subtitle_textview);
        view_signup_revamp_timer_otp_text = view.findViewById(R.id.view_signup_revamp_timer_otp_text);
        view_signup_revamp_resend_button = view.findViewById(R.id.view_signup_revamp_resend_button);
        view_signup_revamp_input_otp_edit_text =  view.findViewById(R.id.view_signup_revamp_input_otp_edit_text) ;

        view_signup_revamp_subtitle_textview.setText("کد 4 رقمی به شماره " + SignupMainContainer.inc.current_phone_number + " ارسال شد لطفا ان را در کادر زیر وارد کنید");
        view_signup_revamp_change_phone_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        view_signup_revamp_resend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_signup_revamp_resend_button.setEnabled(false);
                ResendCode();
            }
        });



        view_signup_revamp_input_otp_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() >= 3){
                    CheckCode(SignupMainContainer.inc.current_phone_number,view_signup_revamp_input_otp_edit_text.getText().toString(), PrefranceHelper.GetFCMToken());

                }
            }
        });



















        new CountDownTimer(45000,1000){


            @Override
            public void onTick(long l) {

mainHandler.post(new Runnable() {
    @Override
    public void run() {
        view_signup_revamp_timer_otp_text.setText((l / 1000) + " ثانیه تا درخواست مجدد کد ");

    }
});


            }

            @Override
            public void onFinish() {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        view_signup_revamp_timer_otp_text.setText("");
                        view_signup_revamp_resend_button.setEnabled(true);
                    }
                });

            }
        }.start();




return  view;
    }




private  void CheckCode(String phone_number,String code,String reg_code){

    UserService userService = ServiceGenerator.createService(UserService.class);
    userService.activationCheck(phone_number,code,reg_code).enqueue(new Callback<UserDataResponseJson>() {
        @Override
        public void onResponse(Call<UserDataResponseJson> call, Response<UserDataResponseJson> response) {
            UserDataResponseJson responseUser = response.body();
           mainHandler.post(new Runnable() {
               @Override
               public void run() {

            if (responseUser.getStatus().equals("user_not_exist")) {
                PrefranceHelper.SetPhoneNumber(SignupMainContainer.inc.current_phone_number);
                SignupMainContainer.inc.NameAndFamilyPage();

            }else if (responseUser.getStatus().equals("user_exist")) {


                UserData user = response.body().getData().get(0);
                startActivity(new Intent(getContext(), MainViewContainer.class));
                SignupMainContainer.inc.finish();

            }else if (responseUser.getStatus().equals("active_codes_dont_match")) {
              CodeWrongDialog();
            } else if (responseUser.getStatus().equals("BANNED")) {
BanError();
            }

               }
           });
            }

        @Override
        public void onFailure(Call<UserDataResponseJson> call, Throwable t) {
mainHandler.post(new Runnable() {
    @Override
    public void run() {


        CallSupporterDialog();


    }
});
        }
    });


}
    MaterialDialog mDialog;
private  void CodeWrongDialog(){
   mDialog =
            new MaterialDialog.Builder(getActivity()).setTitle("خطا").setMessage(getString(R.string.verification_code_error)).setCancelable(false).setNegativeButton("بستن", new AbstractDialog.OnClickListener() {
                @Override
                public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
mDialog.dismiss();
                }
            }).build();
    mDialog.show();


}
private  void BanError(){
      mDialog =
            new MaterialDialog.Builder(getActivity()).setTitle("خطا").setMessage(getString(R.string.ban_error)).setCancelable(false).setPositiveButton("تماس با پشتیبانی", new AbstractDialog.OnClickListener() {
                @Override
                public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {


                }
            }).setNegativeButton("بستن", new AbstractDialog.OnClickListener() {
                @Override
                public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {

                }
            }).build();
    mDialog.show();
}
private  void CallSupporterDialog(){
     mDialog =
            new MaterialDialog.Builder(getActivity()).setTitle("خطا").setMessage(getString(R.string.verification_code_error)).setCancelable(false).setPositiveButton("تماس با پشتیبانی", new AbstractDialog.OnClickListener() {
                @Override
                public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {


                }
            }).setNegativeButton("بستن", new AbstractDialog.OnClickListener() {
                @Override
                public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {

                }
            }).build();
    mDialog.show();
}






    private  void ResendCode(){


        UserService userService = ServiceGenerator.createService(UserService.class);
        String phone_number = SignupMainContainer.inc.current_phone_number;
        userService.activationAdd(phone_number).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
             mainHandler.post(new Runnable() {
                 @Override
                 public void run() {
                     if (status.getStatus().equals("success")) {

                         new CountDownTimer(45000,1000){


                             @Override
                             public void onTick(long l) {

                                 mainHandler.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         view_signup_revamp_timer_otp_text.setText((l / 1000) + " ثانیه تا درخواست مجدد کد ");

                                     }
                                 });


                             }

                             @Override
                             public void onFinish() {
                                 mainHandler.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         view_signup_revamp_timer_otp_text.setText("");
                                         view_signup_revamp_resend_button.setEnabled(true);
                                     }
                                 });

                             }
                         }.start();
                     } else {

                     }
                 }
             });

            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

            }
        });


    }





    }
