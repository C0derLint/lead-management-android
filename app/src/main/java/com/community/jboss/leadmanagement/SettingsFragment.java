package com.community.jboss.leadmanagement;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v14.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.community.jboss.leadmanagement.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {


    public SettingsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preferences);

        if(getActivity()!=null) {
            Activity mActivity = getActivity();

            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            final SharedPreferences sharedPref = mActivity.getPreferences(Context.MODE_PRIVATE);
            final String currentServer = sharedPref.getString(getString(R.string.saved_server_ip), "https://github.com/jboss-outreach");

            final EditTextPreference mPreference = (EditTextPreference) findPreference("server_location");
            final SwitchPreference mToggleMode = (SwitchPreference) findPreference("dark_theme");
            Preference signOut = findPreference("sign_out");
            mToggleMode.setOnPreferenceChangeListener( (preference, newValue) -> {
                mActivity.startActivity(new Intent(mActivity, MainActivity.class));
                mActivity.finish();
                return true;
            });

            if(mAuth.getCurrentUser() == null) {
                signOut.setSummary("You are not signed in");
                signOut.setEnabled(false);
            } else {
                signOut.setSummary("Sign out from your account.");
            }

            signOut.setOnPreferenceClickListener( preference ->  {
                mAuth.signOut();
                Snackbar.make(mActivity.findViewById(R.id.settings_content_main), "Signed out", Snackbar.LENGTH_LONG).show();
                return true;
            });
            mPreference.setSummary(currentServer);
            mPreference.setText(currentServer);

        }
    }
}
