package com.zybooks.weighttracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static String PREFERENCE_THEME = "pref_theme";
    public static String PREFERENCE_SMS = "pref_sms";
    public static String PREFERENCE_WEIGHT_ORDER = "pref_weight_order";
    //public static String PREFERENCE_DEFAULT_QUESTION = "pref_default_question";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        // Access the default shared prefs
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());

        //setPrefSummarySubjectOrder(sharedPrefs);
        setPrefSummarySMSQuestion(sharedPrefs);
    }

    // Set the summary to the currently selected weight order
    /*
    private void setPrefSummarySubjectOrder(SharedPreferences sharedPrefs) {
        String order = sharedPrefs.getString(PREFERENCE_WEIGHT_ORDER, "1");
        String[] weightOrders = getResources().getStringArray(R.array.pref_weight_order);
        Preference weightOrderPref = findPreference(PREFERENCE_WEIGHT_ORDER);
        weightOrderPref.setSummary(weightOrders[Integer.parseInt(order)]);
    }

     */

    // Set the summary to the default question
    /*
    private void setPrefSummaryDefaultQuestion(SharedPreferences sharedPrefs) {
        String defaultQuestion = sharedPrefs.getString(PREFERENCE_DEFAULT_QUESTION, "");
        defaultQuestion = defaultQuestion.trim();
        Preference questionPref = findPreference(PREFERENCE_DEFAULT_QUESTION);
        if (defaultQuestion.length() == 0) {
            questionPref.setSummary(getResources().getString(R.string.pref_none));
        }
        else {
            questionPref.setSummary(defaultQuestion);
        }
    }
    */
    private void setPrefSummarySMSQuestion(SharedPreferences sharedPrefs){
        //TODO add function to set preference for SMS
    }
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PREFERENCE_THEME)) {
            // Recreate the activity so the theme takes effect
            getActivity().recreate();
        }
        //else if (key.equals(PREFERENCE_WEIGHT_ORDER)) {
        //    setPrefSummarySubjectOrder(sharedPreferences);
        //}
        else if (key.equals(PREFERENCE_SMS)) {
            setPrefSummarySMSQuestion(sharedPreferences);
        }
    }
}