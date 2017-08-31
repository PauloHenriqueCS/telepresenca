package com.sinch.android.rtc.sample.video;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.sinch.android.rtc.SinchError;

//

public class LoginActivity extends BaseActivity implements SinchService.StartFailedListener {

    private Button mLoginButton;
    private Button mNurseV;
    private Button mNurseIs;
    private ProgressDialog mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        int PERMISSION_ALL = 1;
        final String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE};
       if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mLoginButton.setEnabled(false);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClicked();
            }
        });

        mNurseV = (Button) findViewById(R.id.nurseV);
        mNurseV.setOnClickListener(new OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           nurseVoice();
                                       }
                                   }
        );
        mNurseIs = (Button) findViewById(R.id.nurseIs);
        mNurseIs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                nurseIs();
            }
        }
        );
    }

    @Override
    protected void onServiceConnected() {
        mLoginButton.setEnabled(true);
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
    }

    @Override
    public void onStarted() {
        openPlaceCallActivity();
    }

    private void loginClicked() {
        String userName = "gisele";

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
            showSpinner();
        } else {
            openPlaceCallActivity();
        }
    }

    private void openPlaceCallActivity() {
        Intent mainActivity = new Intent(this, PlaceCallActivity.class);
        startActivity(mainActivity);
    }

    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Logando");
        mSpinner.setMessage("Aguarde...");
        mSpinner.show();
    }
    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void nurseVoice(){
       Intent Vnurse = new Intent(LoginActivity.this, NurseVoice.class);
        startActivity(Vnurse);
    }


    private void nurseIs(){
        Intent Inurse = new Intent(LoginActivity.this, NurseIs.class);
        startActivity(Inurse);
    }
}
