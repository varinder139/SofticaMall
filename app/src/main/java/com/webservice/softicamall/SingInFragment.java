package com.webservice.softicamall;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.webservice.softicamall.RegisterActivity.onResetPasswordFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class SingInFragment extends Fragment {


    public SingInFragment() {
        // Required empty public constructor
    }

    private TextView dontHaveAnAount, forgotPassword;
    private FrameLayout parentFrameLayout;

    private EditText email, password;
    private Button signInbtn;
    private ProgressBar progressBar;
    private ImageButton closebtn;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private FirebaseAuth firebaseAuth;

    public static boolean disableCloseBtn = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sing_in, container, false);
        dontHaveAnAount = view.findViewById(R.id.tv_dont_have_an_acount);
        email = view.findViewById(R.id.sign_in_email);
        password = view.findViewById(R.id.sign_in_password);
        signInbtn = view.findViewById(R.id.sign_in_btn);
        progressBar = view.findViewById(R.id.sign_in_progressbar);
        closebtn = view.findViewById(R.id.sign_in_close_btn);
        forgotPassword = view.findViewById(R.id.sign_in_forgot_password);

        parentFrameLayout = getActivity().findViewById(R.id.register_framLayout);

        firebaseAuth = FirebaseAuth.getInstance();

        if (disableCloseBtn){
            closebtn.setVisibility(View.GONE);
        }else {
            closebtn.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dontHaveAnAount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignUpFragment());
            }
        });

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity2.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onResetPasswordFragment = true;
                setFragment(new ResetPasswordFragment());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chekEmailAndPassword();
            }
        });
    }



    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_form_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(email.getText())){
            if (!TextUtils.isEmpty(password.getText())){
                signInbtn.setEnabled(true);
                signInbtn.setTextColor(Color.rgb(255, 255, 255));
            }else
            {
                signInbtn.setEnabled(false);
                signInbtn.setTextColor(Color.argb(50,255,255, 255));
            }
        }else
        {
            signInbtn.setEnabled(false);
            signInbtn.setTextColor(Color.argb(50,255,255, 255));
        }
    }

    private void chekEmailAndPassword() {
        if (email.getText().toString().matches(emailPattern)){
            if (password.length() >= 8){

                progressBar.setVisibility(View.VISIBLE);
                signInbtn.setEnabled(false);
                signInbtn.setTextColor(Color.argb(50,255,255, 255));

                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    mainIntent();

                                }else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signInbtn.setEnabled(true);
                                    signInbtn.setTextColor(Color.rgb(255, 255, 255));

                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }else {
                Toast.makeText(getActivity(), "Incorrect Email And Password", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(), "Incorrect Email And Password", Toast.LENGTH_SHORT).show();
        }

    }

    private void mainIntent(){
        if (disableCloseBtn){
            disableCloseBtn = false;
        } else {
            Intent intent = new Intent(getActivity(), MainActivity2.class);
            startActivity(intent);
        }
        getActivity().finish();
    }

}