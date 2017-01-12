package com.example.wojciech.jasiewiczwojciech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    public static EditText passwordEditText;
    public static EditText loginEditText;
    public static Button loginButton;
    public static TextView emailInfo;
    public static TextView passwordInfo;
    public static Intent intent1;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        passwordEditText = (EditText)findViewById(R.id.password);
        loginEditText = (EditText)findViewById(R.id.login);
        loginButton = (Button)findViewById(R.id.loginButton);
        emailInfo = (TextView)findViewById(R.id.emailInfo);
        passwordInfo = (TextView) findViewById(R.id.passwordInfo);
        intent1 = getIntent();

        OnClickListener onClickLoginButton = new OnClickListener() {
            @Override
            public void onClick(View v) {

                String enterEmail = loginEditText.getText().toString();
                String validEmailInfo = MyLogger.validateEmail(enterEmail);
                emailInfo.setText(validEmailInfo);

                String enterPassword = passwordEditText.getText().toString();
                String validPasswordInfo = MyLogger.validatePassword(enterPassword);
                passwordInfo.setText(validPasswordInfo);


                if((validEmailInfo == "") && (validPasswordInfo == "")){
                    Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                    copyIntent(intent2);

                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("Logged", true);
                    editor.putString("email", enterEmail);
                    editor.commit();

                    startActivity(intent2);
                    finish();
                }
            }
        };

        loginButton.setOnClickListener(onClickLoginButton);
    }

    public void copyIntent(Intent i2){

        int[] colorTable = intent1.getIntArrayExtra("colorsTable");
        int[] theMostPopularColorInfo = intent1.getIntArrayExtra("color");
        int[] theMostPopularDigitInfo = intent1.getIntArrayExtra("digit");
        int[] cornersValues = intent1.getIntArrayExtra("corners");

        i2.putExtra("colorsTable", colorTable);
        i2.putExtra("color", theMostPopularColorInfo);
        i2.putExtra("digit", theMostPopularDigitInfo);
        i2.putExtra("corners", cornersValues);
    }
}

