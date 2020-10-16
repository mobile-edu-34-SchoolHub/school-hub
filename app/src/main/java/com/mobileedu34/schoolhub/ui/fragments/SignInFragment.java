package com.mobileedu34.schoolhub.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mobileedu34.schoolhub.R;
import com.mobileedu34.schoolhub.callbacks.users.ActionContract;
import com.mobileedu34.schoolhub.callbacks.users.UsersActionController;
import com.mobileedu34.schoolhub.ui.activities.MainActivity;
import com.mobileedu34.schoolhub.utils.DialogLoader;

public class SignInFragment extends Fragment implements ActionContract.View {

    private TextInputEditText edtEmailAddress;
    private TextInputEditText edtPassword;
    private DialogLoader dialogLoader;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogLoader = new DialogLoader(requireContext());
        UsersActionController.getInstance().setListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity)getActivity()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sign_in, container, false);
        init(root);
        return root;
    }

    private void init(View root) {
        edtEmailAddress = root.findViewById(R.id.edt_email_address);
        edtPassword = root.findViewById(R.id.edt_password);
        MaterialButton btnSignUp = root.findViewById(R.id.btn_sign_up);
        MaterialButton btnSignIn = root.findViewById(R.id.btn_sign_in);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view)
                        .navigate(R.id.action_nav_sign_in_to_nav_sign_up);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUserWithEmailAndPassword();
            }
        });
    }

    // Email and password Sign in

    private void signInUserWithEmailAndPassword() {
        String userEmail = edtEmailAddress.getText().toString().trim();
        String userPassword = edtPassword.getText().toString().trim();

        if(! userEmail.isEmpty() && ! userPassword.isEmpty()) {
            dialogLoader.showProgressDialog(getResources().getString(R.string.signing_in));
            UsersActionController.getInstance().signInUser(userEmail, userPassword);
        } else {
            Toast.makeText(requireContext(), "Please, fill the form fields", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateUserSuccess() {
        // Nothing to override here
    }

    @Override
    public void onCreateUserFailure(String message) {
        // Nothing to override here
    }

    @Override
    public void onSignInUserSuccess() {
        dialogLoader.dismissProgressDialog();
        requireActivity().finish();
        startActivity(new Intent(requireActivity(), MainActivity.class));
    }

    @Override
    public void onSignInUserFailure(String message) {
        dialogLoader.dismissProgressDialog();
        try {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }catch (Exception ignore) {}
    }

    @Override
    public void onSignUpUserSuccess() {
        // Nothing to override here
    }

    @Override
    public void onSignUpUserFailure(String message) {
        // Nothing to override here
    }

    @Override
    public void onPause() {
        super.onPause();
        /*if(edtEmailAddress != null && edtPassword != null)
            resetFields();

         */
    }

    private void resetFields() {
        edtEmailAddress.setText(null);
        edtPassword.setText(null);
    }
}