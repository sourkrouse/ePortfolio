package com.zybooks.weighttracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zybooks.weighttracker.data.model.Register;
import com.zybooks.weighttracker.data.AppDatabase;
import com.zybooks.weighttracker.data.DbConfig;
import com.zybooks.weighttracker.data.InitDb;
import com.zybooks.weighttracker.data.DAO.RegisterDao;
import com.zybooks.weighttracker.ui.login.LoginActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    public static final String EXTRA_PROFILE_ID = "com.zybooks.weighttracker.register_id";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
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
    private final int REQUEST_CODE_NEW_PROFILE = 0;
    private final int REQUEST_CODE_UPDATE_PROFILE = 1;
    private Register mProfile;
    private RegisterDao registerDao;
    private Register mDeletedProfile;
    private boolean mDarkTheme;
    private SharedPreferences mSharedPrefs;

    private int mID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mDarkTheme = mSharedPrefs.getBoolean(SettingsFragment.PREFERENCE_THEME, false);
        if (mDarkTheme) {
            setTheme(R.style.DarkTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize the RegisterDao
        registerDao = InitDb.appDatabase.registerDao();

        mUserField = findViewById(R.id.editTextUserName);
        mPwdField = findViewById(R.id.editTextPassword);
        mFirstField = findViewById(R.id.editTextFirstname);
        mLastField = findViewById(R.id.editTextLastname);
        mEmailField = findViewById(R.id.editTextEmailAddress);
        mRegisterButton = findViewById(R.id.buttonRegisterSubmit);

        //mRegisterDb = WeightAndSeeDatabase.getInstance(getApplicationContext());


        // Set onClickListener for the login button
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertTestUser();
                submitNewRegisterButtonClick();
            }
        });

        //insert a test user
        insertTestUser();

        // Get profile ID from Register
        /*
        Intent intent = getIntent();
        mID = (int) intent.getLongExtra(EXTRA_PROFILE_ID, -1);
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


        }

         */

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

    public void submitNewRegisterButtonClick(){
        // TODO temporarily go to weights page without registration action
        //goToWeights();
        String username = mUserField.getText().toString().trim();
        String password = mPwdField.getText().toString().trim();
        //String firstname = mFirstField.getText().toString().trim();
        //String lastname = mLastField.getText().toString().trim();
        //String email_txt = mEmailField.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter login credentials", Toast.LENGTH_SHORT).show();
            return;
        }


        // Execute the database query on a background thread
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final Register register = registerDao.getProfileByUsername(username);
                Log.d("REGISTNAME",username);
                // Handle the result on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (register != null) {
                            // TODO: need password check -  && password.equals(register.getPassword())

                            // Successful login, navigate to the main activity
                            mCurrentProfileIndex = register.getId();
                            goToDailyWeights(mCurrentProfileIndex);
                            finish();
                        } else {
                            // Invalid login credentials
                            Log.d("LOGINERROR","Invalid Login");
                            //Toast.makeText(LoginActivity.this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        /*
        if (mID == -1) {
            // New profile
            mRegisterDb.registerDao().insertProfile(mProfile);

        } else {
            // Existing profile
            mRegisterDb.registerDao().updateProfile(mProfile);
            // TODO - send message

        }
        */
        // Send back profile ID
        // TODO - THIS IS NOT WORKING

        /*
        Intent intent = new Intent();
        Log.d("IDTAG",String.valueOf(mProfile.getId()));
        Log.d("NAMETAG",String.valueOf(mProfile.getLast()));
        startActivity(new Intent(this, WeightsActivity.class));
        intent.putExtra(WeightsActivity.EXTRA_PROFILE_ID, mProfile.getId());
        setResult(RESULT_OK, intent);
        */

        //finish();


    }
    private void insertTestUser() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                String username = mUserField.getText().toString().trim();
                String password = mPwdField.getText().toString().trim();
                String firstname = mFirstField.getText().toString().trim();
                String lastname = mLastField.getText().toString().trim();
                String email_txt = mEmailField.getText().toString().trim();

                if (registerDao.getProfileByUsername(username) == null) {
                    // Insert the test user
                    Register registerUser = new Register();
                    registerUser.setUser(username);
                    registerUser.setPassword(password);  // TODO: Secure, passwords should be hashed
                    registerUser.setFirst(firstname);
                    registerUser.setLast(lastname);
                    registerUser.setEmail(email_txt);

                    registerDao.insertProfile(registerUser);
                }

                // Check if the test user 'admin' already exists in the db
                /*
                if (registerDao.getProfileByUsername("admin") == null) {
                    // Insert the test user
                    Register testUser = new Register();
                    testUser.setUser("admin");
                    testUser.setPassword("admin");  // Note: In a real application, passwords should be hashed
                    testUser.setFirst("Laura");
                    testUser.setLast("Brooks");
                    testUser.setEmail("admin@example.com");

                    registerDao.insertProfile(testUser);
                }
                */

            }
        });
    }
    private void goToDailyWeights(int newID){


        //Log.d("IDTAG",String.valueOf(mProfile.getId()));
        //Log.d("NAMETAG",String.valueOf(mProfile.getLast()));
        Intent intent = new Intent(RegisterActivity.this, WeightsActivity.class);
        //startActivity(new Intent(RegisterActivity.this, WeightsActivity.class));
        intent.putExtra(WeightsActivity.EXTRA_PROFILE_ID, newID);
        startActivity(intent);
        setResult(RESULT_OK, intent);
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