package com.prepr.b4walkin.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.prepr.b4walkin.R;
import com.prepr.b4walkin.credential.AccountUtil;
import com.prepr.b4walkin.data.user.User;

import java.util.Objects;

public class ProfileFragment extends PreferenceFragmentCompat {
    private static final int SIGN_IN_REQUEST_CODE = 1;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        AccountUtil util = AccountUtil.get(Objects.requireNonNull(getActivity()).getApplication());
        Preference signIn = findPreference("sign_in");
        Preference signOut = findPreference("sign_out");

        assert signIn != null;
        signIn.setOnPreferenceClickListener(preference -> {
            GoogleSignInClient googleSignInClient = util.getGSIC();
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, SIGN_IN_REQUEST_CODE);
            return false;
        });

        assert signOut != null;
        signOut.setOnPreferenceClickListener(preference -> {
            util.logout();
            util.getGSIC().signOut();
            updateAccountMenuVisibility();
            return false;
        });

        updateAccountMenuVisibility();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        AccountUtil util = AccountUtil.get(Objects.requireNonNull(getActivity()).getApplication());
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    Log.w("Preference", "Sign in : " + account.getEmail());
                    User user = new User();
                    user.setEmail(account.getEmail());
                    user.setFName(account.getGivenName());
                    user.setLName(account.getFamilyName());

                    util.signIn(user);
                }
            } catch (ApiException e) {
                Log.w("Preference", "signInResult : failed code = " + e.getStatusCode());
                e.printStackTrace();
            }

            updateAccountMenuVisibility();
        }
    }

    private void updateAccountMenuVisibility() {
        AccountUtil util = AccountUtil.get(Objects.requireNonNull(getActivity()).getApplication());
        Preference signIn = findPreference("sign_in");
        Preference signOut = findPreference("sign_out");
        assert signIn != null;
        assert signOut != null;

        if (util.getLoggedInUser() != null) {
            signIn.setVisible(false);
            signOut.setVisible(true);
        } else {
            signIn.setVisible(true);
            signOut.setVisible(false);
        }
    }
}
