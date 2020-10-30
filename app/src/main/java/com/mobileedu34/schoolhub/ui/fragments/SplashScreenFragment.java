package com.mobileedu34.schoolhub.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobileedu34.schoolhub.R;
import com.mobileedu34.schoolhub.preferences.AppPreferences;
import com.mobileedu34.schoolhub.ui.activities.MainActivity;

public class SplashScreenFragment extends Fragment {

    private Handler handler;
    private boolean paused;
    private FirebaseUser fUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if ((fUser != null && fUser.getEmail() != null) || (new AppPreferences(requireContext()).getUserId() != null)) {
                        requireActivity().finish();
                        startActivity(new Intent(requireActivity(), MainActivity.class));
                    } else {
                        NavHostFragment
                                .findNavController(requireParentFragment())
                                .navigate(R.id.action_nav_splash_screen_to_nav_sign_in);
                    }
                } catch (Exception ignore) {
                }
            }
        }, 1500);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (paused) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (fUser != null && fUser.getEmail() != null) {
                            requireActivity().finish();
                            startActivity(new Intent(requireActivity(), MainActivity.class));
                        } else {
                            NavHostFragment
                                    .findNavController(requireParentFragment())
                                    .navigate(R.id.action_nav_splash_screen_to_nav_sign_in);
                        }
                    } catch (Exception ignore) {
                    }
                }
            }, 1500);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
    }
}