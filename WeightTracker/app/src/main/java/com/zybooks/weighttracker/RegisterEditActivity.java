package com.zybooks.weighttracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class RegisterEditActivity extends AppCompatActivity {

    public static final String EXTRA_PROFILE_ID = "com.zybooks.studyhelper.register_id";

    // THIS PAGE MAY NOT BE NECESSARY - Currently not in use
    //variables for fields
    private EditText mUserField;
    private EditText mPwdField;
    private EditText mFirstField;
    private EditText mLastField;
    private EditText mEmailField;


    private WeightAndSeeDatabase mRegisterDb;
    private long mRegisterId;
    private Register mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_edit);


        mUserField = findViewById(R.id.editTextUserName);
        mPwdField = findViewById(R.id.editTextPassword);
        mFirstField = findViewById(R.id.editTextFirstname);
        mLastField = findViewById(R.id.editTextLastname);
        mEmailField = findViewById(R.id.editTextEmailAddress);

        mRegisterDb = WeightAndSeeDatabase.getInstance(getApplicationContext());

        // Get profile ID from RegisterActivity
        Intent intent = getIntent();
        mRegisterId = intent.getLongExtra(EXTRA_PROFILE_ID, -1);

        //TODO - add action bar
        //ActionBar actionBar = getSupportActionBar();

        if (mRegisterId == -1) {
            // Add new registration
            mRegister = new Register();
            //TODO - set title in action bar
            //setTitle(R.string.add_question);
        }
        else {
            // Update existing registration
            mRegister = mRegisterDb.registerDao().getProfile(mRegisterId);
            mUserField.setText(mRegister.getUser());
            mPwdField.setText(mRegister.getPassword());
            mFirstField.setText(mRegister.getFirst());
            mLastField.setText(mRegister.getLast());
            mEmailField.setText(mRegister.getEmail());
            //TODO - for action bar
            //setTitle(R.string.update_question);
        }

    }

    public void saveButtonClick(View view) {

        mRegister.setUser(mUserField.getText().toString());
        mRegister.setPassword(mPwdField.getText().toString());
        mRegister.setFirst(mFirstField.getText().toString());
        mRegister.setLast(mLastField.getText().toString());
        mRegister.setEmail(mEmailField.getText().toString());

        if (mRegisterId == -1) {
            // New profile
            mRegisterDb.registerDao().insertProfile(mRegister);
        } else {
            // Existing profile
            mRegisterDb.registerDao().updateProfile(mRegister);
        }

        // Send back register ID
        Intent intent = new Intent();
        Log.d("IDTAG",String.valueOf(mRegister.getId()));
        intent.putExtra(EXTRA_PROFILE_ID, mRegister.getId());
        setResult(RESULT_OK, intent);
        finish();
    }
}