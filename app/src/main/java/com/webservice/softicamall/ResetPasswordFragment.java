package com.webservice.softicamall;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ResetPasswordFragment extends Fragment {

    public ResetPasswordFragment() {
        // Required empty public constructor
    }
    private EditText registerEmail;
    private Button resetPasswordbtn;
    private TextView goBack;
    private FrameLayout parentFrameLayout;

    private FirebaseAuth firebaseAuth;

    private ViewGroup emailIonContainar;
    private ImageView emailIcon;
    private TextView emailIconText;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
         registerEmail  = view.findViewById(R.id.forgot_password_email);
         resetPasswordbtn = view.findViewById(R.id.reset_password_btn);
         goBack = view.findViewById(R.id.rest_password_goback);

         emailIonContainar = view.findViewById(R.id.forgot_password_email_container);
         emailIcon = view.findViewById(R.id.forgot_password_email_icon);
         emailIconText = view.findViewById(R.id.forgot_password_email_icon_text);
         progressBar = view.findViewById(R.id.forgot_password_progressbar);

         parentFrameLayout = getActivity().findViewById(R.id.register_framLayout);

         firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                      checkInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SingInFragment());
            }
        });

        resetPasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(emailIonContainar);
                emailIconText.setVisibility(View.GONE);

                TransitionManager.beginDelayedTransition(emailIonContainar);
                emailIcon.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                resetPasswordbtn.setEnabled(false);
                resetPasswordbtn.setTextColor(Color.argb(50, 255,255,255));

                firebaseAuth.sendPasswordResetEmail(registerEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                   /* emailIconText.setText("Recovery email sent successfully ! check your inbox");
                                    emailIconText.setTextColor(getResources().getColor(R.color.successGreen));
                                    TransitionManager.beginDelayedTransition(emailIonContainar);
                                    emailIconText.setVisibility(View.VISIBLE);
                                    emailIcon.setImageResource(R.drawable.icon_mail_outline);  */

                                    //Toast.makeText(getActivity(), "Email sent Sucessfully!", Toast.LENGTH_SHORT).show();
                                    ScaleAnimation scaleAnimation = new ScaleAnimation(1,0,1,0,emailIcon.getWidth()/2,emailIcon.getHeight()/2);
                                    scaleAnimation.setDuration(100);
                                    scaleAnimation.setInterpolator(new AccelerateInterpolator());
                                    scaleAnimation.setRepeatMode(Animation.REVERSE);
                                    scaleAnimation.setRepeatCount(1);

                                    scaleAnimation.setAnimationListener(new Animation.AnimationListener(){

                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            emailIconText.setText("Recovery email sent successfully ! check your inbox");
                                            emailIconText.setTextColor(getResources().getColor(R.color.successGreen));

                                            TransitionManager.beginDelayedTransition(emailIonContainar);
                                            emailIconText.setVisibility(View.VISIBLE);

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {

                                            emailIcon.setImageResource(R.drawable.icon_mail_outline);
                                        }
                                    });

                                    emailIcon.startAnimation(scaleAnimation);
                                   // emailIconText.startAnimation(scaleAnimation);

                                }else {
                                    String error = task.getException().getMessage();
                                   // Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();


                                    emailIconText.setText(error);
                                    emailIconText.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    TransitionManager.beginDelayedTransition(emailIonContainar);
                                    emailIconText.setVisibility(View.VISIBLE);

                                    resetPasswordbtn.setEnabled(true);
                                    resetPasswordbtn.setTextColor(Color.rgb(255,255,255));

                                }
                                progressBar.setVisibility(View.GONE);

                            }
                        });
            }
        });
    }

    private void checkInput() {
        if (TextUtils.isEmpty(registerEmail.getText())){
            resetPasswordbtn.setEnabled(false);
            resetPasswordbtn.setTextColor(Color.argb(50,255,255,255));
        }else {
            resetPasswordbtn.setEnabled(true);
            resetPasswordbtn.setTextColor(Color.rgb(255,255,255));
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }


}