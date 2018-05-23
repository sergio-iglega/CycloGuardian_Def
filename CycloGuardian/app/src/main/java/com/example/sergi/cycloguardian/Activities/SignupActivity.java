package com.example.sergi.cycloguardian.Activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergi.cycloguardian.MyApplication;
import com.example.sergi.cycloguardian.R;
import com.example.sergi.cycloguardian.Retrofit.APIClient;
import com.example.sergi.cycloguardian.Retrofit.RestInterface;
import com.example.sergi.cycloguardian.Retrofit.SignUpResponse;

import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    EditText _nameText;
    EditText _emailText;
    EditText _passwordText;
    FancyButton _signupButton;
    TextView _loginLink;
    RestInterface restInterface;
    Boolean signUpSuccess = false;
    String msgSignUp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        restInterface = APIClient.getRetrofit().create(RestInterface.class);
        _nameText = (EditText) findViewById(R.id.input_name);
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _signupButton = (FancyButton) findViewById(R.id.btn_signup);
        _loginLink = (TextView) findViewById(R.id.link_login);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed(getString(R.string.signup_fail));
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.ThemeOverlay_AppCompat_Dark_ActionBar);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own signup logic here.
        Call<SignUpResponse> signUpResponseCall = restInterface.signUpUser(name, " ", " ", email, password);
        signUpResponseCall.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {

                Log.d("TAG",response.code()+"");

                SignUpResponse signUpResponse = response.body();
                String type = signUpResponse.getType();
                String registry = signUpResponse.getRegistry();
                String rval = signUpResponse.getRval();

                if (registry.equals("success")) {
                    signUpSuccess = true;
                    //TODO gurdar en BD local

                } else {
                    if (rval.equals("existing_email")) {
                        msgSignUp = getString(R.string.existing_email);
                    }
                }


            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                call.cancel();
            }
        });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (signUpSuccess)
                            onSignupSuccess();
                        else
                            onSignupFailed(msgSignUp);
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed(String mensaje) {
        Toast.makeText(getBaseContext(), mensaje, Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("At least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
