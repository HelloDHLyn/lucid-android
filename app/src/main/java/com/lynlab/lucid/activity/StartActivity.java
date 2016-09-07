package com.lynlab.lucid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lynlab.lucid.R;
import com.lynlab.lucid.api.LucidApiManager;
import com.lynlab.lucid.model.AccessToken;
import com.lynlab.lucid.util.PreferenceUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author lyn
 * @since 2016/09/03
 */
public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (PreferenceUtil.getStringPreference(this, PreferenceUtil.KEY_ACCESS_TOKEN, "").length() > 0) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_start);

        Button loginButton = (Button) findViewById(R.id.activity_start_login);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_start_login:
                TextView emailTextView = (TextView) findViewById(R.id.activity_start_email);
                TextView passwordTextView = (TextView) findViewById(R.id.activity_start_password);

                String encryptedPassword;
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    byte[] digest = md.digest(passwordTextView.getText().toString().getBytes());

                    encryptedPassword = Base64.encodeToString(digest, Base64.DEFAULT).trim();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    return;
                }

                LucidApiManager apiManager = LucidApiManager.getInstance(this);
                Call<AccessToken> call = apiManager.getService().login(
                        emailTextView.getText().toString(), encryptedPassword);
                call.enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        if (response.code() == 422) { // Unprocessable Entity
                            Toast.makeText(StartActivity.this, R.string.error_login, Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 500) {
                            Toast.makeText(StartActivity.this, R.string.error_500, Toast.LENGTH_SHORT).show();
                        } else {
                            PreferenceUtil.setPreference(StartActivity.this, PreferenceUtil.KEY_ACCESS_TOKEN, response.body().getAccessToken());

                            startActivity(new Intent(StartActivity.this, MainActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
        }
    }
}
