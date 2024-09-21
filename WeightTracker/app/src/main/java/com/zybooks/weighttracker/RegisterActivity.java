package com.zybooks.weighttracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.zybooks.weighttracker.data.model.Register;
import com.zybooks.weighttracker.data.InitDb;
import com.zybooks.weighttracker.data.DAO.RegisterDao;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Last Updated 9/18/2024, Laura Brooks
PAGE DISPLAYS - first,last,email,username,password text fields
and button to add new account

UPDATES INCLUDE:
1) Changed colors to a softer green
2) Changed the color of the text labels
3) Added the special back button to go back to the home page
4) Updated the header so it displays the title, back button, and settings button
5) Adjusted manifest file to ensure all pages have an intended direction
TODO Items line 62,192,205
 */

public class RegisterActivity extends AppCompatActivity {

    //public static final String EXTRA_PROFILE_ID = "com.zybooks.weighttracker.register_id";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();


    private EditText mUserField;
    private EditText mPwdField;
    private EditText mFirstField;
    private EditText mLastField;
    private EditText mEmailField;
    private Button mRegisterButton;
    private TextView mSuccessText;
    private int mCurrentProfileIndex;
    private RegisterDao registerDao;
    private boolean mDarkTheme;
    private SharedPreferences mSharedPrefs;


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

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow);

        // showing the custom back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

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

        String username = mUserField.getText().toString().trim();
        String password = mPwdField.getText().toString().trim();

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
                            Toast.makeText(RegisterActivity.this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
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
                    errorDuplicateUser();
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
    public void errorDuplicateUser(){
        //Toast.makeText(this, "This username already exists", Toast.LENGTH_SHORT).show();

       // InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), "Incorrect!!", Snackbar.LENGTH_LONG)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
        snackbar.setActionTextColor(Color.RED);

        snackbar.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spinner_menu, menu);

        MenuItem item = menu.findItem(R.id.menu_closed);
        Spinner spinner = (Spinner) item.getActionView();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        return true;


    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
            case android.R.id.home:
                intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}