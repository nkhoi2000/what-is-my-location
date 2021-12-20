package com.huawei.finalassignment.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.huawei.finalassignment.R;
import com.huawei.finalassignment.models.Account;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;

public class LoginActivity extends AppCompatActivity {
    // AccountAuthService provides a set of APIs, including silentSignIn, getSignInIntent, and signOut.
    private AccountAuthService mAuthService;
    // Define the request code for signInIntent.
    private static final int REQUEST_CODE_SIGN_IN = 1000;
    // Define the log tag.
    private static final String TAG = "Account_Login";
    private static final String[] RUNTIME_PERMISSION = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (!hasPermissions(this, RUNTIME_PERMISSION)) {
            ActivityCompat.requestPermissions(this, RUNTIME_PERMISSION, 100);
        }
        Button btn_login = findViewById(R.id.btn_login_username);

        btn_login.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "Cannot find your username & password!", Toast.LENGTH_SHORT).show());
        findViewById(R.id.btn_login).setOnClickListener(v -> {
            Log.d(TAG, "Login click!");
            silentSignInByHwId();
        });
    }

    private void silentSignInByHwId() {
        Log.d(TAG, "silentSignInByHwId: start");
        AccountAuthParams mAuthParam = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setEmail()
                .createParams();
        Log.d(TAG, "silentSignInByHwId: " + mAuthParam.toString());
        mAuthService = AccountAuthManager.getService(this, mAuthParam);
        Intent sigInIntent = mAuthService.getSignInIntent();
        startActivityForResult(sigInIntent, REQUEST_CODE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // requested is checked to make that the activity that invoked passed the result.This can be any code

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            Log.i(TAG, "onActivityResult of sigInInIntent, request code: " + REQUEST_CODE_SIGN_IN);
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            Log.d(TAG, "onActivityResult, authAccountTask status: " + authAccountTask.isSuccessful());
            if (authAccountTask.isSuccessful()) {
                // The sign-in is successful, and the authAccount object that contains the HUAWEI ID information is obtained.
                AuthAccount authAccount = authAccountTask.getResult();
                Log.i(TAG, "display:" + authAccount.getOpenId());

                // when the user login successfully , i will get all the details and i am passing all to the next activity via the intent .
                Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                Account account = new Account(
                        authAccount.getDisplayName(),
                        authAccount.getEmail(),
                        authAccount.getAvatarUri().toString());
                intent.putExtra("Account", account);
                startActivity(intent);
                Toast.makeText(
                        getApplicationContext(),
                        "Welcome " + authAccount.getDisplayName(),
                        Toast.LENGTH_SHORT).show();
            } else {
                // The sign-in fails. Find the failure cause from the status code. For more information, please refer to the "Error Codes" section in the API Reference.
                Log.e(TAG, "sign in failed, status code : " + ((ApiException) authAccountTask.getException()).getStatusCode());
            }
        }
    }

    /**
     * @param context - the IPC context
     * @return true if the IPC has the granted permission
     */
    public static boolean hasPermission(Context context, String permission) {
        int res = context.checkCallingOrSelfPermission(permission);
        Log.v(TAG, "permission: " + permission + " = \t\t" +
                (res == PackageManager.PERMISSION_GRANTED ? "GRANTED" : "DENIED"));
        return res == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * @param context     - the IPC context
     * @param permissions - The permissions to check
     * @return true if the IPC has the granted permission
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        boolean hasAllPermissions = true;
        for (String permission : permissions) {
            if (!hasPermission(context, permission)) {
                hasAllPermissions = false;
            }
        }
        return hasAllPermissions;
    }
}