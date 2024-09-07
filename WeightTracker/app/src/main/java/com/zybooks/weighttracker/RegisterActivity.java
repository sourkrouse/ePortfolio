package com.zybooks.weighttracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zybooks.weighttracker.ui.login.LoginActivity;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    public static final String EXTRA_PROFILE_ID = "com.zybooks.weighttracker.register_id";
    private WeightAndSeeDatabase mRegisterDb;

    private List<Register> mProfileList;
    private EditText mUserField;
    private EditText mPwdField;
    private EditText mFirstField;
    private EditText mLastField;
    private EditText mEmailField;
    private Button mRegisterButton;
    private TextView mSuccessText;
    private int mCurrentProfileIndex;
    //private ViewGroup mShowQuestionsLayout;
    //private ViewGroup mNoQuestionsLayout;
    private final int REQUEST_CODE_NEW_PROFILE = 0;
    private final int REQUEST_CODE_UPDATE_PROFILE = 1;
    private Register mProfile;
    private Register mDeletedProfile;
    private boolean mDarkTheme;
    private SharedPreferences mSharedPrefs;

    private long mID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mDarkTheme = mSharedPrefs.getBoolean(SettingsFragment.PREFERENCE_THEME, false);
        if (mDarkTheme) {
            setTheme(R.style.DarkTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUserField = findViewById(R.id.editTextUserName);
        mPwdField = findViewById(R.id.editTextPassword);
        mFirstField = findViewById(R.id.editTextFirstname);
        mLastField = findViewById(R.id.editTextLastname);
        mEmailField = findViewById(R.id.editTextEmailAddress);
        mRegisterButton = findViewById(R.id.buttonRegisterSubmit);

        mRegisterDb = WeightAndSeeDatabase.getInstance(getApplicationContext());
        // Get profile ID from Register
        Intent intent = getIntent();
        mID = intent.getLongExtra(EXTRA_PROFILE_ID, -1);
        if (mID == -1) {
            // Add new profile
            mProfile = new Register();
            //TODO - set title in action bar
            //setTitle(R.string.add_question);
        }
        else {
            // get existing profile
            mProfile = mRegisterDb.registerDao().getProfile(mID);
            //mAnswerText.setText(mQuestion.getAnswer());

            mUserField.setText(mProfile.getUser().toString());
            mPwdField.setText(mProfile.getPassword().toString());
            mFirstField.setText(mProfile.getFirst().toString());
            mLastField.setText(mProfile.getLast().toString());
            mEmailField.setText(mProfile.getEmail().toString());


            //TODO - for action bar
            //setTitle(R.string.update_question);
        }

    }
    @Override
    protected void onResume() {
        super.onResume();

        // If theme changed, recreate the activity so theme is applied
        boolean darkTheme = mSharedPrefs.getBoolean(SettingsFragment.PREFERENCE_THEME, false);
        if (darkTheme != mDarkTheme) {
            recreate();
        }

        // Load subjects here in case settings changed
        // TODO Loading of profiles or goal/start weights
        //mSubjectAdapter = new SubjectAdapter(loadSubjects());
        //mRecyclerView.setAdapter(mSubjectAdapter);
    }

    /*
    private List<Register> loadWeights() {
        String order = mSharedPrefs.getString(SettingsFragment.PREFERENCE_SUBJECT_ORDER, "1");
        // TODO options will be how to order weights
        switch (Integer.parseInt(order)) {
            case 0: return mRegisterDb.weightDao().getWeights();
            case 1: return mRegisterDb.weightDao.getWeightsNewerFirst();
            default: return mRegisterDb.weightDao.getWeightsOlderFirst();
        }

    }
    */

    public void submitNewRegisterButtonClick(View view){
        // TODO temporarily go to weights page without registration action
        //goToWeights();


        mProfile.setUser(mUserField.getText().toString());
        mProfile.setPassword(mPwdField.getText().toString());
        mProfile.setFirst(mFirstField.getText().toString());
        mProfile.setLast(mLastField.getText().toString());
        mProfile.setEmail(mEmailField.getText().toString());

        if (mID == -1) {
            // New profile
            mRegisterDb.registerDao().insertProfile(mProfile);

        } else {
            // Existing profile
            mRegisterDb.registerDao().updateProfile(mProfile);
            // TODO - send message

        }

        // Send back profile ID
        // TODO - THIS IS NOT WORKING

        Intent intent = new Intent();
        Log.d("IDTAG",String.valueOf(mProfile.getId()));
        startActivity(new Intent(this, WeightsActivity.class));
        intent.putExtra(WeightsActivity.EXTRA_PROFILE_ID, mProfile.getId());
        setResult(RESULT_OK, intent);
        //finish();


    }
    private void goToWeights() {
        //Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(new Intent(RegisterActivity.this, WeightsActivity.class));
        //finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.register_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sms_preference:
                Intent intent = new Intent(this,
                        SMSActivity.class);
                startActivity(intent);
                return true;
            case R.id.settings:
                intent = new Intent(RegisterActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}