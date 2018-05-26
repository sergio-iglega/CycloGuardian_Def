package com.example.sergi.cycloguardian.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.sergi.cycloguardian.Retrofit.LoginResponse;
import com.example.sergi.cycloguardian.Retrofit.RestInterface;
import com.example.sergi.cycloguardian.Utils.Constants;

import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;


    EditText _emailText, _passwordText;
    FancyButton _loginButton;
    TextView _signupLink;
    RestInterface restInterface;
    Boolean loginSuccess = false;
    MyApplication myApplication;
    String msgLogin;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myApplication = ((MyApplication)this.getApplication());
        restInterface = APIClient.getRetrofit().create(RestInterface.class);
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _loginButton = (FancyButton) findViewById(R.id.btn_login);
        _signupLink = (TextView) findViewById(R.id.link_signup);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        msgLogin = getString(R.string.conexion_server_fail);
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed(getString(R.string.login_fail));
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.ThemeOverlay_AppCompat_Dark_ActionBar);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        Call<LoginResponse> loginResponseCall = restInterface.loginUser(email, password);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                LoginResponse loginResponse = response.body();
                String type = loginResponse.getType();
                String acceso = loginResponse.getAcceso();
                String rval = loginResponse.getRval();
                Integer idUser = loginResponse.getIdUser();

                if (acceso.equals(Constants.SERVER_LOGIN_SUCCESS)) {
                    //myApplication.mySession.setUserID(idUser);
                    loginSuccess = true;
                } else {
                    loginSuccess = false;
                    if (rval.equals("unexisting_email")) {
                        msgLogin = getString(R.string.incorrect_email);
                    }

                    if (rval.equals("incorrect_password")) {
                        msgLogin = getString(R.string.incorrect_password);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                call.cancel();
                msgLogin = getString(R.string.conexion_server_fail);
            }
        });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if (loginSuccess) {
                            onLoginSuccess();

                        } else
                            onLoginFailed(msgLogin);

                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                Toast.makeText(getBaseContext(), getText(R.string.singup_success), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed(String mensaje) {
        Toast.makeText(getBaseContext(), mensaje, Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

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