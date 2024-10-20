package com.zybooks.weighttracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.zybooks.weighttracker.data.model.Register;
import com.zybooks.weighttracker.data.InitDb;
import com.zybooks.weighttracker.data.DAO.RegisterDao;
import com.zybooks.weighttracker.ui.login.RunOnThread;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
Last Updated 10/17/2024, Laura Brooks
PAGE DISPLAYS - first,last,email,username,password text fields
and button to add new account

UPDATES INCLUDE:
1) Changed colors to a softer green
2) Changed the color of the text labels
3) Added the special back button to go back to the home page
4) Updated the header so it displays the title, back button, and settings button
5) Adjusted manifest file to ensure all pages have an intended direction
6) Added data check function to check for correct data to enter
7) Using the RunOnThread object to check if username exists
 */

public class RegisterActivity extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    private AuthenticateUser authenticateUser;
    private EditText mUserField;
    private EditText mPwdField;
    private EditText mFirstField;
    private EditText mLastField;
    private EditText mEmailField;
    private Button mRegisterButton;
    private RegisterDao registerDao;
    private boolean mDarkTheme;
    private SharedPreferences mSharedPrefs;
    private int insertID;


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

        // calling the action bar to customize the back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow);
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


        // Set onClickListener for the Create Account button
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkDataFields()){
                    // checking the data fields first on each
                    errorDuplicateUser();

                } else {
                    // if fields are checked, move to insert
                    insertUser();
                    if (insertID != -1){

                        submitNewRegisterButtonClick();

                    }
                }


            }
        });





    }

    // resets background preference if changed
    // clear all fields so registration cannot be resubmitted when going back
    @Override
    protected void onResume() {
        super.onResume();

        // If theme changed, recreate the activity so theme is applied
        boolean darkTheme = mSharedPrefs.getBoolean(SettingsFragment.PREFERENCE_THEME, false);
        if (darkTheme != mDarkTheme) {
            recreate();
        }

        mUserField = findViewById(R.id.editTextUserName);
        mPwdField = findViewById(R.id.editTextPassword);
        mFirstField = findViewById(R.id.editTextFirstname);
        mLastField = findViewById(R.id.editTextLastname);
        mEmailField = findViewById(R.id.editTextEmailAddress);
        mUserField.setText("");
        mPwdField.setText("");
        mFirstField.setText("");
        mLastField.setText("");
        mEmailField.setText("");

    }


    // runs the functions that moves to the next page
    public void submitNewRegisterButtonClick(){

        goToDailyWeights(insertID);
        finish();


    }

    // database function to insert new user to register database
    // gets the data from the entry fields on the screen to insert
    // data checks are run before this function to check data accuracy
    // uses a Callable function to return the Integer value of the new user ID
    // try/catch will get exception if database function fails
    private void insertUser() {

        // insert record and get new ID

        Future<Integer> result = executorService.submit(new Callable<Integer>() {
            public Integer call() throws Exception {

                String username = mUserField.getText().toString().trim();
                String password = mPwdField.getText().toString().trim();
                String firstname = mFirstField.getText().toString().trim();
                String lastname = mLastField.getText().toString().trim();
                String email_txt = mEmailField.getText().toString().trim();

                    // Insert the new user
                    Register registerUser = new Register();
                    registerUser.setUser(username);
                    registerUser.setPassword(password);  // TODO: Secure, passwords should be hashed
                    registerUser.setFirst(firstname);
                    registerUser.setLast(lastname);
                    registerUser.setEmail(email_txt);

                    int newID = (int) registerDao.insertProfile(registerUser);

                if (newID > 0) {
                    return newID; // pass
                } else {
                    return -1; //fail
                }

            }
        });
        // try/catch to get the result from the Future object or the exception
        try {
            insertID = result.get();
        } catch (Exception e) {
            // failed
            insertID = -1;
        }
        executorService.shutdown(); // shutdown executor once complete
    }

    // using authenticateUser class to check data accuracy
    // also calling function to check duplicate users
    private boolean checkDataFields(){
        String username = mUserField.getText().toString().trim();
        String password = mPwdField.getText().toString().trim();
        String firstname = mFirstField.getText().toString().trim();
        String lastname = mLastField.getText().toString().trim();
        String email_txt = mEmailField.getText().toString().trim();

        if(checkDuplicateUser(username,password) != -1){
            return false;
        }
        // testing entered data for database criteria before inserting record
        authenticateUser = new AuthenticateUser();
        if(!authenticateUser.checkStringLength(username,25)
                || !authenticateUser.checkStringLength(firstname,50)
                || !authenticateUser.checkStringLength(lastname,50)
                || !authenticateUser.checkEmail(email_txt)
                || !authenticateUser.checkPassword(password)
        ){

            return false;
        }

        return true; // iff all checks pass
    }

    // calling function in data check
    // creates object from RunOnThread to check if user exists
    public int checkDuplicateUser(String username, String password) {

        RunOnThread future =  new RunOnThread(username, password) ;
        //System.gc(); // ensure object is destroyed
        return future.getUser();

    }

    // function to move to next screen, Weight Progress
    private void goToDailyWeights(int newID){

        Intent intent = new Intent(RegisterActivity.this, WeightsActivity.class);
        intent.putExtra(WeightsActivity.EXTRA_PROFILE_ID, newID);
        startActivity(intent);
        setResult(RESULT_OK, intent);
    }

    // error message when data entered is not accurate
    // NOTE: the error does not explain what happened. There was no time to add individual errors.
    public void errorDuplicateUser(){

        runOnUiThread(new Runnable() {
            public void run() {

                LayoutInflater myInflater = LayoutInflater.from(getApplicationContext());
                View view = myInflater.inflate(R.layout.error_duplicate_user, null);
                Toast myToast = new Toast(view.getContext());
                myToast.setView(view);
                myToast.setDuration(Toast.LENGTH_LONG);
                myToast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
                myToast.show();
            }
        });
        return;


    }

    // options menu, this is working but not useful because there is no other screens to navigate to
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


    // switch statement for option menu
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