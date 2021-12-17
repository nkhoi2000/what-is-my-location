package com.huawei.finalassignment.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
    private static final String TAG = "Account";
    private Account account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.btn_login).setOnClickListener(view -> silentSignInByHwId());
    }
    private void silentSignInByHwId() {
        // 1. Use AccountAuthParams to specify the user information to be obtained, including the user ID (OpenID and UnionID), email address, and profile (nickname and picture).
        // 2. By default, DEFAULT_AUTH_REQUEST_PARAM specifies two items to be obtained, that is, the user ID and profile.
        // 3. If your app needs to obtain the user's email address, call setEmail().
        // Set HUAWEI ID sign-in authorization parameters.
        AccountAuthParams mAuthParam = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setEmail()
                .createParams();
        // Use AccountAuthParams to build AccountAuthService.
        mAuthService = AccountAuthManager.getService(this, mAuthParam);
        // Use silent sign-in to sign in with a HUAWEI ID.
        Task<AuthAccount> task = mAuthService.silentSignIn();
        task.addOnSuccessListener(authAccount -> {
            Intent intent = new Intent(LoginActivity.this,MapActivity.class);
            account = new Account(authAccount.getDisplayName(),authAccount.getEmail(),authAccount.getAvatarUri().toString());
            intent.putExtra("Account",account);
            startActivity(intent);
            Toast.makeText(LoginActivity.this,"Hello " + authAccount.getDisplayName(), Toast.LENGTH_SHORT).show();
        });
        task.addOnFailureListener(e -> {
            // The silent sign-in fails. Use the getSignInIntent() method to show the authorization or sign-in screen.
            if (e instanceof ApiException) {
                Intent signInIntent = mAuthService.getSignInIntent();
                Log.i(TAG, "FAIL: " +  e.getMessage() );
                startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
            }
        });
    }
}