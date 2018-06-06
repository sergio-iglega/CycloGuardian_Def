package com.example.sergi.cycloguardian.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergi.cycloguardian.Database.AppDataBase;
import com.example.sergi.cycloguardian.Database.UserEntity;
import com.example.sergi.cycloguardian.MyApplication;
import com.example.sergi.cycloguardian.R;
import com.example.sergi.cycloguardian.Retrofit.APIClient;
import com.example.sergi.cycloguardian.Retrofit.LoginResponse;
import com.example.sergi.cycloguardian.Retrofit.RestInterface;
import com.example.sergi.cycloguardian.Utils.Constants;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText _emailText, _passwordText;
    FancyButton _loginButton;
    TextView _signupLink;
    RestInterface restInterface;
    Boolean loginSuccess = false;
    String msgLogin;
    Integer idUser;
    String token;
    AppDataBase myDb;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            restInterface = APIClient.getRetrofit(getApplicationContext()).create(RestInterface.class);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        getSupportActionBar().hide();

        msgLogin = getString(R.string.conexion_server_fail);

        myDb = AppDataBase.getAppDataBase(this);

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed(getString(R.string.login_fail));
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Authentication");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Checking credentials");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        Call<LoginResponse> loginResponseCall = restInterface.loginUser(email, password);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                LoginResponse loginResponse = response.body();
                String type = loginResponse.getType();
                String acceso = loginResponse.getAcceso();
                String rval = loginResponse.getRval();
                idUser = loginResponse.getIdUser();
                token = loginResponse.getToken();

                if (acceso.equals(Constants.SERVER_LOGIN_SUCCESS)) {
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

        //loginSuccess = true;

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if (loginSuccess) {
                            createRegisterToDataBase(myDb, idUser, email, password, token);
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

        //Change to start Activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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


    private void createRegisterToDataBase(AppDataBase dataBase, int idUser, String email, String password, String token) {
        UserEntity userEntity = new UserEntity();
        userEntity.setIdUser(idUser);
        userEntity.setEmail(email);
        userEntity.setPassword(password);
        userEntity.setToken(token);

        //Save to DB
        dataBase.userDao().insertUser(userEntity);
    }
}