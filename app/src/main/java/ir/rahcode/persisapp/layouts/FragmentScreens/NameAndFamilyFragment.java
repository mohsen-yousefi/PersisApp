package ir.rahcode.persisapp.layouts.FragmentScreens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.shreyaspatil.MaterialDialog.AbstractDialog;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

import ir.rahcode.persisapp.R;
import ir.rahcode.persisapp.api.ServiceGenerator;
import ir.rahcode.persisapp.api.services.UserService;
import ir.rahcode.persisapp.layouts.LoginScreens.SignupMainContainer;
import ir.rahcode.persisapp.layouts.SeperatedScreen.MainViewContainer;
import ir.rahcode.persisapp.models.UserData;
import ir.rahcode.persisapp.models.json.RegisterResponseJson;
import ir.rahcode.persisapp.utils.PrefranceHelper;
import ir.rahcode.persisapp.utils.StaticCenteral;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NameAndFamilyFragment extends Fragment {
AppCompatImageView back_signup;
MaterialButton view_signup_revamp_recover_account_btn,view_signup_revamp_next_step_btn;
TextInputEditText view_signup_revamp_input_full_name_clearable_edit_text,view_signup_revamp_input_full_family_clearable_edit_text,view_signup_revamp_input_email_clearable_edit_text;
Handler mainHandler;

    MaterialDialog mDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_signup_profile,container,false);
        view_signup_revamp_recover_account_btn = view.findViewById(R.id.view_signup_revamp_recover_account_btn);
        view_signup_revamp_next_step_btn = view.findViewById(R.id.view_signup_revamp_next_step_btn);
        view_signup_revamp_input_full_name_clearable_edit_text = view.findViewById(R.id.view_signup_revamp_input_full_name_clearable_edit_text);
        view_signup_revamp_input_full_family_clearable_edit_text = view.findViewById(R.id.view_signup_revamp_input_full_family_clearable_edit_text);
        view_signup_revamp_input_email_clearable_edit_text = view.findViewById(R.id.view_signup_revamp_input_email_clearable_edit_text);
        back_signup = view.findViewById(R.id.back_signup);

        view_signup_revamp_next_step_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        if(view_signup_revamp_input_full_name_clearable_edit_text.getText().length() < 4){
            view_signup_revamp_input_full_name_clearable_edit_text.setError("نام  نمی تواند خالی باشد");
        }else if(view_signup_revamp_input_full_family_clearable_edit_text.getText().toString().length() < 4){
            view_signup_revamp_input_full_family_clearable_edit_text.setError("نام خانوادگی نمی تواند خالی باشد");

        }else{

            RegisterUser(view_signup_revamp_input_full_name_clearable_edit_text.getText().toString(),view_signup_revamp_input_full_family_clearable_edit_text.getText().toString(),view_signup_revamp_input_email_clearable_edit_text.getText().toString());


        }



            }
        });



        back_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });


        view_signup_revamp_recover_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



        }
        });








        return  view;

    }



    private void RegisterUser(String name,String family,String email){

        UserService service = ServiceGenerator.createService(UserService.class);
        service.register(name, family,email, PrefranceHelper.GetFCMToken(), PrefranceHelper.getPhoneNumber()).enqueue(new Callback<RegisterResponseJson>() {
            @Override
            public void onResponse(Call<RegisterResponseJson> call, Response<RegisterResponseJson> response) {
              mainHandler.post(new Runnable() {
                  @Override
                  public void run() {
                      if (response.isSuccessful()) {
                          if (response.body().getMessage().equalsIgnoreCase("success")) {
                              UserData user = response.body().getData().get(0);
                              StaticCenteral.user = user;


                              startActivity(new Intent(getContext(), MainViewContainer.class));
                              SignupMainContainer.inc.finish();

                          } else {
                              CallSupporterDialog();
                          }
                      } else {
                          CallSupporterDialog();
                      }
                  }
              });

            }

            @Override
            public void onFailure(Call<RegisterResponseJson> call, Throwable t) {
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
        mDialog =
                new MaterialDialog.Builder(getActivity()).setTitle("خطا").setMessage(getString(R.string.name_and_family_error)).setCancelable(false).setPositiveButton("تماس با پشتیبانی", new AbstractDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {


                    }
                }).setNegativeButton("بستن", new AbstractDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
mDialog.dismiss();
                    }
                }).build();
        mDialog.show();
    }


}
