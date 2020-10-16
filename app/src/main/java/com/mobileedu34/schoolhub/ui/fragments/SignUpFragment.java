package com.mobileedu34.schoolhub.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mobileedu34.schoolhub.models.User;
import com.mobileedu34.schoolhub.ui.activities.MainActivity;
import com.mobileedu34.schoolhub.utils.DialogLoader;


public class SignUpFragment extends Fragment implements ActionContract.View {

    private TextInputEditText edtEmailAddress;
    private TextInputEditText edtPassword;
    private TextInputEditText edtFullName;
    private DialogLoader dialogLoader;
    private String userEmail;
    private String userName;


    public SignUpFragment() {
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sign_up, container, false);
        init(root);
        return root;
    }

    private void init(View root) {
        edtEmailAddress = root.findViewById(R.id.edt_email_address);
        edtPassword = root.findViewById(R.id.edt_password);
        edtFullName = root.findViewById(R.id.edt_full_name);
        MaterialButton btnSignUp = root.findViewById(R.id.btn_sign_up);
        MaterialButton btnSignIn = root.findViewById(R.id.btn_sign_in);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view)
                        .navigate(R.id.action_nav_sign_up_to_nav_sign_in);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpUserWithEmailAndPassword();
            }
        });
    }

    // Email and password Sign Up
    private void signUpUserWithEmailAndPassword() {
        userEmail = edtEmailAddress.getText().toString().trim();
        userName = edtFullName.getText().toString().trim();
        String userPassword = edtPassword.getText().toString().trim();

        if(! userName.isEmpty() && !userEmail.isEmpty() && ! userPassword.isEmpty()) {
            dialogLoader.showProgressDialog(getResources().getString(R.string.signing_up));
            UsersActionController.getInstance().signUpUser(userEmail, userPassword);
        } else {
            Toast.makeText(requireContext(), "Please, fill the form fields", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateUserSuccess() {
        dialogLoader.dismissProgressDialog();
        requireActivity().finish();
        startActivity(new Intent(requireActivity(), MainActivity.class));
    }

    @Override
    public void onCreateUserFailure(String message) {
        try {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }catch (Exception ignore) {}
        dialogLoader.dismissProgressDialog();
    }

    @Override
    public void onSignInUserSuccess() {
        // Nothing to override here
    }

    @Override
    public void onSignInUserFailure(String message) {
        // Nothing to override here
    }

    @Override
    public void onSignUpUserSuccess() {
        User user = new User();
        user.setEmailAddress(userEmail);
        user.setFullName(userName);
        UsersActionController.getInstance().createUser(user);
    }

    @Override
    public void onSignUpUserFailure(String message) {
        try {
            dialogLoader.dismissProgressDialog();
        }catch (Exception ignore) {}
        dialogLoader.dismissProgressDialog();
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
        edtFullName.setText(null);
        edtPassword.setText(null);
    }
}