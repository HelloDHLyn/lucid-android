package com.lynlab.lucid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lynlab.lucid.R;

/**
 * @author lyn
 * @since 2016/09/03
 */
public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button loginButton = (Button) findViewById(R.id.activity_start_login);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_start_login:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }
}
