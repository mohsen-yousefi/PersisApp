package ir.rahcode.persisapp.layouts.FragmentScreens;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;

import ir.rahcode.persisapp.R;

public class WelcomFragment extends Fragment {


MaterialTextView view_welcome_to_log_in_input;
    RelativeLayout view_welcome_main_layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_welcome,container,false);

view_welcome_main_layout = view.findViewById(R.id.view_welcome_main_layout);
        view_welcome_main_layout.setAlpha(0);
view_welcome_main_layout.animate().alpha(1.0f).setDuration(1500).start();


        view_welcome_to_log_in_input = view.findViewById(R.id.view_welcome_to_log_in_input);
        view_welcome_to_log_in_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });









        return  view;
    }
}
