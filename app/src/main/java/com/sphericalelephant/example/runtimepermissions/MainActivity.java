package com.sphericalelephant.example.runtimepermissions;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * We are implementing ActivityCompat.OnRequestPermissionsResultCallback in order to be able to receive
 * permission callbacks
 */
public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int REQUEST_CODE = 1;

    /**
     * used to display information about the grant status of COARSE and FINE location
     */
    private TextView infoText;
    /**
     * used to display the rationale behind granting access to COARSE and FINE location
     */
    private TextView explain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainactivity);
        findViewById(R.id.activity_mainactivity_b_requestpermissions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if permissions have been granted and ...
                if (coarseLocationDenied() || fineLocationDenied()) {
                    ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS, REQUEST_CODE); // ... request permissions if they've not been granted
                } else {
                    Toast.makeText(getApplicationContext(), R.string.activity_mainactivity_permissionsgranted, Toast.LENGTH_SHORT).show(); // .. show an informative toast
                }
            }
        });

        infoText = (TextView) findViewById(R.id.activity_mainactivity_tv_info);
        explain = (TextView) findViewById(R.id.activity_mainactivity_tv_explain);
    }

    private boolean coarseLocationDenied() {
        return ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED;
    }

    private boolean fineLocationDenied() {
        return ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE) { // verify that we are dealing with the correct request
            StringBuilder text = new StringBuilder();
            for (int i = 0; i < permissions.length; i++) {
                text.append(permissions[i]);
                text.append(' ');
                text.append(
                        grantResults[i] == PackageManager.PERMISSION_DENIED ?
                                getString(R.string.activity_mainactivity_denied) :
                                getString(R.string.activity_mainactivity_granted));
                text.append('\n');
            }
            infoText.setText(text);

            // show info to the user if she / he declined to grant the permissions
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                explain.setText(R.string.activity_mainactivity_coarseandfine);
            } else {
                explain.setText("");
            }
        }
    }
}
