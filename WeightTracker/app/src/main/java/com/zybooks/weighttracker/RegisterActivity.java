package com.zybooks.weighttracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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

import com.google.android.material.snackbar.Snackbar;
import com.zybooks.weighttracker.data.model.Register;
import com.zybooks.weighttracker.data.AppDatabase;
import com.zybooks.weighttracker.data.DbConfig;
import com.zybooks.weighttracker.data.InitDb;
import com.zybooks.weighttracker.data.DAO.RegisterDao;
import com.zybooks.weighttracker.ui.login.LoginActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    //public static final String EXTRA_PROFILE_ID = "com.zybooks.weighttracker.register_id";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    //private WeightAndSeeDatabase mRegisterDb;

    Button button;
    CoordinatorLayout layout;

    private EditText mUserField;
    private EditText mPwdField;
    private EditText mFirstField;
    private EditText mLastField;
    private EditText mEmailField;
    private Button mRegisterButton;
    private TextView mSuccessText;
    private int mCurrentProfileIndex;
    //private final int REQUEST_CODE_NEW_PROFILE = 0;
    //private final int REQUEST_CODE_UPDATE_PROFILE = 1;
    //private Register mProfile;
    private RegisterDao registerDao;
    //private Register mDeletedProfile;
    private boolean mDarkTheme;
    private SharedPreferences mSharedPrefs;

    //private int mID;

    /*
    MAIN THREAD
    Arguments - passes in the instance of the user record (in this case the ID)
    Calls - preferences check, pulls theme of front end, initialize database,
    set user field variable connections to the form field IDs, button click listener
    Summary - runs at the time of screen load and manages all other method/class calls
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mDarkTheme = mSharedPrefs.getBoolean(SettingsFragment.PREFERENCE_THEME, false);
        if (mDarkTheme) {
            setTheme(R.style.DarkTheme);
        }

        // pulls XML file for front end design, checks for data being passed through instance argument
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize the RegisterDao
        registerDao = InitDb.appDatabase.registerDao();

        // sets variables of user fields by connecting to ID of UI/UX field
        mUserField = findViewById(R.id.editTextUserName);
        mPwdField = findViewById(R.id.editTextPassword);
        mFirstField = findViewById(R.id.editTextFirstname);
        mLastField = findViewById(R.id.editTextLastname);
        mEmailField = findViewById(R.id.editTextEmailAddress);
        mRegisterButton = findViewById(R.id.buttonRegisterSubmit);


        // Set onClickListener for the login button
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertUser();
                submitNewRegisterButtonClick();
            }
        });





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


    }

    private void insertUser() {

        // TODO: check for existing profile before inserting new record
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                String username = mUserField.getText().toString().trim();
                String password = mPwdField.getText().toString().trim();
                String firstname = mFirstField.getText().toString().trim();
                String lastname = mLastField.getText().toString().trim();
                String email_txt = mEmailField.getText().toString().trim();


                if(!AuthenticateUser.checkStringLength(username,25)
                        && !AuthenticateUser.checkStringLength(firstname,50)
                        && !AuthenticateUser.checkStringLength(lastname,50)
                        && !AuthenticateUser.checkEmail(email_txt)
                        && !AuthenticateUser.checkPassword(password)
                ){
                    //TODO: add message when credentials fail
                }

                // check if username exists before inserting a new record
                if (registerDao.getProfileByUsername(username) == null) {
                    // Insert the test user
                    Register registerUser = new Register();
                    registerUser.setUser(username);
                    registerUser.setPassword(password);  // TODO: Secure, passwords should be hashed
                    registerUser.setFirst(firstname);
                    registerUser.setLast(lastname);
                    registerUser.setEmail(email_txt);

                    registerDao.insertProfile(registerUser);
                } else {

                    //TODO: not working when trying to pull on same activity
                    errorDuplicateUser();
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

    public void errorDuplicateUser(){
        //Toast.makeText(this, "This username already exists", Toast.LENGTH_SHORT).show();

        Snackbar snackbarDuplicate = Snackbar.make(layout, "This username already exists", Snackbar.LENGTH_SHORT);
        snackbarDuplicate.show();
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
            case R.id.add_daily_weight:
                intent = new Intent(this, AddDailyWeight.class);
                startActivity(intent);
                //TODO: add case for add new weight
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}