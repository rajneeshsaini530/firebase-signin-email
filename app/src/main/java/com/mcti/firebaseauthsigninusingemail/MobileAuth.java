package com.mcti.firebaseauthsigninusingemail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MobileAuth extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText mobile,OTP;
    private Button send,verify;
    private String code;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_auth);
        firebaseAuth = FirebaseAuth.getInstance();
        mobile = findViewById(R.id.mobile);
        OTP = findViewById(R.id.otp);
        send = findViewById(R.id.send);
        verify = findViewById(R.id.verify);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userMObile= mobile.getText().toString();
                if("".equals(userMObile)){
                    mobile.setError("Enter Mobile");
                }else{
                    sendCode("+91"+userMObile);
                }
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userOtp = OTP.getText().toString();
                if("".equals(userOtp)){
                    Toast.makeText(MobileAuth.this, "enter OTP", Toast.LENGTH_SHORT).show();
                }else{
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(code,userOtp);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void sendCode(String userMObile) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                userMObile,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            // ...
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // The SMS quota for the project has been exceeded
                            // ...
                        }
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        code = s;
                        Toast.makeText(MobileAuth.this, "code send", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MobileAuth.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MobileAuth.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
