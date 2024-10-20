package com.zybooks.weighttracker.ui.login;

import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.zybooks.weighttracker.MainActivity;
import com.zybooks.weighttracker.R;
import com.zybooks.weighttracker.SMSActivity;
import com.zybooks.weighttracker.SettingsActivity;
import com.zybooks.weighttracker.SettingsFragment;
import com.zybooks.weighttracker.RegisterActivity;
import com.zybooks.weighttracker.WeightAndSeeDatabase;
import com.zybooks.weighttracker.WeightsActivity;
import com.zybooks.weighttracker.databinding.ActivityLoginBinding;

/**
Last Updated 10/19/2024, Laura Brooks
PAGE DISPLAYS - username/password text fields and button to login

UPDATES INCLUDE:
1) Changed colors to a softer green
2) Added the special back button to go back to the home page
3) Updated the header so it only displays the title and settings button
4) Adjusted manifest file to ensure all pages have an intended direction
5) OnResume resets the text fields so they cannot be seen if navigating back to the screen
6) Getting username and password entered from user, running through DB code on
    view model, returning the user ID to the Weights Activity page


 */
public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_PROFILE_ID = "com.zybooks.weighttracker.register_id";

    private int mRId = -1;
    private EditText mUserField;
    private EditText mPwdField;
    private Button loginButton;
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private boolean mDarkTheme;
    private SharedPreferences mSharedPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mDarkTheme = mSharedPrefs.getBoolean(SettingsFragment.PREFERENCE_THEME, false);
        if (mDarkTheme) {
            setTheme(R.style.DarkTheme);
        }

        super.onCreate(savedInstanceState);

        // setting a custom back arrow in the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // connecting the view to fields
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // set ID of login button to access from onClick
        loginButton = findViewById(R.id.login);

        savedInstanceState = getIntent().getExtras();
        // Get profile ID from argument passed
        if (savedInstanceState != null)
            mRId = savedInstanceState.getInt(EXTRA_PROFILE_ID, -1);

        // initializing the View Model where the main DB work is done
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);


        // on button click, get the userID of the logged in user, if found (using view model)
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get user entry from input fields - replace binding.username
                mUserField = findViewById(R.id.username);
                mPwdField = findViewById(R.id.password);
                String usernameEditText = mUserField.getText().toString().trim();
                String passwordEditText = mPwdField.getText().toString().trim();

                // run login method from ViewModel class to get user ID from database
                mRId = loginViewModel.login(usernameEditText,passwordEditText);
                if (mRId != -1) {
                    weightsScreen(mRId);
                } else {
                    // show error
                    TextView textView = (TextView)findViewById(R.id.login_error_message);
                    textView.setText("LOGIN NOT FOUND!"); //set text for login error message
                }

             }
        });
    }


    // removes any text from edit fields if user navigates back to the screen.
    @Override
    public void onResume(){
        super.onResume();

        mUserField = findViewById(R.id.username);
        mPwdField = findViewById(R.id.password);
        mUserField.setText("");
        mPwdField.setText("");

    }

    // called from onCreate/onClick, goes to the weight screen with the user ID
    private void weightsScreen(int userID) {
        Intent intent = new Intent(LoginActivity.this, WeightsActivity.class);
        intent.putExtra(WeightsActivity.EXTRA_PROFILE_ID, userID);
        startActivity(intent);
        setResult(RESULT_OK, intent);


    }

    // getter method to go to registration screen
    public void newRegisterButtonClick(View view){
        newRegister();
    }

    // open registration screen
    private void newRegister() {

        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }

    // setup menu XML and options within
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

    // switch statement to run menu options
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
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                //finish(); // returns the user to parent page
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}