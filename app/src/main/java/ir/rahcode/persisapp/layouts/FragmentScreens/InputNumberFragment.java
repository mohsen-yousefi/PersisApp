package ir.rahcode.persisapp.layouts.FragmentScreens;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.shreyaspatil.MaterialDialog.AbstractDialog;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

import ir.rahcode.persisapp.R;
import ir.rahcode.persisapp.api.ServiceGenerator;
import ir.rahcode.persisapp.api.services.UserService;
import ir.rahcode.persisapp.layouts.LoginScreens.SignupMainContainer;
import ir.rahcode.persisapp.models.Status;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputNumberFragment extends Fragment {


    TextInputEditText view_signup_revamp_input_phone_number_clearable_edit_text;
    Button view_signup_revamp_next_step_btn;
Handler mainHandler;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    private  boolean isVaildMobile(String Mobile){
        return android.util.Patterns.PHONE.matcher(Mobile).matches();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_signup_revamp,container,false);

        view_signup_revamp_input_phone_number_clearable_edit_text = view.findViewById(R.id.view_signup_revamp_input_phone_number_clearable_edit_text);
        view_signup_revamp_next_step_btn = view.findViewById(R.id.view_signup_revamp_next_step_btn);


        view_signup_revamp_input_phone_number_clearable_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
if(view_signup_revamp_input_phone_number_clearable_edit_text.getText().length() >=10){
SignupMainContainer.inc.current_phone_number = view_signup_revamp_input_phone_number_clearable_edit_text.getText().toString();

    view_signup_revamp_next_step_btn.setEnabled(true);

}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });





        view_signup_revamp_next_step_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isVaildMobile(view_signup_revamp_input_phone_number_clearable_edit_text.getText().toString())){
SendCode();

                }else{


                    view_signup_revamp_input_phone_number_clearable_edit_text.setError("تلفن همراه وارد شده صحیح نمی  باشد");




                }






            }
        });









        return  view;
    }

  private  void SendCode(){
      UserService userService = ServiceGenerator.createService(UserService.class);
      String phone_number = SignupMainContainer.inc.current_phone_number;
      userService.activationAdd(phone_number).enqueue(new Callback<Status>() {
          @Override
          public void onResponse(Call<Status> call, Response<Status> response) {
              Status status = response.body();
              if (status.getStatus().equals("success")) {

                  SignupMainContainer.inc.InputCodePage();

              } else {
CallSupporterDialog();
              }
          }

          @Override
          public void onFailure(Call<Status> call, Throwable t) {
mainHandler.post(new Runnable() {
    @Override
    public void run() {
   CallSupporterDialog();
    }
});
          }
      });

  }



    private  void CallSupporterDialog(){
        MaterialDialog mDialog =
                new MaterialDialog.Builder(getActivity()).setTitle("خطا").setMessage(getString(R.string.any_error_occurrence)).setCancelable(false).setPositiveButton("تماس با پشتیبانی", new AbstractDialog.OnClickListener() {
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



}
