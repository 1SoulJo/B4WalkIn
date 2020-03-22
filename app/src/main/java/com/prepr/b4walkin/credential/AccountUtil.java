package com.prepr.b4walkin.credential;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.prepr.b4walkin.data.user.User;
import com.prepr.b4walkin.repo.UserRepo;

public class AccountUtil {
    private static volatile AccountUtil INSTANCE;

    private static final String PREF_ACCOUNT_NAME = "pref_account_name";
    private static final String PREF_ACCOUNT_KEY = "pref_account_key";

    private User mUser = null;
    private UserRepo mUserRepo;
    private GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences mSharedPreference;

    private AccountUtil(Application application) {
        mUserRepo = new UserRepo(application);

        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
        mGoogleSignInClient = GoogleSignIn.getClient(application, gso);

        mSharedPreference =
                application.getSharedPreferences(PREF_ACCOUNT_NAME, Context.MODE_PRIVATE);

        int savedId = mSharedPreference.getInt(PREF_ACCOUNT_KEY, -1);
        if (savedId > -1) {
            mUser = mUserRepo.findById(savedId);
            saveStatus();
        }
    }

    public static AccountUtil get(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new AccountUtil(application);
        }

        return INSTANCE;
    }

    public void logout() {
        mUser = null;

        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.remove(PREF_ACCOUNT_KEY);
        editor.apply();
    }

    public void signIn(User newUser) {
        User user = mUserRepo.findByEmail(newUser.getEmail());
        if (user == null) {
            int id = (int) mUserRepo.insert(newUser);
            mUser = newUser;
            mUser.setId(id);
        } else {
            mUser = user;
        }
        saveStatus();
    }

    public User getLoggedInUser() {
        return mUser;
    }

    public GoogleSignInClient getGSIC() {
        return mGoogleSignInClient;
    }

    private void saveStatus() {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putInt(PREF_ACCOUNT_KEY, mUser.getId());
        editor.apply();
    }
}
